/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.mycompany.imagej;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imagej.tensorflow.Tensors;

import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
// import net.imglib2.type.numeric.integer.LongType;
// import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import org.tensorflow.Tensor;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;

import java.nio.FloatBuffer;

import org.tensorflow.DataType;
import org.tensorflow.types.TFloat16;

// import org.tensorflow.types.UInt8;

// import net.imglib2.img.basictypeaccess.array.LongArray;

//import org.tensorflow.ndarray.Shape;
//import org.tensorflow.ndarray.buffer.ByteDataBuffer;
//import org.tensorflow.op.Ops;
import org.tensorflow.types.UInt8;
//import org.tensorflow.types.TFloat32;
//import org.tensorflow.types.TString;
//import org.tensorflow.ndarray.ByteNdArray;
//import org.tensorflow.ndarray.NdArrays;
//import org.tensorflow.ndarray.buffer.DataBuffers;
//import org.tensorflow.op.image.DecodeImage;
//import org.tensorflow.op.Scope;
import org.tensorflow.Graph;
import org.tensorflow.Session;

//import java.awt.image.BufferedImage;
import java.io.File;
// import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
// import net.imglib2.type.numeric.real.FloatType;
/**
 * This example illustrates how to create an ImageJ {@link Command} plugin.
 * <p>
 * The code here is a simple Gaussian blur using ImageJ Ops.
 * </p>
 * <p>
 * You should replace the parameter fields with your own inputs and outputs,
 * and replace the {@link run} method implementation with your own logic.
 * </p>
 */
@Plugin(type = Command.class, menuPath = "Plugins>Gauss Filtering")
public class GaussFiltering<T extends RealType<T>> implements Command {
    //
    // Feel free to add more parameters here...
    //

    @Parameter
    private Dataset currentData;

    @Parameter
    private UIService uiService;

    @Parameter
    private OpService opService;

    @Override
    public void run() {
        final Img<T> image = (Img<T>)currentData.getImgPlus();
        System.out.print(image.numDimensions());
        System.out.print(image.getAt(100,200));
        System.out.print("aaaa");        
        int numberdim = image.numDimensions();
        int[] dimOrder = new int[numberdim];
		for (int i = 0; i < numberdim; i++) {
			dimOrder[i] = (int) image.dimension(i);
		}
		// Img<FloatType> region = ImagePlusAdapter.wrap(img);
		// RandomAccessibleInterval a = TensorManagement.imPlus2tensor(image, "HW");
		// float[] b = ImageProcess.createFloatArray(a);
		if (numberdim==2) {
			int h = dimOrder[0];
			int w = dimOrder[1];
			FloatNdArray matrix = NdArrays.ofFloats(Shape.of(h, w, 1));
			for (int i=0; i<h; i++) {
				System.out.print(i);
				System.out.print("\n");
				for (int j=0; j<w; j++) {
					T v = image.getAt(i, j);
					float v1 = v.getRealFloat();
					// System.out.print(v1);
					
					FloatNdArray values = NdArrays.scalarOf(v1);
					matrix.set(values, i,j,0);
				}
			}
			try (Tensor input = Tensor.of(TFloat16.class, Shape.of(100, 100, 1))){
				  System.out.print("t");
			}
		}
		
//		System.out.print("\nbbbbbb");
//		Graph graph = new Graph();
//		Session session = new Session(graph);
//		Tensor input_tensor = Tensors.tensor(image);
//		session.runner().feed("sequential_1_input", input_tensor).fetch("output/Softmax").run().get(0);
//		System.out.print("\n222222");
//		session.close();
		// Tensor input_tensor = Tensors.tensor(image);

		// Tensor<?> input_senor = Tensors.tensor(image);
		// Tensor<?> input_senor = Tensors.tensor(image);
		// try(Tensor<?> input_senor = Tensor.create(input)){
//		final int NUM_PREDICTIONS = 1;
//        Tensor<?> x = Tensor.create(
//                new long[] {NUM_PREDICTIONS}, 
//                FloatBuffer.wrap( new float[] {2.0f} ) );
        System.out.print("\nccccc");
//		try(Tensor<?> input_senor = Tensor.create(2.0f)){
//			System.out.print("\nccccc");
//			System.out.print(input_senor);
//		}
//		catch(Exception e) {
//			System.out.print("\ndddddd");
//			System.out.print(e);
//		}
//		finally {
//			System.out.println("The 'try catch' is finished.");
//		}
//        Graph graph = new Graph();
//        Scope scope = new Scope(graph);
//        DecodeImage.create(scope, image.toString(), TFloat16.class);
//        Tensor a =tf.constant(image.toString()).asTensor();
//        Tensor a = tf.reshape(tf.constant(image.toString()), tf.array(image.size())).asTensor();
//        		tf.reshape(
//        		tf.dtypes.cast(tf.constant(image.toString()), TString.class),
//        		tf.array(-1L, image.size())
//        		).asTensor();
 //       System.out.print(a);
        
        // Enter image processing code here ...
        // The following is just a Gauss filtering example
        //
        // HelloTensorFlow model = new HelloTensorFlow();
        // TFloat16 test = TFloat16.tensorOf(image.);
        // Tensor input_tensor = Tensor.of(image.toString());
        // Tensor out = model.predict(input_tensor);
        
        final double[] sigmas = {1.0, 3.0, 5.0};

        List<RandomAccessibleInterval<T>> results = new ArrayList<>();

        for (double sigma : sigmas) {
            results.add(opService.filter().gauss(image, sigma));
        }

        // display result
        for (RandomAccessibleInterval<T> elem : results) {
            uiService.show(elem);
        }
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
            ij.command().run(GaussFiltering.class, true);
        }
    }

}
