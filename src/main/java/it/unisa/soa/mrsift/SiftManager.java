package it.unisa.soa.mrsift;

import java.util.ArrayList;
import java.util.List;
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
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author didacus
 */
public class SiftManager {

  private static final Scalar RECT_SCALAR = new Scalar(0, 255, 0);
  private static final float RATIO_THRESHOLD = 0.75f;
  private static final double RANSAC_THRESHOLD = 3.0;

  private SiftManager() {
    super();
  }

  public static MatOfKeyPoint extractKeypoints(Mat image, Feature2D extractor) {
    MatOfKeyPoint imageKeypoint = new MatOfKeyPoint();
    extractor.detect(image, imageKeypoint);
    return imageKeypoint;
  }

  public static MatOfKeyPoint extractDescriptors(Mat image, MatOfKeyPoint imageKeypoints, Feature2D extractor) {
    MatOfKeyPoint imageDescriptors = new MatOfKeyPoint();
    extractor.compute(image, imageKeypoints, imageDescriptors);
    return imageDescriptors;
  }

  public static MatOfDMatch calculateMatches(MatOfKeyPoint objectDescriptor, MatOfKeyPoint sceneDescriptor, DescriptorMatcher matcher) {
    List<MatOfDMatch> knnMatches = new ArrayList<>();
    matcher.knnMatch(objectDescriptor, sceneDescriptor, knnMatches, 2);
    List<DMatch> listOfGoodMatches = new ArrayList<>();
    for (int i = 0; i < knnMatches.size(); i++) {
      if (knnMatches.get(i).rows() > 1) {
        DMatch[] matches = knnMatches.get(i).toArray();
        if (matches[0].distance < RATIO_THRESHOLD * matches[1].distance) {
          listOfGoodMatches.add(matches[0]);
        }
      }
    }
    MatOfDMatch goodMatches = new MatOfDMatch();
    goodMatches.fromList(listOfGoodMatches);
    return goodMatches;
  }

  public static Mat localizeObject(List<KeyPoint> objectKeypoints, List<KeyPoint> sceneKeypoints, MatOfDMatch goodMatches) {
    List<Point> obj = new ArrayList<>();
    List<Point> scene = new ArrayList<>();
    List<DMatch> listOfGoodMatches = goodMatches.toList();
    for (int i = 0; i < listOfGoodMatches.size(); i++) {
      obj.add(objectKeypoints.get(listOfGoodMatches.get(i).queryIdx).pt);
      scene.add(sceneKeypoints.get(listOfGoodMatches.get(i).trainIdx).pt);
    }
    MatOfPoint2f objMat = new MatOfPoint2f(), sceneMat = new MatOfPoint2f();
    objMat.fromList(obj);
    sceneMat.fromList(scene);
    Mat H = Calib3d.findHomography(objMat, sceneMat, Calib3d.RANSAC, RANSAC_THRESHOLD);
    return H;
  }

  public static Mat drawImage(Mat objectImage, Mat sceneImage, MatOfKeyPoint objectKeypoints, MatOfKeyPoint sceneKeypoints, MatOfDMatch matches, Mat homography) {
    Mat outputImage = new Mat();
    Features2d.drawMatches(objectImage, objectKeypoints, sceneImage, sceneKeypoints, matches, outputImage, Scalar.all(-1),
            Scalar.all(-1), new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
    Mat objCorners = new Mat(4, 1, CvType.CV_32FC2), sceneCorners = new Mat();
    float[] objCornersData = new float[(int) (objCorners.total() * objCorners.channels())];
    objCorners.get(0, 0, objCornersData);
    objCornersData[0] = 0;
    objCornersData[1] = 0;
    objCornersData[2] = objectImage.cols();
    objCornersData[3] = 0;
    objCornersData[4] = objectImage.cols();
    objCornersData[5] = objectImage.rows();
    objCornersData[6] = 0;
    objCornersData[7] = objectImage.rows();
    objCorners.put(0, 0, objCornersData);
    Core.perspectiveTransform(objCorners, sceneCorners, homography);
    float[] sceneCornersData = new float[(int) (sceneCorners.total() * sceneCorners.channels())];
    sceneCorners.get(0, 0, sceneCornersData);
    Imgproc.line(outputImage, new Point(sceneCornersData[0] + objectImage.cols(), sceneCornersData[1]),
            new Point(sceneCornersData[2] + objectImage.cols(), sceneCornersData[3]), RECT_SCALAR, 4);
    Imgproc.line(outputImage, new Point(sceneCornersData[2] + objectImage.cols(), sceneCornersData[3]),
            new Point(sceneCornersData[4] + objectImage.cols(), sceneCornersData[5]), RECT_SCALAR, 4);
    Imgproc.line(outputImage, new Point(sceneCornersData[4] + objectImage.cols(), sceneCornersData[5]),
            new Point(sceneCornersData[6] + objectImage.cols(), sceneCornersData[7]), RECT_SCALAR, 4);
    Imgproc.line(outputImage, new Point(sceneCornersData[6] + objectImage.cols(), sceneCornersData[7]),
            new Point(sceneCornersData[0] + objectImage.cols(), sceneCornersData[1]), RECT_SCALAR, 4);
    return outputImage;
  }
  
  /**
   * Check the compare absolute values at (0,0) and (0,1) with (1,1) and (1,0)respectively. 
   * If the two differences are close to 0, the homography matrix is  good. 
   * (This just takes of rotation and not translation)
   * @param homography the homography to estimate
   * @return true if the homography is good
   */
  public static boolean checkHomography(Mat homography){
    if(homography == null)
      return false;
    double firstCheck = Math.abs(Math.abs(homography.get(0, 0)[0]) - Math.abs(homography.get(1, 1)[0]));
    double secondCheck = Math.abs(Math.abs(homography.get(0, 1)[0]) - Math.abs(homography.get(1, 0)[0]));
    return firstCheck <= 0.1 && secondCheck <= 0.1;
  }

}
