package it.unisa.soa.mrsift;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordReader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

/**
 * Created by Epanchee on 24.02.15.
 */
public class MatImageRecordReader extends RecordReader<NullWritable, MatImageWritable> {

    private String fileName;
    private FSDataInputStream fileStream;
    private boolean processed = false;
    private MatImageWritable im;


    protected MatImageWritable readImage(FSDataInputStream fileStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = fileStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] temporaryImageInMemory = buffer.toByteArray();
        buffer.close();

        Mat out = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.IMREAD_ANYCOLOR);

        return new MatImageWritable(out);
    }

    @Override
    public void close() throws IOException {
        fileStream.close();
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public void initialize(org.apache.hadoop.mapreduce.InputSplit inputSplit, org.apache.hadoop.mapreduce.TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        org.apache.hadoop.mapreduce.lib.input.FileSplit fileSplit = (org.apache.hadoop.mapreduce.lib.input.FileSplit) inputSplit;
        Configuration job = taskAttemptContext.getConfiguration();
        final Path file = fileSplit.getPath();
        fileName = file.getName();
        FileSystem fs = file.getFileSystem(job);
        fileStream = fs.open(file);
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!processed) {
            im = readImage(fileStream);
            if (im != null) {
                setFilenameAndFormat();
                processed = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public MatImageWritable getCurrentValue() throws IOException, InterruptedException {
        return im;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return processed ? 1.0f : 0.0f;
    }

    private void setFilenameAndFormat() {
        if ((fileName != null) && (im != null)) {
            // Determining image format
            int dotPos = fileName.lastIndexOf(".");
            if (dotPos > -1) {
                im.setFileName(fileName.substring(0, dotPos));
                im.setFormat(fileName.substring(dotPos + 1));
            } else {
                im.setFileName(fileName);
            }
        }
    }
}