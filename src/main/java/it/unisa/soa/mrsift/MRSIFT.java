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
    String template = "/home/didacus/first.jpg";
    SiftManager algorithm = SiftManager.getInstance();
    String second = "/home/didacus/second.jpg";
    SiftModel mo = algorithm.findFeatures(template, second);
    Mat image =algorithm.detectObject(mo, mo.getObjectKeypoints().toList(), mo.getSceneKeyPoints().toList());
    Imgcodecs.imwrite("/home/didacus/result.jpg", image);
    /*File dir = new File("/home/didacus/db");
    if(dir.isDirectory()){
      System.out.println("Directory trovata");
    }
    List<String> paths = new ArrayList<>();
    for(File i : dir.listFiles()){
      paths.add(i.getAbsolutePath());
    }
    List<SiftModel> models = algorithm.findFeatures(template, paths);
    models.forEach(model -> {
      Mat image = algorithm.detectObject(model, model.getObjectKeypoints().toList(), model.getSceneKeyPoints().toList());
      Imgcodecs.imwrite("/home/didacus/test/result"+res+".jpg", image);
      res++;
    });*/
  }

}
