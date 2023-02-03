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
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import org.tensorflow.Tensor;
//import org.tensorflow.ndarray.Shape;
//import org.tensorflow.ndarray.buffer.ByteDataBuffer;
//import org.tensorflow.op.Ops;
//import org.tensorflow.types.TFloat16;
//import org.tensorflow.types.TFloat32;
//import org.tensorflow.types.TString;
//import org.tensorflow.ndarray.ByteNdArray;
//import org.tensorflow.ndarray.NdArrays;
//import org.tensorflow.ndarray.buffer.DataBuffers;
//import org.tensorflow.op.image.DecodeImage;
//import org.tensorflow.op.Scope;
//import org.tensorflow.Graph;

//import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.imglib2.type.numeric.real.FloatType;
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
        System.out.print(image.dimension(0));
        System.out.print(image.dimension(1));
 
        System.out.print("aaaa");
        // Tensor input_tensor = TensorManagement.imPlus2tensor(image, "HW");
        
		int[] dimOrder = new int[2];
		for (int i = 0; i < 2; i++) {
			dimOrder[i] = 512;
		}
		System.out.print("\nbbbbbb");
		Tensor input_tensor = Tensors.tensor(image, dimOrder);
		System.out.print("\nccccc");
		System.out.print(input_tensor);
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
