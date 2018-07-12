package it.unisa.soa.mrsift;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;
import org.opencv.core.MatOfKeyPoint;

/**
 *
 * @author antonio
 */
public class MatOfKeyPointWritable implements Writable {

  private MatOfKeyPoint mat;

  public MatOfKeyPointWritable() {
    this.mat = new MatOfKeyPoint();
  }

  public MatOfKeyPointWritable(MatOfKeyPoint mat) {
    this.mat = mat;
  }

  public void setMatOfKeyPoint(MatOfKeyPoint mat) {
    this.mat = mat;
  }

  public MatOfKeyPoint getMatOfKeyPoint() {
    return mat;
  }

  @Override
  public void write(DataOutput d) throws IOException {
    d.writeInt(this.mat.rows());
    d.writeInt(this.mat.cols());
    d.writeInt(this.mat.type());
    int size = (int) this.mat.total() * this.mat.channels();
    float[] floats = new float[size];
    d.writeInt(size);
    this.mat.get(0, 0, floats);
    for (int i = 0; i < size; i++) {
      d.writeFloat(floats[i]);
    }
  }

  @Override
  public void readFields(DataInput di) throws IOException {
    int rows = di.readInt();
    int cols = di.readInt();
    int type = di.readInt();
    int size = di.readInt();
    float[] floats = new float[size];
    for (int i = 0; i < size; i++) {
      floats[i] = di.readFloat();
    }
    this.mat.create(rows, cols, type);
    this.mat.put(0, 0, floats);
  }

}
