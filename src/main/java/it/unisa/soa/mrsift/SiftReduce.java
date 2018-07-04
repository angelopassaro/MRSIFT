package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;
import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author didacus
 */
public class SiftReduce extends Reducer<Text, MapWritable, NullWritable, NullWritable> {

  @Override
  protected void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
    for(MapWritable map : values){
      byte[] rcvimg = ((BytesWritable)map.get(new Text("objectImage"))).getBytes();
      byte[] opoints = ((BytesWritable)map.get(new Text("objectKeypoint"))).getBytes();
      byte[] odesc = ((BytesWritable)map.get(new Text("objectDescriptors"))).getBytes();
      MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
      objectDescriptors.put(0, 0, odesc);
      MatOfKeyPoint objectKeypoints = new MatOfKeyPoint();
      objectKeypoints.put(0, 0, opoints);
      Mat mat = Imgcodecs.imdecode(new MatOfByte(rcvimg), Imgcodecs.IMREAD_GRAYSCALE);
      SiftManager manager = SiftManager.getInstance();
      FileSystem fs = FileSystem.get(URI.create(""), context.getConfiguration());
      fs
    }
  }
  
  
  
}
