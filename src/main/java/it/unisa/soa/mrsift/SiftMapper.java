package it.unisa.soa.mrsift;
import it.unisa.soa.sift.SiftManager;
import java.io.IOException;
import java.io.File;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.xfeatures2d.SIFT;

/**
 *
 * @author didacus
 */
public class SiftMapper extends Mapper<LongWritable, MatImageWritable, LongWritable, MapWritable> {

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
   System.load(new File("libopencv_java341.so").getAbsolutePath());
   }

  @Override
  protected void map(LongWritable key, MatImageWritable value, Context context) throws IOException, InterruptedException {
    SIFT sift = SIFT.create();
    SiftManager manager = SiftManager.getInstance();
    MatOfKeyPoint objectKeyPoints = manager.extractKeypoints(value.getImage(), sift);
    MatOfKeyPoint objectDescriptors = manager.extractDescriptors(value.getImage(), objectKeyPoints, sift);
    MapWritable map = new MapWritable();
    map.put(new Text("objectImage"), value);
    map.put(new Text("keyPoint"),new MatImageWritable(objectKeyPoints));
    map.put(new Text("descriptorsPoint"),new MatImageWritable(objectDescriptors));
    context.write(key, map);
  }

}
