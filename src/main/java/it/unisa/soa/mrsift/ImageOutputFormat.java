package it.unisa.soa.mrsift;

import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;
import org.apache.hadoop.io.Text;

/**
 * 
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo
 */
public class ImageOutputFormat extends FileOutputFormat<Text, MatWritable> {

    @Override
    public RecordWriter<Text, MatWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MatRecordWriter();
    }
}