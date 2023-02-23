package com.martinlab.imagej;
import ij.*;
import ij.gui.*;
import ij.plugin.frame.PlugInFrame;


public class ParamGUI extends PlugInFrame {
	private static final long serialVersionUID = 1L;
	public String model_path = "./";
	public ParamGUI() {
		super("ParamGUI");
		}
	
	public void run(String arg) {
		GenericDialog gd = new GenericDialog("Parameters settings");
		// GUI button = new GUI();
		gd.addDirectoryField("Model Folder Path", model_path);
		// gd.addNumericField(arg, ABORT);
		gd.showDialog();
		if (gd.wasCanceled()) {
			IJ.error("PlugIn canceled!");
			// return;
		}
		if (gd.wasOKed()) {
			model_path = gd.getNextString();
			// IJ.log(model_path);
			System.out.print(model_path+"\n");
		}
	}
	public static void main(final String... args) throws Exception {
		ParamGUI window = new ParamGUI();
		window.run("Parameters settings");
	}
}