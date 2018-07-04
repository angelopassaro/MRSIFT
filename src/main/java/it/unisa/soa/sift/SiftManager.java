package it.unisa.soa.sift;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SIFT;
/**
 *
 * @author didacus
 */
public class SiftManager implements SiftAlgorithms {

  private static SiftManager instance = null;
  private static final Scalar RECT_SCALAR = new Scalar(0, 255, 0);

  private SiftManager() {
    super();
  }

  public static SiftManager getInstance() {
    return instance = (instance == null) ? new SiftManager() : instance;
  }

  @Override
  public void extractKeypoints(SiftModel model) {
    model.getExtractor().detect(model.getImage(), model.getImageKeypoints());
  }

  @Override
  public void extractDescriptors(SiftModel model) {
    model.getExtractor().compute(model.getImage(), model.getImageKeypoints(), model.getImageDescriptors());
  }

  @Override
  public MatOfDMatch calculateMatches(SiftModel objectModel, SiftModel sceneModel) {
    BFMatcher matcher = BFMatcher.create(Core.NORM_L2, true);
    MatOfDMatch matchesFound = new MatOfDMatch();
    matcher.match(objectModel.getImageDescriptors(), sceneModel.getImageDescriptors(), matchesFound);
    double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
    List<DMatch> goodMatches = new ArrayList<>();
    for(DMatch match : matchesFound.toList()){
      double dist = match.distance;
      if(dist < min) { min = dist; }
      if(dist > max) { max = dist; }
    }
    double average = (min + max) / 2;
    for(DMatch match : matchesFound.toList()){
      if(match.distance <= average){
        goodMatches.add(match);
      }
    }
    matchesFound.fromList(goodMatches);
    return matchesFound;
  }

  @Override
  public Mat detectObject(SiftModel objectModel, SiftModel sceneModel) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  
}
