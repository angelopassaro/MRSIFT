package it.unisa.soa.mrsift;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import java.io.IOException;

/**
 * Custom FileInputFormat to read images. images are not splittable because SIFT
 * requires the whole image for feature's extraction
 *
 * @author Diego Avella
 * @auhor Angelo Passaro
 * @author Antonio Addeo
 * @param <Text> object name
 * @param <MatImageWritable> the image associated
 */
public class ImageInputFormat<Text, MatImageWritable> extends FileInputFormat {

  @Override
  protected boolean isSplitable(JobContext context, Path file) {
    return false;
  }

  @Override
  public RecordReader createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
    return new MatRecordReader();
  }
}
