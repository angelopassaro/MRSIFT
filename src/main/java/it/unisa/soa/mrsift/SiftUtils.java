package it.unisa.soa.mrsift;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.core.MatOfKeyPoint;

public class SiftUtils {

    private static byte[] readStream(InputStream stream) throws IOException {
        // Copy content of the image to byte-array
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] temporaryImageInMemory = buffer.toByteArray();
        buffer.close();
        stream.close();
        return temporaryImageInMemory;
    }

    protected static Mat readInputStreamIntoMat(InputStream inputStream) throws IOException {
        byte[] temporaryImageInMemory = readStream(inputStream);
        Mat outputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory),
                Imgcodecs.IMREAD_GRAYSCALE);
        return outputImage;
    }

    protected static Mat byteToMat(byte[] value) {


        Mat mat = new Mat(269,187, CvType.CV_8UC1);
        Mat mat2 = new Mat(1,value.length, CvType.CV_8UC1 );
        mat2.put(0,0,value);

        int c = 0;

            for(int i = 0; i < 269; i++) {
                for (int j = 0; j < 187; j++) {
                    mat.put(i, j, mat2.get(0,c++));
                }
            }

        return mat;
    }


    protected static byte[] matToByte(Mat value) {
        int size =  value.channels() * (int)value.total();
        byte[] bytes = new byte[size];
        value.get(0, 0, bytes);
        return bytes;

    }

    protected static MapWritable createMapWritable(MatOfKeyPoint objectKeyPoints, MatOfKeyPoint objectDescriptors, Mat mat) {
        MapWritable map = new MapWritable();
        byte[] keyPointsBytes = new byte[objectKeyPoints.rows() * (int) objectKeyPoints.elemSize()];
        objectKeyPoints.get(0, 0, keyPointsBytes);
        byte[] descriptorBytes = new byte[objectDescriptors.rows() * (int) objectDescriptors.elemSize()];
        byte[] imageBytes = new byte[(int) mat.total() * (int) mat.elemSize()];
        mat.get(0, 0, imageBytes);
        map.put(new Text("objectKeypoint"), new BytesWritable(keyPointsBytes));
        map.put(new Text("objectDescriptors"), new BytesWritable(descriptorBytes));
        map.put(new Text("objectImage"), new BytesWritable(imageBytes));
        return map;
    }
}
