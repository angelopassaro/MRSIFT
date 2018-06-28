package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;
import it.unisa.soa.sift.SiftModel;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.Mat;

import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author didacus
 */
public class MRSIFT {

  private static final String OPENCV_LIB = "libopencv_java341.so";
  private static int res = 0;

  public static void load_library() {
    URL url = MRSIFT.class.getResource("/" + OPENCV_LIB);
    File opencv = new File(url.getFile());
    System.load(opencv.getAbsolutePath());
  }

  public static void main(String[] args) {
    load_library();
    String firstImage = "/home/didacus/first.jpeg";
    String secondImage = "/home/didacus/second.jpg";
    SiftManager algorithm = SiftManager.getInstance();
    SiftModel sif = algorithm.findFeatures(firstImage, secondImage);
    Mat out = algorithm.detectObject(sif, sif.getObjectKeypoints().toList(), sif.getSceneKeyPoints().toList());
    Imgcodecs.imwrite("/home/didacus/result.jpg", out);
   /* File file = new File("/home/didacus/db");
    if(file.isDirectory()){
      System.out.println("Directory");
    }
    SiftManager algorithm = SiftManager.getInstance();
    List<String> paths = new ArrayList<>();
    for(File i : file.listFiles()){
      paths.add(i.getAbsolutePath());
    }
    List<SiftModel> images = algorithm.findFeatures(firstImage, paths);
    images.forEach(image -> {
      Mat mat = algorithm.createImageMatches(image);
      Imgcodecs.imwrite("/home/didacus/test/result"+res+".jpg", mat);
      Mat hom = algorithm.findImage(image, image.getObjectKeypoints().toList(), image.getSceneKeyPoints().toList());
      Imgcodecs.imwrite("/home/didacus/test/hom"+res+".jpg", hom);
      res++;
    });*/
  }

}
