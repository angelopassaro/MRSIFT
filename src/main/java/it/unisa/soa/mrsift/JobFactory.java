package it.unisa.soa.mrsift;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import static it.unisa.soa.mrsift.MRSIFT.OPENCV_LIB;


public class JobFactory {

    public static Job createaMainJob(String[] strings, Configuration conf) throws  Exception{

        Job job = Job.getInstance(conf, "MRSIFT");
        job.addFileToClassPath(new Path(MRSIFT.class.getResource(OPENCV_LIB).toURI()));
        job.setJarByClass(MRSIFT.class);
        job.setMapperClass(SiftMapper.class);
        job.setReducerClass(SiftReduce.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(MapWritable.class);
        job.setInputFormatClass(ImageInputFormat.class);
        job.setOutputFormatClass(ImageOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(MatWritable.class);
        ImageOutputFormat.setOutputPath(job, new Path(strings[1]));
        ImageInputFormat.addInputPath(job, new Path(strings[0]));
        return job;
    }

    public static Job createSequentJob(String[] strings, Configuration conf) throws Exception {
        Job job = Job.getInstance(conf, "MRSIFT");
        job.addFileToClassPath(new Path(MRSIFT.class.getResource(OPENCV_LIB).toURI()));
        job.setJarByClass(MRSIFT.class);
        job.setMapperClass(ImageSequentMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(MatWritable.class);
        job.setInputFormatClass(ImageInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(MatWritable.class);
        ImageOutputFormat.setOutputPath(job, new Path(strings[1]));
        ImageInputFormat.addInputPath(job, new Path(strings[0]));
        return job;

    }
}
