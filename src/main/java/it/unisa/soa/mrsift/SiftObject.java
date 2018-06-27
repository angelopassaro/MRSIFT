package it.unisa.soa.mrsift;
import java.util.stream.Collectors;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SIFT;


/**
 *
 * @author didacus
 */
public class SiftObject {
  
  private final SIFT sift;
  private final Mat objetImage, sceneImage;
  private final MatOfKeyPoint objectKeypoints, sceneKeyPoints;
  private final MatOfKeyPoint objectDescriptors, sceneDescriptors;
  private final MatOfDMatch matches;

  public SiftObject(String objectPath, String scenePath){
    this.objetImage = Imgcodecs.imread(objectPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
    this.sceneImage = Imgcodecs.imread(scenePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
    this.objectKeypoints = new MatOfKeyPoint();
    this.sceneKeyPoints = new MatOfKeyPoint();
    this.objectDescriptors = new MatOfKeyPoint();
    this.sceneDescriptors = new MatOfKeyPoint();
    this.matches = new MatOfDMatch();
    this.sift = SIFT.create();
  }

  public SIFT getSift() {
    return sift;
  }

  public Mat getObjetImage() {
    return objetImage;
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
  
  public void extractKeyPoints(){
    this.sift.detect(this.objetImage, this.objectKeypoints);
    this.sift.detect(this.sceneImage, this.sceneKeyPoints);
  }
  
  public void extractFeatures(){
    this.sift.compute(this.objetImage, this.objectKeypoints, this.objectDescriptors);
    this.sift.compute(this.sceneImage, this.sceneKeyPoints, this.sceneDescriptors);
  }
  
  
  public void calculateMatches(int descriptorMatcherType){
    DescriptorMatcher matcher = DescriptorMatcher.create(descriptorMatcherType);
    matcher.match(this.objectDescriptors, this.sceneDescriptors, this.matches);
    double min = this.matches.toList().stream().mapToDouble(e -> e.distance).min().getAsDouble();
    double threshold = 0.7;
    this.matches.fromList(this.matches.toList().stream().filter(e -> e.distance <= min * threshold).collect(Collectors.toList()));
  }
  
}
