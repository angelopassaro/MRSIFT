package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SIFT;

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
//      FileStatus[] fileStatus = fs.listStatus(new Path(""));

      Path pathName = new Path("");
      RemoteIterator iterator = fs.listLocatedStatus(pathName);
      SIFT sift = SIFT.create();
      
      while (iterator.hasNext())
              {
                  LocatedFileStatus lfs = (LocatedFileStatus) iterator.next();
                  if (lfs.isFile()) {
                      InputStream input = fs.open(lfs.getPath());
                      Mat sceneImg = readInputStreamIntoMat(input);
                      MatOfKeyPoint sceneKeyPoints = manager.extractKeypoints(sceneImg, sift);
                      MatOfKeyPoint sceneDescriptors = manager.extractDescriptors(sceneImg,
                              sceneKeyPoints, sift);
                      MatOfDMatch matches = manager.calculateMatches(objectDescriptors, sceneDescriptors);
                      Mat outputImg = manager.detectObject(mat, sceneImg, matches,
                              objectKeypoints, sceneKeyPoints);

//                      CONVERTI IN BYTE STRONZO fs.create(lfs.getPath()).write(odesc);
                      
                  } 
              }
    
    
    }
  }
  
  private static byte[] readStream(InputStream stream) throws IOException {
    // Copy content of the image to byte-array
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[16384];

    while ((nRead = stream.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
    }

    buffer.flush();
    byte[] temporaryImageInMemory = buffer.toByteArray();
    buffer.close();
    stream.close();
    return temporaryImageInMemory;
}
  
  private static Mat readInputStreamIntoMat(InputStream inputStream) throws IOException {
    // Read into byte-array
    byte[] temporaryImageInMemory = readStream(inputStream);

    // Decode into mat. Use any IMREAD_ option that describes your image appropriately
    Mat outputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory),
            Imgcodecs.IMREAD_GRAYSCALE);

    return outputImage;
}
  
}
