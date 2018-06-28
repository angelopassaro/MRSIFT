package it.unisa.soa.sift;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

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
    SiftModel siftObject = new SiftModel(objectImagePath, sceneImagePath);
    this.extractFeatures(siftObject);
    this.calculateMatches(siftObject);
    return siftObject;
  }

  @Override
  public List<SiftModel> findFeatures(String objectImagePath, List<String> scenesImagePath) {
    return scenesImagePath.stream().map(path -> findFeatures(objectImagePath, path)).collect(Collectors.toList());
  }

  @Override
  public Mat detectObject(SiftModel siftObject, List<KeyPoint> objectPoints, List<KeyPoint> scenePoints) {
    Mat outputImage = new Mat(siftObject.getSceneImage().rows(), siftObject.getSceneImage().cols() * 2, siftObject.getSceneImage().type());
    Features2d.drawMatches(siftObject.getObjetImage(), siftObject.getObjectKeypoints(), siftObject.getSceneImage(),
            siftObject.getSceneKeyPoints(), siftObject.getMatches(), outputImage, Scalar.all(-1), Scalar.all(-1), 
            new MatOfByte(), Features2d.NOT_DRAW_SINGLE_POINTS);
    List<DMatch> good = siftObject.getMatches().toList();
    List<Point> oPointsList = good.stream().map(el -> objectPoints.get(el.queryIdx).pt).collect(Collectors.toList());
    List<Point> sPointsList = good.stream().map(el -> scenePoints.get(el.trainIdx).pt).collect(Collectors.toList());
    MatOfPoint2f objpoint2f = new MatOfPoint2f();
    MatOfPoint2f scnpoint2f = new MatOfPoint2f();
    objpoint2f.fromList(oPointsList);
    scnpoint2f.fromList(sPointsList);
    //Mat homography = Calib3d.findHomography(objpoint2f, scnpoint2f, Calib3d.RANSAC, 3);
    Mat homography = Calib3d.findHomography(objpoint2f, scnpoint2f);
    Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
    Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);
    obj_corners.put(0, 0, new double[]{0, 0});
    obj_corners.put(1, 0, new double[]{siftObject.getObjetImage().cols(), 0});
    obj_corners.put(2, 0, new double[]{siftObject.getObjetImage().cols(), siftObject.getObjetImage().rows()});
    obj_corners.put(3, 0, new double[]{0, siftObject.getObjetImage().rows()});
    System.out.println("Transforming object corners to scene corners...");
    Core.perspectiveTransform(obj_corners, scene_corners, homography);
    int offset = siftObject.getObjetImage().cols();
    Imgproc.line(outputImage, new Point(scene_corners.get(0,0)[0] + offset, scene_corners.get(0,0)[1]), 
            new Point(scene_corners.get(1,0)[0]+offset,scene_corners.get(1,0)[1]), SiftConfig.RECT_SCALAR, 4);
    Imgproc.line(outputImage,new Point(scene_corners.get(1,0)[0]+offset,scene_corners.get(1,0)[1]),
            new Point(scene_corners.get(2,0)[0]+offset, scene_corners.get(2,0)[1]), SiftConfig.RECT_SCALAR, 4);
    Imgproc.line(outputImage, new Point(scene_corners.get(2,0)[0]+offset, scene_corners.get(2,0)[1]), 
            new Point(scene_corners.get(3,0)[0]+offset, scene_corners.get(3,0)[1]), SiftConfig.RECT_SCALAR, 4);
    Imgproc.line(outputImage, new Point(scene_corners.get(3,0)[0]+offset, scene_corners.get(3,0)[1]), 
            new Point(scene_corners.get(0,0)[0]+offset, scene_corners.get(0,0)[1]), SiftConfig.RECT_SCALAR, 4);
    return outputImage;
  }

  private void extractFeatures(SiftModel siftObject) {
    siftObject.getSift().detectAndCompute(siftObject.getObjetImage(), new Mat(), siftObject.getObjectKeypoints(), siftObject.getObjectDescriptors());
    siftObject.getSift().detectAndCompute(siftObject.getSceneImage(), new Mat(), siftObject.getSceneKeyPoints(), siftObject.getSceneDescriptors());

  }

  private void calculateMatches(SiftModel siftObject) {
    DescriptorMatcher matcher = DescriptorMatcher.create(SiftConfig.DESCRIPTOR_EXTRACTOR);
    matcher.match(siftObject.getObjectDescriptors(), siftObject.getSceneDescriptors(), siftObject.getMatches());
    List<DMatch> list = siftObject.getMatches().toList();
    double min = 0.0;
    /*for(int i = 0; i < siftObject.getObjectDescriptors().rows(); i++){
      if(list.get(i).distance <= min){
        min = list.get(i).distance;
      }
    }*/
    List<DMatch> goodMatch = new ArrayList<>();
    for(int i = 0; i < siftObject.getObjectDescriptors().rows(); i++){
      if(list.get(i).distance <= SiftConfig.OPTIMAL_THRESHOLD){
        goodMatch.add(list.get(i));
      }
    }
    siftObject.getMatches().fromList(goodMatch);
    /*double min = list.stream().mapToDouble(m -> m.distance).min().getAsDouble();
    siftObject.getMatches().fromList(list.stream()
            .filter(e -> e.distance <= SiftConfig.OPTIMAL_THRESHOLD * min).collect(Collectors.toList()));
    for (int i = 0; i < knnMatches.size(); i++) {
      if (knnMatches.get(i).rows() > 1) {
        DMatch[] matches = knnMatches.get(i).toArray();
        if (matches[0].distance / matches[1].distance <= SiftConfig.OPTIMAL_THRESHOLD) {
          listOfGoodMatches.add(matches[0]);
        }
      }
    }*/
  }
}
