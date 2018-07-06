package it.unisa.soa.mrsift;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 *
 * @author antonio
 */
class ImageFileWriter extends RecordWriter<Text, BytesWritable> {
  
  private final FileSystem fs;
  private DataOutputStream out;
  private final Path path; 

  public ImageFileWriter(FileSystem fs, Path path) {
    this.fs = fs;
    this.path = path;
  }
  
  

    @Override
    public void write(Text k, BytesWritable v) throws IOException, InterruptedException {
        this.out = fs.create(new Path(this.path, k.toString()));
        out.write(v.getBytes());
    }

    @Override
    public void close(TaskAttemptContext tac) throws IOException, InterruptedException {
        out.close();
    }
    
}
