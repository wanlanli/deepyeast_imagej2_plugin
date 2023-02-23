/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */
package com.martinlab.imagej;

import java.io.File;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;
import org.tensorflow.Tensor;
// import org.tensorflow.Tensor;
// import org.tensorflow.ndarray.NdArray;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat16;
import org.tensorflow.types.TInt32;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
/**
 * This example illustrates how to create an ImageJ {@link Command} plugin.
 * <p>
 * The code here is a simple DeepYeastImageJ blur using ImageJ Ops.
 * </p>
 * <p>
 * You should replace the parameter fields with your own inputs and outputs,
 * and replace the {@link run} method implementation with your own logic.
 * </p>
 */
@Plugin(type = Command.class, menuPath = "Plugins>DeepYeastImageJ")
public class DeepYeastImageJ<T extends RealType<T>> implements Command {
    //
    // Feel free to add more parameters here...
    //

    @Parameter
    private Dataset currentData;

    @Parameter
    private UIService uiService;

    @Parameter
    private OpService opService;
    public static TensorFlowModel model = new TensorFlowModel();
    public static String model_path;
    
    @Override
    public void run() {
    	System.out.print("start\n");
        if (model.getmodel()==false) {
        	if(model_path==null) {
            	ParamGUI window = new ParamGUI();
            	window.run("Load Model");
            	model_path = window.model_path;
        	}
        	System.out.print("load model");
        	// model = new HelloTensorFlow();
        	model.load(model_path);
        	System.out.print("model loaded \n");
        }
        // HelloTensorFlow model = new HelloTensorFlow();

        final Img<T> image = (Img<T>)currentData.getImgPlus();
        int[] dimOrder = getDim(image);
        System.out.print("dimorder:"+dimOrder+"\n");
        
        FloatNdArray matrix = image2array(image);
        System.out.print("to matrix:"+matrix.shape()+"\n");

		try (Tensor input = Tensor.of(TFloat16.class, Shape.of(dimOrder[0], dimOrder[1], 1), matrix::copyTo)){
			System.out.print("to tensor:\n");
			TInt32 out = (TInt32) model.predict(input);
			System.out.print("out:"+out.shape()+"\n");
			ImageProcessor outimage = array2image(out, dimOrder);
			
	     // make a stack of gradients
	        int w = dimOrder[0], h = dimOrder[1];
	        ImageStack stack = new ImageStack(w, h);
	        for (int i = 0; i < 1; i++)
	        	stack.addSlice("", outimage);
	        ImagePlus a = new ImagePlus("stack", stack);
	        // you do not need to show intermediate image
	        a.show();        
		}
	}
    public int[] getDim(Img<T> image) {
        int numberdim = image.numDimensions();
        int[] dimOrder = new int[numberdim];
		for (int i = 0; i < numberdim; i++) {
			dimOrder[i] = (int) image.dimension(i);
		}
		return dimOrder;
    }
    
    public FloatNdArray image2array(Img<T> image){
        int numberdim = image.numDimensions();
        int[] dimOrder = getDim(image);
		if (numberdim==2) {
			int h = dimOrder[0];
			int w = dimOrder[1];
			FloatNdArray matrix = NdArrays.ofFloats(Shape.of(h, w, 1));
			for (int i=0; i<h; i++) {
				for (int j=0; j<w; j++) {
					T v = image.getAt(i, j);
					float v1 = v.getRealFloat();
					// System.out.print(v1);
					FloatNdArray values = NdArrays.scalarOf(v1);
					matrix.set(values, i,j,0);
				}
			}
			return matrix;
		}
		else {
			return null;
		}
	}
    
    public ImageProcessor array2image(TInt32 output, int[] dim){
    	int w = dim[0];
    	int h = dim[1];
    	float[] p = new float[w * h];
    	for (int j = 0; j < h; j++) 
    		for (int i = 0; i < w; i++) 
    			p[i + w *j] = (float) output.getInt(0, i, j);
    	return new FloatProcessor(w, h, p, null);
	}
    /**
     * This main function serves for development purposes.
     * It allows you to run the plugin immediately out of
     * your integrated development environment (IDE).
     *
     * @param args whatever, it's ignored
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
        // create the ImageJ application context with all available services
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();

        // ask the user for a file to open
        final File file = ij.ui().chooseFile(null, "open");

        if (file != null) {
            // load the dataset
            final Dataset dataset = ij.scifio().datasetIO().open(file.getPath());

            // show the image
            ij.ui().show(dataset);
            
            // invoke the plugin
            ij.command().run(DeepYeastImageJ.class, true);
        }
    }

}
