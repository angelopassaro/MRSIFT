package it.unisa.soa.mrsift;
import it.unisa.soa.sift.SiftManager;
import java.io.IOException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SIFT;

/**
 *
 * @author didacus
 */
public class SiftMapper extends Mapper<Text, BytesWritable, Text, MapWritable> {

  @Override
  protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
    byte[] bytes = value.getBytes();
    Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_GRAYSCALE);
    SIFT sift = SIFT.create();
    SiftManager manager = SiftManager.getInstance();
    MatOfKeyPoint objectKeyPoints = manager.extractKeypoints(mat, sift);
    MatOfKeyPoint objectDescriptors = manager.extractDescriptors(mat, objectKeyPoints, sift);
    MapWritable map = new MapWritable();
    byte[] keyPointsBytes = new byte[objectKeyPoints.rows() * (int)objectKeyPoints.elemSize()];
    objectKeyPoints.get(0, 0, keyPointsBytes);
    byte[] descriptorBytes = new byte[objectDescriptors.rows() * (int)objectDescriptors.elemSize()];
    byte[] imageBytes = new byte[(int)mat.total() * (int)mat.elemSize()];
    mat.get(0, 0, imageBytes);
    map.put(new Text("objectKeypoint"), new BytesWritable(keyPointsBytes));
    map.put(new Text("objectDescriptors"), new BytesWritable(descriptorBytes));
    map.put(new Text("objectImage"), new BytesWritable(imageBytes));
    context.write(key, map);
  }
 
}
