package it.unisa.soa.mrsift;
import it.unisa.soa.sift.SiftManager;
import java.io.IOException;
import java.io.File;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.xfeatures2d.SIFT;

/**
 *
 * @author didacus
 */
public class SiftMapper extends Mapper<Text, BytesWritable, Text, MapWritable> {

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
   System.load(new File("libopencv_java341.so").getAbsolutePath());
   }

  @Override
  protected void map(Text key,BytesWritable value, Context context) throws IOException, InterruptedException {
    Mat mat = SiftUtils.byteToMat(value);
    SIFT sift = SIFT.create();
    SiftManager manager = SiftManager.getInstance();
    MatOfKeyPoint objectKeyPoints = manager.extractKeypoints(mat, sift);
    MatOfKeyPoint objectDescriptors = manager.extractDescriptors(mat, objectKeyPoints, sift);
    context.write(key, SiftUtils.createMapWritable(objectKeyPoints, objectDescriptors, mat));
  }

}
