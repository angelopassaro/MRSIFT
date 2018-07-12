package it.unisa.soa.mrsift;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.IOException;
import org.apache.hadoop.io.Text;

/**
 *
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo This class is the concrete implementation od
 * RecordWriter and write an Image on Filesystem
 */
public class MatRecordWriter extends RecordWriter<Text, MatWritable> {

  private TaskAttemptContext taskAttemptContext;

  public MatRecordWriter() {
    super();
  }

  @Override
  public void write(Text filename, MatWritable matImageWritable) throws IOException, InterruptedException {
    if (matImageWritable.getImage() != null) {
      FSDataOutputStream imageFile = null;
      Configuration job = taskAttemptContext.getConfiguration();
      Path file = FileOutputFormat.getOutputPath(taskAttemptContext);
      FileSystem fs = file.getFileSystem(job);
      Path imageFilePath = new Path(file, String.format("%s.%s", matImageWritable.getFileName(), matImageWritable.getFormat()));
      try {
        imageFile = fs.create(imageFilePath);
        writeImage(matImageWritable, imageFile);
      } catch (Exception e) {
        System.err.println(e);
      } finally {
        IOUtils.closeStream(imageFile);
      }
    }
  }

  @Override
  public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

  }

  protected void writeImage(MatWritable image, FSDataOutputStream imageFile) throws Exception {
    MatOfByte mob = new MatOfByte();
    Imgcodecs.imencode("." + image.getFormat(), image.getImage(), mob);
    imageFile.write(mob.toArray());
  }
}
