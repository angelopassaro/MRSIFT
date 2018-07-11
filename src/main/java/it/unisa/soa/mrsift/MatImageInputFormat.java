package it.unisa.soa.mrsift;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.JobContext;

/**
 * Base class for MIPr image input formats
 */
public abstract class MatImageInputFormat<K,V> extends org.apache.hadoop.mapreduce.lib.input.FileInputFormat {

  /**
   *  Return false because image cannot be splitted
   */
  @Override
  protected boolean isSplitable(JobContext context, Path file) {
    return false;
  }

}
