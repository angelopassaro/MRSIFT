package it.unisa.soa.mrsift;

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
public class SiftMapper extends Mapper<Text, MatWritable, Text, MapWritable> {
  
  private static SIFT sift;

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
    System.load(new File("libopencv_java341.so").getAbsolutePath());
    sift = SIFT.create();
  }

  @Override
  protected void map(Text key, MatWritable value, Context context) throws IOException, InterruptedException {
    MatOfKeyPoint objectKeyPoints = SiftManager.extractKeypoints(value.getImage(), sift);
    MatOfKeyPoint objectDescriptors = SiftManager.extractDescriptors(value.getImage(), objectKeyPoints, sift);
    MapWritable map = new MapWritable();
    map.put(new Text(SiftUtils.OBJ_IMG), value);
    map.put(new Text(SiftUtils.OBJ_KPS), new MatOfKeyPointWritable(objectKeyPoints));
    map.put(new Text(SiftUtils.OBJ_DSC), new MatOfKeyPointWritable(objectDescriptors));
    context.write(key, map);
  }

}
