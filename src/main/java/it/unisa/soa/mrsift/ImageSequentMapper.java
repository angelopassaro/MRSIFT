package it.unisa.soa.mrsift;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.File;
import java.io.IOException;

public class ImageSequentMapper extends Mapper<Text, MatWritable, Text, MatWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        System.load(new File("libopencv_java341.so").getAbsolutePath());
    }

    @Override
    protected void map(Text key, MatWritable value, Context context) throws IOException, InterruptedException {
        context.write(key, value);
    }
}
