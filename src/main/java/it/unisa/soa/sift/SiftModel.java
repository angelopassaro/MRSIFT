package it.unisa.soa.sift;


import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Feature2D;
import org.opencv.imgcodecs.Imgcodecs;


/**
 *
 * @author didacus
 */
public class SiftModel {
  
  private final Mat image;
  private final MatOfKeyPoint imageKeypoints;
  private final MatOfKeyPoint imageDescriptors;
  private final Feature2D extractor;

  public SiftModel(byte[] dataStream, Feature2D extractor) {
    this.extractor = extractor;
    this.image = Imgcodecs.imdecode(new MatOfByte(dataStream), Imgcodecs.IMREAD_GRAYSCALE);
    this.imageKeypoints = new MatOfKeyPoint();
    this.imageDescriptors = new MatOfKeyPoint();
  }

  public Mat getImage() {
    return image;
  }

  public MatOfKeyPoint getImageKeypoints() {
    return imageKeypoints;
  }

  public MatOfKeyPoint getImageDescriptors() {
    return imageDescriptors;
  }

  public Feature2D getExtractor() {
    return extractor;
  }
}
