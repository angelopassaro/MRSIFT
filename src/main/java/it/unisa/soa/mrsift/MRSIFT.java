package it.unisa.soa.mrsift;

/**
 *
 * @author didacus
 */
public class MRSIFT {
  
  public static void main(String[] args) {
    /*obbligatorie*/
    nu.pattern.OpenCV.loadShared();
    System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
  }
  
}
