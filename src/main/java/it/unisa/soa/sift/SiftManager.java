package it.unisa.soa.sift;

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
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
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
  public MatOfKeyPoint extractKeypoints(Mat image, Feature2D extractor) {
    MatOfKeyPoint imageKeyPoints = new MatOfKeyPoint();
    extractor.detect(image, imageKeyPoints);
    return imageKeyPoints;
  }

  @Override
  public MatOfKeyPoint extractDescriptors(Mat image, MatOfKeyPoint imageKeypoints, Feature2D extractor) {
    MatOfKeyPoint imageDescriptors = new MatOfKeyPoint();
    extractor.compute(image, imageKeypoints, imageDescriptors);
    return imageDescriptors;
  }

  @Override
  public MatOfDMatch calculateMatches(MatOfKeyPoint objectDescriptors, MatOfKeyPoint sceneDescriptors) {
    MatOfDMatch matches = new MatOfDMatch();
    BFMatcher matcher = BFMatcher.create(Core.NORM_L2, true);
    matcher.match(objectDescriptors, sceneDescriptors, matches);
    return matches;
  }

  @Override
  public Mat detectObject(Mat objectImage, Mat sceneImage, MatOfDMatch matches, MatOfKeyPoint objectKeyPoints, MatOfKeyPoint sceneKeyPoints) {
    Mat output = new Mat();
    Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, 
            matches, output, Scalar.all(-1), Scalar.all(-1), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
    List<KeyPoint> objkeys = objectKeyPoints.toList();
    List<KeyPoint> scnkeys = sceneKeyPoints.toList();
    List<DMatch> goodMatches = matches.toList();
    List<Point> oPoints = goodMatches.stream().map(m -> objkeys.get(m.queryIdx).pt).collect(Collectors.toList());
    List<Point> sPoints = goodMatches.stream().map(m -> scnkeys.get(m.trainIdx).pt).collect(Collectors.toList());
    MatOfPoint2f objectMatrix = new MatOfPoint2f();
    MatOfPoint2f sceneMatrix = new MatOfPoint2f();
    objectMatrix.fromList(oPoints);
    sceneMatrix.fromList(sPoints);
    Mat homography = Calib3d.findHomography(objectMatrix, sceneMatrix, Calib3d.RANSAC, 3);
    Mat objectCorners = new Mat(4, 1, CvType.CV_32FC2);
    Mat sceneCorners = new Mat(4, 1, CvType.CV_32FC2);
    objectCorners.put(0, 0, new double[]{0,0});
    objectCorners.put(1, 0, new double[]{objectImage.cols(),0});
    objectCorners.put(2, 0, new double[]{objectImage.cols(),objectImage.rows()});
    objectCorners.put(3, 0, new double[]{0,objectImage.rows()});
    Core.perspectiveTransform(objectCorners, sceneCorners, homography);
    int offset = objectImage.cols();
    Imgproc.line(output, 
            new Point(sceneCorners.get(0, 0)[0]+offset, sceneCorners.get(0, 0)[1]), 
            new Point(sceneCorners.get(1, 0)[0]+offset, sceneCorners.get(1, 0)[1]), RECT_SCALAR, 4);
    Imgproc.line(output, 
            new Point(sceneCorners.get(1, 0)[0]+offset, sceneCorners.get(1, 0)[1]), 
            new Point(sceneCorners.get(2, 0)[0]+offset, sceneCorners.get(2, 0)[1]), RECT_SCALAR, 4);
    Imgproc.line(output, 
            new Point(sceneCorners.get(2, 0)[0]+offset, sceneCorners.get(2, 0)[1]), 
            new Point(sceneCorners.get(3, 0)[0]+offset, sceneCorners.get(3, 0)[1]), RECT_SCALAR, 4);
    Imgproc.line(output, 
            new Point(sceneCorners.get(3, 0)[0]+offset, sceneCorners.get(3, 0)[1]), 
            new Point(sceneCorners.get(0, 0)[0]+offset, sceneCorners.get(0, 0)[1]), RECT_SCALAR, 4);
    return output;
  }

  
  
}
