package it.unisa.soa.mrsift;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        // Read into byte-array
        byte[] temporaryImageInMemory = readStream(inputStream);

        // Decode into mat. Use any IMREAD_ option that describes your image appropriately
        Mat outputImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory),
                Imgcodecs.IMREAD_GRAYSCALE);

        return outputImage;
    }

    protected static Mat byteToMat (BytesWritable value) {
        byte[] bytes = value.getBytes();
        Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_GRAYSCALE);
        return mat;

    }

    protected static byte[] matToByte(Mat value){

        int size = (int)value.total()*(int)value.elemSize();
        byte[] bytes = new byte[size];

        System.arraycopy(value,0,bytes,0,size);  // possibile problema

        return bytes;
    }


    protected static MapWritable createMapWritable (Mat objectKeyPoints, Mat objectDescriptors, Mat mat) {
        List<BytesWritable> bytesWritables = new ArrayList<>();
        MapWritable map = new MapWritable();


        byte[] keyPointsBytes = new byte[objectKeyPoints.rows() * (int)objectKeyPoints.elemSize()];
        objectKeyPoints.get(0, 0, keyPointsBytes);
        byte[] descriptorBytes = new byte[objectDescriptors.rows() * (int)objectDescriptors.elemSize()];
        byte[] imageBytes = new byte[(int)mat.total() * (int)mat.elemSize()];
        mat.get(0, 0, imageBytes);

        map.put(new Text("objectKeypoint"), new BytesWritable(keyPointsBytes));
        map.put(new Text("objectDescriptors"), new BytesWritable(descriptorBytes));
        map.put(new Text("objectImage"), new BytesWritable(imageBytes));

       // bytesWritables.add(new BytesWritable(keyPointsBytes));
       // bytesWritables.add(new BytesWritable(descriptorBytes));
       // bytesWritables.add(new BytesWritable(imageBytes));

        return map;

    }

/*
    protected static List<BytesWritable> byteToBytesWritable (Mat objectKeyPoints, Mat objectDescriptors, Mat mat) {
        List<BytesWritable> bytesWritables = new ArrayList<>();

        byte[] keyPointsBytes = new byte[objectKeyPoints.rows() * (int)objectKeyPoints.elemSize()];
        objectKeyPoints.get(0, 0, keyPointsBytes);
        byte[] descriptorBytes = new byte[objectDescriptors.rows() * (int)objectDescriptors.elemSize()];
        byte[] imageBytes = new byte[(int)mat.total() * (int)mat.elemSize()];
        mat.get(0, 0, imageBytes);

        bytesWritables.add(new BytesWritable(keyPointsBytes));
        bytesWritables.add(new BytesWritable(descriptorBytes));
        bytesWritables.add(new BytesWritable(imageBytes));

        return bytesWritables;

    }



    protected static MapWritable getMapWritable (MapWritable map) {
        MapWritable ma = new MapWritable();

        byte[] rcvimg = ((BytesWritable) map.get(new Text("objectImage"))).getBytes();
        byte[] opoints = ((BytesWritable) map.get(new Text("objectKeypoint"))).getBytes();
        byte[] odesc = ((BytesWritable) map.get(new Text("objectDescriptors"))).getBytes();



        return ma;

    }
    */
}
