package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;
import java.io.IOException;
import java.io.File;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.xfeatures2d.SIFT;

/**
 *
 * @author didacus
 */
public class SiftMapper extends Mapper<Text, MatWritable, Text, MapWritable> {

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
    System.load(new File("libopencv_java341.so").getAbsolutePath());
  }

  @Override
  protected void map(Text key, MatWritable value, Context context) throws IOException, InterruptedException {
    SIFT sift = SIFT.create();
    SiftManager manager = SiftManager.getInstance();
    MatOfKeyPoint objectKeyPoints = manager.extractKeypoints(value.getImage(), sift);
    MatOfKeyPoint objectDescriptors = manager.extractDescriptors(value.getImage(), objectKeyPoints, sift);
    MapWritable map = new MapWritable();
    map.put(new Text(SiftUtils.OBJ_IMG), value);
    map.put(new Text(SiftUtils.OBJ_KPS), new MatWritable(objectKeyPoints));
    map.put(new Text(SiftUtils.OBJ_DSC), new MatWritable(objectDescriptors));
    context.write(key, map);
  }

}
