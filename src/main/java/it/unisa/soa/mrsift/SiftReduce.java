package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.xfeatures2d.SIFT;

/**
 * @author didacus
 */
public class SiftReduce extends Reducer<NullWritable, MapWritable, NullWritable, MatImageWritable> {

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
    System.load(new File("libopencv_java341.so").getAbsolutePath());
  }

  @Override
  protected void reduce(NullWritable key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
    for (MapWritable map : values) {
      MatImageWritable rcvimg = ((MatImageWritable) map.get(new Text("objectImage")));
      MatImageWritable opoints = ((MatImageWritable) map.get(new Text("objectKeypoint")));
      MatImageWritable odesc = ((MatImageWritable) map.get(new Text("objectDescriptors")));
      MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
      objectDescriptors.put(0, 0, odesc.getImage().dump().getBytes());
      MatOfKeyPoint objectKeypoints = new MatOfKeyPoint();
      objectKeypoints.put(0, 0, opoints.getImage().dump().getBytes());
      SiftManager manager = SiftManager.getInstance();
      FileSystem fs = FileSystem.get(context.getConfiguration());
      Path scenePath = Path.mergePaths(fs.getHomeDirectory(), new Path("/scenes"));
      RemoteIterator iterator = fs.listLocatedStatus(scenePath);
      SIFT sift = SIFT.create();
      while (iterator.hasNext()) {
        LocatedFileStatus lfs = (LocatedFileStatus) iterator.next();
        if (lfs.isFile()) {
          InputStream input = fs.open(lfs.getPath());
          Mat sceneImg = SiftUtils.readInputStreamIntoMat(input);
          MatOfKeyPoint sceneKeyPoints = manager.extractKeypoints(sceneImg, sift);
          MatOfKeyPoint sceneDescriptors = manager.extractDescriptors(sceneImg, sceneKeyPoints, sift);
          MatOfDMatch matches = manager.calculateMatches(objectDescriptors, sceneDescriptors);
          Mat outputImg = manager.detectObject(rcvimg.getImage(), sceneImg, matches, objectKeypoints, sceneKeyPoints);
          context.write(key, new MatImageWritable(outputImg));
        }
      }
    }
  }
}
