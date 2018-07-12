package it.unisa.soa.mrsift;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import javax.lang.model.SourceVersion;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
/**
 *
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo
 * This class is the main M/R Job configuration
 */
public class MRSIFT extends Configured implements Tool {

  private static final String OPENCV_LIB = "/libs/libopencv_java341.so";

  public static void load_library() {
    URL url = MRSIFT.class.getResource(OPENCV_LIB);
    File opencv = new File(url.getFile());
    System.load(opencv.getAbsolutePath());
  }

  public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, 
URISyntaxException, Exception {
      int res = ToolRunner.run(new Configuration(), new MRSIFT(), args);
      System.exit(res);
  }

    @Override
    public int run(String[] strings) throws Exception {
    Configuration conf = this.getConf();
    
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
    
    ImageOutputFormat.setOutputPath(job, new Path(strings[1]));
    ImageInputFormat.addInputPath(job, new Path(strings[0]));
    
    return job.waitForCompletion(true) ? 0 : 1;       
    }

    public Set<SourceVersion> getSourceVersions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

