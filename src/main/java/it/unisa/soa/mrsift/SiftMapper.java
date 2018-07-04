package it.unisa.soa.mrsift;

import java.io.IOException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencv.core.MatOfKeyPoint;

/**
 *
 * @author didacus
 */
public class SiftMapper extends Mapper<Text, BytesWritable, Text, MatOfKeyPoint> {

  @Override
  protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
    
  }

  

 
  
}
