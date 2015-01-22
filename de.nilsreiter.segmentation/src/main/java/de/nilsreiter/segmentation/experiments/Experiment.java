package de.nilsreiter.segmentation.experiments;

import java.io.File;

public abstract class Experiment {
	File workingDirectory;

	String inputDirectoryName;
	String outputDirectoryName;

	public Experiment(File wDir) {
		workingDirectory = wDir;
	}

	public File getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public String getOutputDirectory() {
		return outputDirectoryName;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectoryName = outputDirectory;
	}

	public String getInputDirectoryName() {
		return inputDirectoryName;
	}

	public void setInputDirectoryName(String inputDirectoryName) {
		this.inputDirectoryName = inputDirectoryName;
	}

}
