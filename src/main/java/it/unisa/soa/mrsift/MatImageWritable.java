package it.unisa.soa.mrsift;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.opencv.core.Mat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Epanchee on 24.02.15.
 */
public class MatImageWritable implements Writable {

    protected Mat im;
    protected String fileName;
    protected String format;


    public MatImageWritable() {
        this.im = new Mat();
        this.format = "undef";
        this.fileName = "unnamed";
    }

    public MatImageWritable(Mat mat) {
        this();
        this.im = mat;
    }

    public MatImageWritable(Mat mat, String fileName, String format) {
        this(mat);
        setFileName(fileName);
        setFormat(format.toLowerCase());
    }

    public void write(DataOutput out) throws IOException {
        Text.writeString(out, format);
        // Write image file name
        Text.writeString(out, fileName);
        // Write image
        byte[] byteArray = new byte[(int) (im.total() * im.channels())];
        im.get(0, 0, byteArray);
        // Write Mat array size
        out.writeInt((int) (im.total() * im.channels()));
        // Write Mat image width
        out.writeInt(im.width());
        // Write Mat image height
        out.writeInt(im.height());
        // Write image type
        out.writeInt(im.type());
        // Write image bytes
        out.write(byteArray);
    }

    public void readFields(DataInput in) throws IOException {
        // Read image type
        format = Text.readString(in);
        // Read image file name
        fileName = Text.readString(in);
        // Read Mat array size
        int arraySize = in.readInt();
        // Read Mat image width
        int mWidth = in.readInt();
        // Read Mat image height
        int mHeight = in.readInt();
        // Read Mat image type
        int type = in.readInt();
        // Read image byte array
        byte[] bArray = new byte[arraySize];
        in.readFully(bArray);
        this.im = new Mat(mHeight, mWidth, type);
        // Read image from byte array
        this.im.put(0, 0, bArray);
    }

    public Mat getImage(){
        return im;
    }

    public String getFormat() {
        return format;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fName) {
        fileName = fName;
    }

    public void setFormat(String fFormat) {
        format = fFormat;
    }

    public void setImage(Mat bi) {
        im = bi;
    }
}
