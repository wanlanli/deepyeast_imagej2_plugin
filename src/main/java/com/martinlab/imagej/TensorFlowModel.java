package com.martinlab.imagej;
import org.tensorflow.Tensor;

//import org.tensorflow.types.TInt32;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
//import org.tensorflow.ndarray.FloatNdArray;
//import org.tensorflow.ndarray.NdArrays;
//import org.tensorflow.ndarray.Shape;
//import org.tensorflow.types.TFloat16;


public class TensorFlowModel {
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
  public boolean getmodel() {
	  if (this.tensorflowModelBundle == null) 
		  return false;
	  else
		  return true;
  }
}
