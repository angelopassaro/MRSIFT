package it.unisa.soa.mrsift;

import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author antonio
 */
public class ImageOutputFormat extends FileOutputFormat<Text, BytesWritable> {

    @Override
    public RecordWriter<Text, BytesWritable> getRecordWriter(TaskAttemptContext tac) throws IOException, InterruptedException {
    Path path = FileOutputFormat.getOutputPath(tac);
    FileSystem fs = path.getFileSystem(tac.getConfiguration());
    ImageFileWriter writer = new ImageFileWriter(fs, path);
    return writer;
    }
    
}
