package it.unisa.soa.mrsift;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.xfeatures2d.SIFT;

/**
 * @author didacus
 */
public class SiftReduce extends Reducer<Text, MapWritable, Text, MatWritable> {

  private static SIFT sift;
  private static DescriptorMatcher descriptor;

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
    System.load(new File("libopencv_java341.so").getAbsolutePath());
    sift = SIFT.create();
    descriptor = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
  }

  @Override
  protected void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
    for (MapWritable map : values) {
      MatWritable receivedImage = ((MatWritable) map.get(new Text(SiftUtils.OBJ_IMG)));
      MatOfKeyPointWritable receivedKeyPoints = ((MatOfKeyPointWritable) map.get(new Text(SiftUtils.OBJ_KPS)));
      MatOfKeyPointWritable receivedObjectDescriptors = ((MatOfKeyPointWritable) map.get(new Text(SiftUtils.OBJ_DSC)));
      MatOfKeyPoint objectDescriptors = receivedObjectDescriptors.getMatOfKeyPoint();
      MatOfKeyPoint objectKeypoints = receivedKeyPoints.getMatOfKeyPoint();
      FileSystem fs = FileSystem.get(context.getConfiguration());
      Path scenePath = Path.mergePaths(fs.getHomeDirectory(), new Path("/scenes"));
      RemoteIterator iterator = fs.listLocatedStatus(scenePath);
      while (iterator.hasNext()) {
        LocatedFileStatus lfs = (LocatedFileStatus) iterator.next();
        String format = SiftUtils.extractFormat(lfs.getPath().getName());
        String name = lfs.getPath().getName().substring(0, lfs.getPath().getName().lastIndexOf("."));
        if (lfs.isFile()) {
          InputStream input = fs.open(lfs.getPath());
          Mat sceneImg = SiftUtils.readInputStreamIntoMat(input);
          MatOfKeyPoint sceneKeyPoints = SiftManager.extractKeypoints(sceneImg, sift);
          MatOfKeyPoint sceneDescriptors = SiftManager.extractDescriptors(sceneImg, sceneKeyPoints, sift);
          MatOfDMatch matches = SiftManager.calculateMatches(objectDescriptors, sceneDescriptors, descriptor);
          Mat homography = SiftManager.localizeObject(objectKeypoints.toList(), sceneKeyPoints.toList(), matches);
          if (SiftManager.checkHomography(homography)) {
            Mat outputImg = SiftManager.drawImage(receivedImage.getImage(), sceneImg, objectKeypoints, sceneKeyPoints, matches, homography);
            context.write(key, new MatWritable(outputImg, name, format));
          }
        }
      }
    }
  }
}
