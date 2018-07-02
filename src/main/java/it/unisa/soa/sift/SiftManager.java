package it.unisa.soa.sift;

import java.util.ArrayList;
import java.util.Collections;
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
import org.opencv.features2d.FlannBasedMatcher;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.xfeatures2d.SIFT;
/**
 *
 * @author didacus
 */
public class SiftManager implements SiftAlgorithms {

  private static SiftManager instance = null;

  private SiftManager() {
    super();
  }

  public static SiftManager getInstance() {
    return instance = (instance == null) ? new SiftManager() : instance;
  }

  @Override
  public SiftModel findFeatures(String objectImagePath, String sceneImagePath) {
    SiftModel model = new SiftModel(objectImagePath, sceneImagePath, SIFT.create());
    model.getExtractor().detect(model.getObjectImage(), model.getObjectKeypoints());
    model.getExtractor().detect(model.getSceneImage(), model.getSceneKeyPoints());
    model.getExtractor().compute(model.getObjectImage(), model.getObjectKeypoints(), model.getObjectDescriptors());
    model.getExtractor().compute(model.getSceneImage(), model.getSceneKeyPoints(), model.getSceneDescriptors());
    BFMatcher flann = BFMatcher.create(Core.NORM_L2, true);
    flann.match(model.getObjectDescriptors(), model.getSceneDescriptors(), model.getMatches());
    List<DMatch> goodMatches = model.getMatches().toList().stream().sorted(Comparator.comparing(m -> m.distance)).collect(Collectors.toList());
    model.getMatches().fromList(goodMatches.subList(0, goodMatches.size()/2));
    return model;
  }

  @Override
  public List<SiftModel> findFeatures(String objectImagePath, List<String> scenesImagePath) {
    return scenesImagePath.stream().map(path -> findFeatures(objectImagePath, path)).collect(Collectors.toList());
  }

  @Override
  public Mat detectObject(SiftModel model, List<KeyPoint> objectKeyPoints, List<KeyPoint> sceneKeyPoints) {
    Mat outputImage = new Mat();
    Features2d.drawMatches(model.getObjectImage(), model.getObjectKeypoints(), model.getSceneImage(), 
            model.getSceneKeyPoints(), model.getMatches(), outputImage, Scalar.all(-1), Scalar.all(-1), 
            new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
    /*************************homo***********************************/
    MatOfPoint2f objectMatrix = new MatOfPoint2f();
    MatOfPoint2f sceneMatrix = new MatOfPoint2f();
    List<Point> objectPoints = new ArrayList<>();
    List<Point> scenePoints = new ArrayList<>();
    List<DMatch> goodMatches = model.getMatches().toList();
    for(int i = 0; i < goodMatches.size(); i++){
      objectPoints.add(objectKeyPoints.get(goodMatches.get(i).queryIdx).pt);
      scenePoints.add(sceneKeyPoints.get(goodMatches.get(i).trainIdx).pt);
    }
    objectMatrix.fromList(objectPoints);
    sceneMatrix.fromList(scenePoints);
    Mat homography = Calib3d.findHomography(objectMatrix, sceneMatrix, Calib3d.RANSAC, 3);
    if(homography.empty()){
      System.err.println("Vuota");
    }
    Mat objectCorners = new Mat(4, 1, CvType.CV_32FC2);
    objectCorners.put(0, 0, new double[]{0,0});
    objectCorners.put(1, 0, new double[]{model.getObjectImage().cols(),0});
    objectCorners.put(2, 0, new double[]{model.getObjectImage().cols(),model.getObjectImage().rows()});
    objectCorners.put(3, 0, new double[]{0,model.getObjectImage().rows()});
    int offset = model.getObjectImage().cols();
    Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);
    Core.perspectiveTransform(objectCorners, sceneCorners, homography);
    Imgproc.line(outputImage, 
            new Point(sceneCorners.get(0, 0)[0]+offset, sceneCorners.get(0, 0)[1]), 
            new Point(sceneCorners.get(1, 0)[0]+offset, sceneCorners.get(1, 0)[1]), SiftConfig.RECT_SCALAR, 4);
    Imgproc.line(outputImage, 
            new Point(sceneCorners.get(1, 0)[0]+offset, sceneCorners.get(1, 0)[1]), 
            new Point(sceneCorners.get(2, 0)[0]+offset, sceneCorners.get(2, 0)[1]), SiftConfig.RECT_SCALAR, 4);
    Imgproc.line(outputImage, 
            new Point(sceneCorners.get(2, 0)[0]+offset, sceneCorners.get(2, 0)[1]), 
            new Point(sceneCorners.get(3, 0)[0]+offset, sceneCorners.get(3, 0)[1]), SiftConfig.RECT_SCALAR, 4);
    Imgproc.line(outputImage, 
            new Point(sceneCorners.get(3, 0)[0]+offset, sceneCorners.get(3, 0)[1]), 
            new Point(sceneCorners.get(0, 0)[0]+offset, sceneCorners.get(0, 0)[1]), SiftConfig.RECT_SCALAR, 4);
    return outputImage;
  }

  
}
