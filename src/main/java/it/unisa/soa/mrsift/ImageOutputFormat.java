package it.unisa.soa.mrsift;

import java.io.IOException;
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
    ImageFileWriter writer = new ImageFileWriter();
    return writer;
    }
    
}
