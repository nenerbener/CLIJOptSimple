/**
 * 
 */
package com.nenerbener;

import joptsimple.OptionException;
import joptsimple.ValueConversionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import static joptsimple.util.RegexMatcher.*;
//import org.junit.Test;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.*;

import java.io.File;
import java.io.IOException;
import java.lang.AssertionError;

/**
 * Jopt-simple commandline processing class for googleSRT commandline program
 * @author mm
 *
 */
public class CLIJOptSimple {

	/**
	 * create CLIJOptSimple object
	 */
	public CLIJOptSimple() {
	}

	private String[] args;
	private String inputFile;
	private File outputDir;
	private Boolean d;
	private Boolean t;
	private Boolean r;
	
	/**
	 * <p>
	 * <u> Parse command line, access params using setter and getter methods. </u>
	 * NullPointerException and OptionExceptions are caught and program terminated.  
	 * </p>
	 * @param
	 * inputFile Input video URL (e.g., Youtube video) 
	 * @param
	 * outputDir Output directory to extract video closed caption files
	 * @param d - debug flag
	 * @param t - add readable title to caption file 
	 * @param r - add readable track to caption title
	 */
	public boolean readCLI(String[] args) {

		String regexInputFile = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";		
		String regexOutputDir = "^[^-+&@#/%?=~|!:,;].+";		
		OptionSet options; //post-parsed options
		File outputDirDefault = new File( System.getProperty( "java.io.tmpdir" ) );

		//create optionParser (arguments and characters template to parse against)
		OptionParser optionParser = new OptionParser("dtr");
		optionParser.accepts("inputFile").withRequiredArg().withValuesConvertedBy(regex(regexInputFile));
		OptionSpec<File> outputDirOS=optionParser.accepts("outputDir").withRequiredArg().ofType(File.class)
				.defaultsTo(outputDirDefault);

		//perform parsing of args against created optionParser
		try {
			options = optionParser.parse(args);
			try {
				inputFile = (String) options.valueOf("inputFile");
			} catch (NullPointerException e) {
				System.out.println("inputFile read as null");
				System.exit(0);
			}
			outputDir = (File) options.valueOf("outputDir");
			try {
				regex(regexOutputDir).convert(outputDir.toString()).equals(null); 
			} catch (ValueConversionException e) {
				System.out.println("Cannot parse argument '" + outputDir.toString() + "' of option outputDir");
				System.exit(0);;
			}
			if (!outputDir.exists())  {
				if (outputDir.mkdir()) {
					System.out.println("Output directory is created!");
				}
			}
			d=options.has("d");
			t=options.has("t");
			r=options.has("r");
			return true; //successful parse
		} catch (OptionException oe) {
			System.out.println(oe.getMessage());
			System.exit(0);
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
			System.exit(0);
		} 
		return false; //exception occurred
	}

	/**
	 * Displays help menu
	 * @throws IOException
	 */
	public void helpCLI(String[] args) {

		OptionSet options=null; //post-parsed options
		File outputDirDefault = new File( System.getProperty( "java.io.tmpdir" ) );

		//create optionParser (arguments and characters template to parse against)
		OptionParser optionParser = new OptionParser("dtr");
		optionParser.accepts("d","debug - flag");
		optionParser.accepts("t","add descriptive title from URL - flag");
		optionParser.accepts("r","add descriptive track title - flag");
		optionParser.accepts("inputFile","input URL").withRequiredArg().ofType(String.class);
		OptionSpec<File> outputDirOS=optionParser.accepts("outputDir","output directory").withRequiredArg().ofType(File.class)
				.defaultsTo(outputDirDefault);
		optionParser.accepts("help","commandline help - flag").forHelp();

		// check for help option
		try {
			options = optionParser.parse(args);
		} catch (OptionException e) {
//			throw e;
			System.exit(0);
		} catch (NullPointerException e) {
//			throw e;
			System.exit(0);
		}

		// print help to standard out
		if (options.has("help")) {
			try {
				optionParser.printHelpOn(System.out);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(0);
			}
		}
	}	
	
	public String getInputFile() {
		return inputFile;
	}
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	public File getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}
	public Boolean getD() {
		return d;
	}
	public void setD(Boolean d) {
		this.d = d;
	}
	public Boolean getT() {
		return t;
	}
	public void setT(Boolean t) {
		this.t = t;
	}
	public Boolean getR() {
		return r;
	}
	public void setR(Boolean r) {
		this.r = r;
	}
	
	/**
	 * main program for testing
	 * @param args - test args
	 * @throws Exception 
	 */
	public static void main(String[] args) {
		
		CLIJOptSimple cli = new CLIJOptSimple();
//		try {
			cli.helpCLI(args);
//		} catch (IOException e) {
//			System.exit(0);
//		} catch (NullPointerException e) {
//			System.exit(0);
//		} 
//		try {
			cli.readCLI(args);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		if (!cli.getInputFile().equals(null)) System.out.println("input file: " + cli.getInputFile().toString());
		if (!cli.getOutputDir().equals(null)) System.out.println("output dir: " + cli.getOutputDir().toString() + " exists? " +cli.getOutputDir().exists());
		System.out.println("flags: "); 
		if (!cli.getD().equals(null)) System.out.println("d: " + cli.getD());
		if (!cli.getT().equals(null)) System.out.println("t: " + cli.getT());
		if (!cli.getR().equals(null)) System.out.println("r: " + cli.getR());
	}
}
