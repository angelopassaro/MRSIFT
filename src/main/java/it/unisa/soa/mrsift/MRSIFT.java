package it.unisa.soa.mrsift;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo
 * This class is the main M/R Job configuration
 */
public class MRSIFT {

  private static final String OPENCV_LIB = "/libs/libopencv_java341.so";

  public static void load_library() {
    URL url = MRSIFT.class.getResource(OPENCV_LIB);
    File opencv = new File(url.getFile());
    System.load(opencv.getAbsolutePath());
  }

  public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, URISyntaxException {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "MRSIFT");
    job.addFileToClassPath(new Path(MRSIFT.class.getResource(OPENCV_LIB).toURI()));
    job.setJarByClass(MRSIFT.class);
    job.setMapperClass(SiftMapper.class);
    job.setCombinerClass(SiftReduce.class);
    job.setReducerClass(SiftReduce.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(MapWritable.class);
    job.setInputFormatClass(ImageInputFormat.class);
    job.setOutputFormatClass(ImageOutputFormat.class);
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    FileInputFormat.addInputPath(job, new Path(args[0]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
