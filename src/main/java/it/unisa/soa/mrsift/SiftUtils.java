package it.unisa.soa.mrsift;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SiftUtils {

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

  protected static Mat readInputStreamIntoMat(InputStream inputStream) throws IOException {
    byte[] temporaryImageInMemory = readStream(inputStream);
    Mat outputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory),
            Imgcodecs.IMREAD_GRAYSCALE);
    return outputImage;
  }

  protected static Mat byteToMat(BytesWritable value) {
    byte[] bytes = value.getBytes();
    Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_GRAYSCALE);
    return mat;

  }

  protected static byte[] matToByte(Mat value) {
    int size = (int) value.total() * (int) value.elemSize();
    byte[] bytes = new byte[size];
    System.arraycopy(value.dump().getBytes(), 0, bytes, 0, size);  // possibile problema  http://answers.opencv.org/question/33596/convert-mat-to-byte-in-c/
    return bytes;
  }

  protected static MapWritable createMapWritable(Mat objectKeyPoints, Mat objectDescriptors, Mat mat) {
    MapWritable map = new MapWritable();
    byte[] keyPointsBytes = new byte[objectKeyPoints.rows() * (int) objectKeyPoints.elemSize()];
    objectKeyPoints.get(0, 0, keyPointsBytes);
    byte[] descriptorBytes = new byte[objectDescriptors.rows() * (int) objectDescriptors.elemSize()];
    byte[] imageBytes = new byte[(int) mat.total() * (int) mat.elemSize()];
    mat.get(0, 0, imageBytes);
    map.put(new Text("objectKeypoint"), new BytesWritable(keyPointsBytes));
    map.put(new Text("objectDescriptors"), new BytesWritable(descriptorBytes));
    map.put(new Text("objectImage"), new BytesWritable(imageBytes));
    return map;
  }
}
