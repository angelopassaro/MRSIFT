/*
 * Generic function to apply sisft algorithm
 */
package it.unisa.soa.mrsift;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SIFT;

/**
 *
 * @author didacus
 */
public class SiftWrapper {
  
  private static final Logger LOGGER = Logger.getLogger(SiftWrapper.class.getSimpleName());
  
  public static Mat createImage(String imagePath){
    Mat image = Imgcodecs.imread(imagePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
    return image;
  }
  
  public static MatOfKeyPoint extractFeatures(Mat image){
    MatOfKeyPoint imageKeyPoints = new MatOfKeyPoint();
    MatOfKeyPoint imageDescriptors = new MatOfKeyPoint();
    SIFT sift = SIFT.create();
    LOGGER.info("Detect Key points");
    sift.detect(image, imageKeyPoints);
    LOGGER.log(Level.INFO, "Keypoints found:{0}", imageKeyPoints.total());
    LOGGER.info("Detect features image:");
    sift.compute(image, imageKeyPoints, imageDescriptors);
    LOGGER.log(Level.INFO, "Number Of Descriptros:{0}", imageDescriptors.total());
    return imageDescriptors;
  }
  
  public static MatOfDMatch calculateMatches(MatOfKeyPoint templateImage, MatOfKeyPoint inputImage){
    MatOfDMatch matches = new MatOfDMatch();
    DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
    LOGGER.info("Start Find Matches");
    matcher.match(templateImage,inputImage, matches);
    if(!matches.empty()){
      LOGGER.info("Match trovato"+matches.width());
    }
    DMatch min = matches.toList().stream().min(Comparator.comparingDouble(e -> e.distance)).get();
    double thersold = 2.0;
    List<DMatch> good = new ArrayList<>();
    matches.toList().forEach(e -> {
    if(e.distance <= min.distance * thersold){
      good.add(e);
    }
    });
    MatOfDMatch goodMatch = new MatOfDMatch();
    goodMatch.fromList(good);
   return goodMatch;
  }
  
  public static void createOutputImage(Mat firstImage, Mat secondImage, MatOfKeyPoint firstPoints, MatOfKeyPoint secondPoints, MatOfDMatch matches){
    Mat output = new Mat();
    Features2d.drawKeypoints(firstImage, firstPoints, output);
    Imgcodecs.imwrite("/home/didacus/result.jpg", output);
  }
  
}
