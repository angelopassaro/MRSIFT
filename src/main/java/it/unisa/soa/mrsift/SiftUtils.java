package it.unisa.soa.mrsift;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo
 */
public class SiftUtils {

  public static final String OBJ_IMG = "image";
  public static final String OBJ_KPS = "keypoints";
  public static final String OBJ_DSC = "descriptors";

  private static byte[] readStream(InputStream stream) throws IOException {
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

  public static Mat readInputStreamIntoMat(InputStream inputStream) throws IOException {
    byte[] temporaryImageInMemory = readStream(inputStream);
    Mat outputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory),
            Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    return outputImage;
  }
  
  public static String extractFormat(String file){
    int dotPos = file.lastIndexOf(".");
      if (dotPos > -1) 
        return file.substring(dotPos + 1);
      else
        return "jpg";
  }
  
  public static byte[] matToByte(Mat image){
    byte[] bytes = new byte[SiftUtils.totalSize(image)];
    image.get(0, 0, bytes);
    return bytes;
  }
  
  public static Mat byteToMat(byte[] bytes, int width, int height, int type){
    Mat image = new Mat(height, width, type);
    image.put(0, 0, bytes);
    return image;
  }
  
  public static int totalSize(Mat image){
    return Math.toIntExact(image.total()) * image.channels();
  }
}
