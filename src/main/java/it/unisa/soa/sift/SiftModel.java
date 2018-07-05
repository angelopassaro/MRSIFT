package it.unisa.soa.sift;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.commons.lang.SerializationUtils;
import org.apache.hadoop.io.Writable;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;


/**
 *
 * @author didacus
 */
public class SiftModel
//        implements Writable

{
  
  private final Mat image;
  private final MatOfKeyPoint imageKeypoints;
  private final MatOfKeyPoint imageDescriptors;

  public SiftModel(byte[] dataStream) {
    this.image = Imgcodecs.imdecode(new MatOfByte(dataStream), Imgcodecs.IMREAD_GRAYSCALE);
    this.imageKeypoints = new MatOfKeyPoint();
    this.imageDescriptors = new MatOfKeyPoint();
  }

  public Mat getImage() {
    return image;
  }

  public MatOfKeyPoint getImageKeypoints() {
    return imageKeypoints;
  }

  public MatOfKeyPoint getImageDescriptors() {
    return imageDescriptors;
  }

//  @Override
//  public void write(DataOutput out) throws IOException {
//    int krows = this.imageKeypoints.rows();
//    int kcols = this.imageKeypoints.cols();
//    int ktype = this.imageKeypoints.type();
//    float[] kdata = new float[krows * 7];
//    this.imageKeypoints.get(0, 0, kdata);
//    out.writeInt(krows);
//    out.writeInt(kcols);
//    out.writeInt(ktype);
//    out.write(SerializationUtils.serialize(kdata));
//    int drows = this.imageDescriptors.rows();
//    int dcols = this.imageDescriptors.cols();
//    int dtype = this.imageDescriptors.type();
//    float[] ddata = new float[drows * 7];
//    this.imageDescriptors.get(0, 0, ddata);
//    out.writeInt(drows);
//    out.writeInt(dcols);
//    out.writeInt(dtype);
//    out.write(SerializationUtils.serialize(ddata));
//    int icols = this.image.cols();
//    int irows = this.image.rows();
//    int itype = this.image.type();
//    byte[] idata = new byte[(int)this.image.total() * this.image.channels()];
//    this.image.get(0, 0, idata);
//    out.writeInt(irows);
//    out.writeInt(icols);
//    out.writeInt(itype);
//    out.write(SerializationUtils.serialize(idata));
//  }
//
//  @Override
//  public void readFields(DataInput in) throws IOException {
//    int krows = in.readInt();
//    int kcols = in.readInt();
//    int ktype = in.readInt();
//    byte[] kdata = new byte[krows * 7];
//    in.readFully(kdata);
//    int drows = in.readInt();
//    int dcols = in.readInt();
//    int dtype = in.readInt();
//    byte[] ddata = new byte[drows * 7];
//    in.readFully(ddata);
//    int icols = in.readInt();
//    int irows = in.readInt();
//    int itype = in.readInt();
//    byte[] idata = new byte[(int)this.image.total() * this.image.channels()];
//  }
}
