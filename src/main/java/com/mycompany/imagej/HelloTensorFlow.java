package com.mycompany.imagej;
import org.tensorflow.ConcreteFunction;
import org.tensorflow.Signature;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.math.Add;
import org.tensorflow.types.TInt32;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat16;


public class HelloTensorFlow {
  SavedModelBundle tensorflowModelBundle;
  Session tensorflowSession;
  void load(String modelPath) {
	  this.tensorflowModelBundle = SavedModelBundle.load(modelPath, "serve");
	  this.tensorflowSession = tensorflowModelBundle.session();
  }
  
  public Tensor predict(Tensor tensorInput) {
	  Tensor output = this.tensorflowSession.runner().feed("serving_default_input_tensor", tensorInput).fetch("StatefulPartitionedCall:5").run().get(0);
	  return output;
  }
  public static void main(String[] args) {

	  System.out.print("start");
	  HelloTensorFlow myModel = new HelloTensorFlow();
	  myModel.load("/home/wli6/project/fiji_plugin/models/save2/save2/");
	  // input
//	  FloatNdArray matrix = NdArrays.ofFloats(Shape.of(2, 3, 2));
//
//	  // Initialize sub-matrices data with vectors
//	  matrix.set(NdArrays.vectorOf(1.0f, 2.0f), 0, 0)
//	        .set(NdArrays.vectorOf(3.0f, 4.0f), 0, 1)
//	        .set(NdArrays.vectorOf(5.0f, 6.0f), 0, 2)
//	        .set(NdArrays.vectorOf(7.0f, 8.0f), 1, 0)
//	        .set(NdArrays.vectorOf(9.0f, 10.0f), 1, 1)
//	        .set(NdArrays.vectorOf(11.0f, 12.0f), 1, 2);
//	  FloatNdArray x = matrix.get(1);
//	  System.out.print(x.getFloat(0, 0));
	  // prediction
	  try (Tensor input = Tensor.of(TFloat16.class, Shape.of(100, 100, 1))){
		  System.out.print("t");
		  Tensor out = myModel.predict(input);
		  System.out.print(out.shape());
		  FloatNdArray matrix = NdArrays.ofFloats(Shape.of(100, 100, 1));
		  matrix.setFloat(0, null); 
	  }
	  // Tensor out = myModel.predict(input);
	  System.out.print("end");
	  
  }

  private static Signature dbl(Ops tf) {
    Placeholder<TFloat16> x = tf.placeholder(TFloat16.class);
    Add<TFloat16> dblX = tf.math.add(x, x);
    return Signature.builder().input("x", x).output("dbl", dblX).build();
  }
}
