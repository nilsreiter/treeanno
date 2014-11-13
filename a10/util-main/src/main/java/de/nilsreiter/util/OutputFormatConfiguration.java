package de.nilsreiter.util;

import org.kohsuke.args4j.Option;

public class OutputFormatConfiguration {
	@Option(name = "--of:style", usage = "The output format to use")
	public Output.Style outputStyle = Output.Style.CSV;

	@Option(name = "--of:numberFormat", usage = "The number format")
	public String numberFormat = "%1$.3e";

}
