package it.unisa.soa.mrsift;

import it.unisa.soa.sift.SiftManager;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.xfeatures2d.SIFT;

import java.io.File;


public class TestManager {

    private final String testImagePath;
    private final String scene;
    private final String scene2;
    private SiftManager sm;




    public TestManager() {
        super();
        this.testImagePath = new File(MrsiftTest.class.getResource("/mcdonalds.jpg").getFile()).getAbsolutePath();
        this.scene = new File(MrsiftTest.class.getResource("/scene1.jpg").getFile()).getAbsolutePath();
        this.scene2 = new File(MrsiftTest.class.getResource("/scene2.jpg").getFile()).getAbsolutePath();

    }

    @Before
    public void setUp(){
        MRSIFT.load_library();
    }


    @Test
    public void testmatch() {

        this.sm = SiftManager.getInstance();

        Mat obj = Imgcodecs.imread(testImagePath, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

        Mat s = Imgcodecs.imread(scene, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        Mat s2 = Imgcodecs.imread(scene2, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);


        MatOfKeyPoint okp = sm.extractKeypoints(obj, SIFT.create());
        MatOfKeyPoint okd = sm.extractDescriptors(obj,okp, SIFT.create());

        MatOfKeyPoint skp = sm.extractKeypoints(s, SIFT.create());
        MatOfKeyPoint skd = sm.extractDescriptors(s,okp, SIFT.create());

        MatOfDMatch match = sm.calculateMatches(okd, skd);

        Mat det = sm.detectObject(obj,s,match,okp,skp);

        Imgcodecs.imwrite("/home/angelo/test/out.jpg", det);
        System.out.println(CvType.typeToString(okd.type()));
        System.out.println(CvType.typeToString(skd.type()));


    }


}
