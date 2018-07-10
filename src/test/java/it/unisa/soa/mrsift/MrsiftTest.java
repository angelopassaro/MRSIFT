package it.unisa.soa.mrsift;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import static org.junit.Assert.*;

/**
 *
 * @author didacus
 */
public class MrsiftTest {

    public MrsiftTest() {
    }


    @Before
    public void setUp() {
        MRSIFT.load_library();
    }

        @Ignore
        public void testmatToByte(){
            Mat mat =Imgcodecs.imread("/home/angelo/test/carta.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            byte[] result = SiftUtils.matToByte(mat);
           // Mat mat2 = new Mat();
            Mat mat2 = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC1);
            mat2.put(0, 0, result);
            Imgcodecs.imwrite("/home/angelo/test/out.jpg",mat2);
            //mat2.put(0,0,result);
            assertEquals(mat.dump(), mat2.dump());
            //assertEquals(mat.elemSize(), mat2.elemSize());
            //assertEquals(mat.channels(), mat2.channels());
    }

    @Test
    public void testbyteToMat(){
        Mat mat =Imgcodecs.imread("/home/angelo/test/carta.jpg",Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        System.out.println(mat);
        byte[] result = SiftUtils.matToByte(mat);
        Mat mat2 = SiftUtils.byteToMat(result);
        Imgcodecs.imwrite("/home/angelo/test/out.jpg",mat2);
        assertEquals(mat.total(), mat2.total());
        assertEquals(mat.dump(), mat2.dump());
    }
}

