package it.unisa.soa.sift;


import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.Feature2D;
import org.opencv.imgcodecs.Imgcodecs;


/**
 *
 * @author didacus
 */
public class SiftModel {
  
  private final Feature2D extractor;
  private final Mat objectImage, sceneImage;
  private final MatOfKeyPoint objectKeypoints, sceneKeyPoints;
  private final MatOfKeyPoint objectDescriptors, sceneDescriptors;
  private final MatOfDMatch matches;

  public SiftModel(String objectImagePath, String sceneImagePath, Feature2D extractor) {
    this.extractor = extractor;
    this.objectImage = Imgcodecs.imread(objectImagePath, SiftConfig.COLORED_IMAGE);
    this.sceneImage = Imgcodecs.imread(sceneImagePath, SiftConfig.COLORED_IMAGE);
    this.objectKeypoints = new MatOfKeyPoint();
    this.sceneKeyPoints = new MatOfKeyPoint();
    this.objectDescriptors = new MatOfKeyPoint();
    this.sceneDescriptors = new MatOfKeyPoint();
    this.matches = new MatOfDMatch();
  }  

  public Feature2D getExtractor() {
    return extractor;
  }

  public Mat getObjectImage() {
    return objectImage;
  }

  public Mat getSceneImage() {
    return sceneImage;
  }

  public MatOfKeyPoint getObjectKeypoints() {
    return objectKeypoints;
  }

  public MatOfKeyPoint getSceneKeyPoints() {
    return sceneKeyPoints;
  }

  public MatOfKeyPoint getObjectDescriptors() {
    return objectDescriptors;
  }

  public MatOfKeyPoint getSceneDescriptors() {
    return sceneDescriptors;
  }

  public MatOfDMatch getMatches() {
    return matches;
  }

  @Override
  public String toString() {
    String InitialPart = "SiftModel:\nExtractor:SIFT\nMatches:BF\n";
    String firstPart = "FirstImage:[Keypoints:%d, Descriptors:%d]\n";
    String secondPart = "SecondImage:[Keypoints:%d, Descriptors:%d]\n";
    String finalPart = "Matches:%d\n";
    String total = InitialPart.concat(firstPart).concat(secondPart).concat(finalPart);
    return String.format(total, this.objectKeypoints.total(), this.objectDescriptors.total(), this.sceneKeyPoints.total(), this.sceneDescriptors.total(), this.matches.toList().size());
  }
  
  
  
}
