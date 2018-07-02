package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;
import it.unisa.soa.sift.SiftModel;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;

import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author didacus
 */
public class MRSIFT {

  private static final String OPENCV_LIB = "libopencv_java341.so";
  private static final Logger MRS_LOG = Logger.getLogger("global");

  public static void load_library() {
    URL url = MRSIFT.class.getResource("/" + OPENCV_LIB);
    File opencv = new File(url.getFile());
    System.load(opencv.getAbsolutePath());
  }

  public static void main(String[] args) {
    load_library();
    SiftManager manager = SiftManager.getInstance();
    String objectPath = "/home/didacus/object.jpg";
    String scene = "/home/didacus/scene.jpg";
    /*File dir = new File("/home/didacus/db");
    if(dir.isDirectory()){
      List<String> paths = new ArrayList<>();
      for(File f : dir.listFiles()){
        paths.add(f.getAbsolutePath());
      }
      List<SiftModel> goodModels = manager.findFeatures(objectPath, paths);
      int res = 0;
      for(SiftModel model : goodModels){
        Mat img = manager.detectObject(model, model.getObjectKeypoints().toList(), model.getSceneKeyPoints().toList());
        if(img == null){
          System.err.println("Omografia vuota\n"+model);
        }else {
          Imgcodecs.imwrite("/home/didacus/test/result_"+res+".jpg", img);
          res++;
        }
      }
    }*/
    SiftModel mo = manager.findFeatures(objectPath, scene);
    System.out.println(mo);
    Mat o = manager.detectObject(mo, mo.getObjectKeypoints().toList(), mo.getSceneKeyPoints().toList());
    Imgcodecs.imwrite("/home/didacus/result.jpg", o);
  }
}
