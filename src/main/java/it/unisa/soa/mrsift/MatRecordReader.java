package it.unisa.soa.mrsift;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordReader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * 
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo
 * This class is the concrete implementation of RecordReader for read a stream
 * of bytes into a MatWritable Image. Each record return a tuple with key the 
 * filename and the value the image associated
 */
public class MatRecordReader extends RecordReader<Text, MatWritable> {

  private String fileName;
  private FSDataInputStream fileStream;
  private boolean processed = false;
  private MatWritable image;

  private MatWritable readImage(FSDataInputStream fileStream) throws IOException {
    byte[] temporaryImageInMemory;
    try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
      int nRead;
      byte[] data = new byte[16384];
      while ((nRead = fileStream.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      } 
      buffer.flush();
      temporaryImageInMemory = buffer.toByteArray();
    }
    Mat out = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
    return new MatWritable(out);
  }

  @Override
  public void close() throws IOException {
    this.fileStream.close();
  }

  @Override
  public Text getCurrentKey() throws IOException, InterruptedException {
    return new Text(this.fileName);
  }

  @Override
  public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
    FileSplit fileSplit = (FileSplit) inputSplit;
    Configuration job = taskAttemptContext.getConfiguration();
    Path file = fileSplit.getPath();
    this.fileName = file.getName();
    FileSystem fs = file.getFileSystem(job);
    this.fileStream = fs.open(file);
  }

  @Override
  public boolean nextKeyValue() throws IOException, InterruptedException {
    if (!this.processed) {
      this.image = readImage(this.fileStream);
      if (this.image != null) {
        setFilenameAndFormat();
        this.processed = true;
        return true;
      }
    }
    return false;
  }

  @Override
  public MatWritable getCurrentValue() throws IOException, InterruptedException {
    return this.image;
  }

  @Override
  public float getProgress() throws IOException, InterruptedException {
    return this.processed ? 1.0f : 0.0f;
  }

  private void setFilenameAndFormat() {
    if ((this.fileName != null) && (this.image != null)) {
      int dotPos = this.fileName.lastIndexOf(".");
      if (dotPos > -1) {
        this.image.setFileName(this.fileName.substring(0, dotPos));
        this.image.setFormat(this.fileName.substring(dotPos + 1));
      } else {
        this.image.setFileName(this.fileName);
      }
    }
  }
}
