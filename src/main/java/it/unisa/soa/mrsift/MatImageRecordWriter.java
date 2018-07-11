package it.unisa.soa.mrsift;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;

/**
 * Created by Epanchee on 24.02.15.
 */
public class MatImageRecordWriter extends RecordWriter<NullWritable, MatImageWritable> {

    private  TaskAttemptContext taskAttemptContext;

    public MatImageRecordWriter() {
        super();
    }

    @Override
    public void write(NullWritable nullWritable, MatImageWritable matImageWritable) throws IOException, InterruptedException {

        if (matImageWritable.getImage() != null) {
            FSDataOutputStream imageFile = null;
            Configuration job = taskAttemptContext.getConfiguration();
            Path file = FileOutputFormat.getOutputPath(taskAttemptContext);
            FileSystem fs = file.getFileSystem(job);
            // Constructing matImageWritable filename and path
            Path imageFilePath = new Path(file, matImageWritable.getFileName() + "."
                    + matImageWritable.getFormat());

            try {
                // Creating file
                imageFile = fs.create(imageFilePath);
                writeImage(matImageWritable, imageFile);

                // Write image to file using ImageIO
                //ImageIO.write(bufferedImageWritable.getBufferedImage(), bufferedImageWritable.getFormat(), imageFile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeStream(imageFile);
            }
        }

    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

    }

    protected void writeImage(MatImageWritable image, FSDataOutputStream imageFile) throws Exception {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode("." + image.getFormat(), image.getImage(), mob);
        imageFile.write(mob.toArray());
    }
}