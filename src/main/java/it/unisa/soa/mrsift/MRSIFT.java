package it.unisa.soa.mrsift;

import java.io.File;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo This class is the main M/R Job configuration
 * Call with hadoop jar $pathjar -D mapred.reduce.tasks=$number $input $output
 */
public class MRSIFT extends Configured implements Tool {

    public static final String OPENCV_LIB = "/libs/libopencv_java341.so";

    public static void load_library() {
        URL url = MRSIFT.class.getResource(OPENCV_LIB);
        File opencv = new File(url.getFile());
        System.load(opencv.getAbsolutePath());
    }
    

    public static void main(String[] args) throws Exception {

        int res = ToolRunner.run(new Configuration(), new MRSIFT(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        Job job = Job.getInstance(conf, "MRSIFT");
        FileSystem fs = FileSystem.get(conf);
        Path path = Path.mergePaths(fs.getHomeDirectory(),new Path("/libopencv_java341.so"));
        job.addFileToClassPath(path);
        switch(strings[2]){
          case "--images":
            JobFactory.jobWithImages(strings, job);
            break;
          case "--combine":
            JobFactory.jobWithSequenceWriter(strings, job);
            break;
          default:
            JobFactory.jobWithSequenceReader(strings, job);
        }
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
