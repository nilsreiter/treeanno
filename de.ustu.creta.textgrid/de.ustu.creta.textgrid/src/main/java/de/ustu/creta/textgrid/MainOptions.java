package de.ustu.creta.textgrid;

import java.io.File;
import java.util.List;

import com.lexicalscope.jewel.cli.Option;

public interface MainOptions {

	@Option
	List<File> getFiles();

	@Option
	File getOutputDirectory();

	@Option(defaultValue = "1000")
	int getFirstId();

	@Option(defaultToNull = true)
	File getGenreList();

	boolean getPrintFront();

	boolean getPrintBack();
}
