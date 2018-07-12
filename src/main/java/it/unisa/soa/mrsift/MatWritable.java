package it.unisa.soa.mrsift;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.opencv.core.Mat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author Diego Avella
 * @author Angelo Passaro
 * @author Antonio Addeo This class is a Writable image optimized for M/R Task
 */
public class MatWritable implements Writable {

  private Mat image;
  private String fileName;
  private String format;

  public MatWritable() {
    this.image = new Mat();
    this.format = "undef";
    this.fileName = "unnamed";
  }

  public MatWritable(Mat mat) {
    this.image = mat;
    this.format = "undef";
    this.fileName = "unnamed";
  }

  public MatWritable(Mat mat, String fileName, String format) {
    this.image = mat;
    this.fileName = fileName;
    this.format = format.toLowerCase();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    Text.writeString(out, this.format);
    Text.writeString(out, this.fileName);
    out.writeInt(SiftUtils.totalSize(this.image));
    byte[] byteArray = SiftUtils.matToByte(this.image);
    out.writeInt(this.image.width());
    out.writeInt(this.image.height());
    out.writeInt(this.image.type());
    out.write(byteArray);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    this.format = Text.readString(in);
    this.fileName = Text.readString(in);
    int arraySize = in.readInt();
    int mWidth = in.readInt();
    int mHeight = in.readInt();
    int type = in.readInt();
    byte[] bArray = new byte[arraySize];
    in.readFully(bArray);
    this.image = SiftUtils.byteToMat(bArray,mWidth,mHeight,type);
  }

  public Mat getImage() {
    return this.image;
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
    this.image = bi;
  }
}
