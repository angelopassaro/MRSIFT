package it.unisa.soa.mrsift;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Base class for MIPr image input formats
 */
public class MatImageInputFormat<NullWritable,MatImageWritable> extends FileInputFormat {

  /**
   *  Return false because image cannot be splitted
   */
  @Override
  protected boolean isSplitable(JobContext context, Path file) {
    return false;
  }

    @Override
    public RecordReader createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MatImageRecordReader();
    }
}
