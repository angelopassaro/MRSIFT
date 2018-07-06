package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SIFT;

/**
 * @author didacus
 */
public class SiftReduce extends Reducer<Text, MapWritable, Text, BytesWritable> {

  @Override
  protected void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
    for (MapWritable map : values) {
      byte[] rcvimg = ((BytesWritable) map.get(new Text("objectImage"))).getBytes();
      byte[] opoints = ((BytesWritable) map.get(new Text("objectKeypoint"))).getBytes();
      byte[] odesc = ((BytesWritable) map.get(new Text("objectDescriptors"))).getBytes();
      MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
      objectDescriptors.put(0, 0, odesc);
      MatOfKeyPoint objectKeypoints = new MatOfKeyPoint();
      objectKeypoints.put(0, 0, opoints);
      Mat mat = Imgcodecs.imdecode(new MatOfByte(rcvimg), Imgcodecs.IMREAD_GRAYSCALE);
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
          Mat outputImg = manager.detectObject(mat, sceneImg, matches, objectKeypoints, sceneKeyPoints);
          context.write(key, new BytesWritable(SiftUtils.matToByte(outputImg)));
        }
      }
    }
  }
}
