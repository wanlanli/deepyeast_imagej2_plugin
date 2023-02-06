package com.mycompany.imagej;
import java.nio.FloatBuffer;

import org.tensorflow.ConcreteFunction;
import org.tensorflow.Signature;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.math.Add;
import org.tensorflow.types.TInt32;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.types.TFloat16;


public class ImageProcess {
	public static void main(String[] args) {
	}
	public static float[] arrayFloat(
		final RandomAccessibleInterval<FloatType> image)
	{
		final float[] value = floatArray(image);
		// FloatBuffer buffer = FloatBuffer.wrap(value);
		return value;
	}
	private static float[] floatArray(
			final RandomAccessibleInterval<FloatType> image)
	{
			final float[] array = extractFloatArray(image);
			return array == null ? createFloatArray(image) : array;
	}
	private static float[] extractFloatArray(
			final RandomAccessibleInterval<FloatType> image)
	{
		if (!(image instanceof ArrayImg)) return null;
		@SuppressWarnings("unchecked")
		final ArrayImg<FloatType, ?> arrayImg = (ArrayImg<FloatType, ?>) image;
		// GOOD NEWS: float[] rasterization order is dimension-wise!
		// BAD NEWS: it always goes d0,d1,d2,.... is that the order we need?
		// MORE BAD NEWS: As soon as you use Views.permute, image is not ArrayImg anymore.
		// SO: This only works if you give a RAI that happens to be laid out directly as TensorFlow desires.
		final Object dataAccess = arrayImg.update(null);
		return dataAccess instanceof FloatArray ? //
			((FloatArray) dataAccess).getCurrentStorageArray() : null;
	}
	public static float[] createFloatArray(
			final RandomAccessibleInterval<FloatType> image)
	{
		final long[] dims = Intervals.dimensionsAsLongArray(image);
		final ArrayImg<FloatType, FloatArray> dest = ArrayImgs.floats(dims);
		copy(image, dest);
		return dest.update(null).getCurrentStorageArray();
	}

	public static <T extends RealType<T>> void copy(
			final RandomAccessibleInterval<T> source,
			final IterableInterval<T> dest)
	{
		final RandomAccess<T> sourceAccess = source.randomAccess();
		final Cursor<T> destCursor = dest.localizingCursor();
		while (destCursor.hasNext()) {
			destCursor.fwd();
			sourceAccess.setPosition(destCursor);
			destCursor.get().set(sourceAccess.get());
		}
	}
}
