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

import org.apache.maven.shared.utils.StringUtils;

/**
 * Jopt-simple commandline processing class for googleSRT commandline program
 * @author mm
 */
public class CLIJOptSimple {

	/**
	 * create CLIJOptSimple object
	 */
	public CLIJOptSimple() {
	}

	private String[] args;
	private String inputFile;
	private String outputDir;
	private File fileOutputDir;
	private Boolean d;
	private Boolean t;
	private Boolean r;
	
	/**
	 * <p>
	 * Parse command line, access params using setter and getter methods.
	 * </p>
	 * @param
	 * inputFile Input video URL (e.g., Youtube video) 
	 * @param
	 * outputDir Output directory to extract video closed caption files
	 * @param d - debug flag
	 * @param t - add readable title to caption file 
	 * @param r - add readable track to caption title
	 * 
	 */
	public boolean readCLI(String[] args) throws OptionException, NullPointerException {

		String regexInputFile = "^https?://(www.)?youtube.com/watch\\?v=[\\w]{11}"; //Youtube.com page regex
		String regexOutputDir = "^[^-+&@#%?=~|!:,;].+"; //Regex to avoid mkdir to make non-alphabet starting output dir
		OptionSet options; //post-parsed options
		String outputDirDefault = System.getProperty( "java.io.tmpdir" ); //returns static, is this legal?

		//create optionParser (arguments and characters template to parse against)
		OptionParser optionParser = new OptionParser("dtr");
		optionParser.accepts("inputFile").withRequiredArg().withValuesConvertedBy(regex(regexInputFile));
		optionParser.accepts("outputDir").withRequiredArg().withValuesConvertedBy(regex(regexOutputDir))
			.defaultsTo(outputDirDefault);

		//perform parsing of args against created optionParser
		try {
			options = optionParser.parse(args);
			try {
				inputFile = (String) options.valueOf("inputFile");
				if (StringUtils.isEmpty(inputFile)) {
					throw new NullPointerException("Null pointer or empty string of option inputFile");
				}
			} 
			catch (NullPointerException e) {
				throw e;
			}
			try {
				outputDir = (String) options.valueOf("outputDir");
				regex(regexOutputDir).convert(outputDir.toString()).equals(null); 
			} catch (ValueConversionException e) {
				throw e;
			}
			fileOutputDir = new File(outputDir.toString());
			if (!fileOutputDir.exists())  {
				if (fileOutputDir.mkdir()) {
					System.out.println("Output directory is created!");
				}
			}
			d=options.has("d");
			t=options.has("t");
			r=options.has("r");
			return true; //successful parse
		} catch (OptionException e) {
			throw e;
		} catch (NullPointerException e) {
			throw e;
		} 
	}

	/**
	 * <p>
	 * Displays help menu.  
	 * Program terminates on --help|-h. 
	 * Routine returns if help flags not detected.
	 * </p>
	 * @param
	 * inputFile Input video URL (e.g., Youtube video) 
	 * @param
	 * outputDir Output directory to extract video closed caption files
	 * @param d - debug flag
	 * @param t - add readable title to caption file 
	 * @param r - add readable track to caption title
	 * 
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

		// check for help option. handle exceptions inside method
		try {
			options = optionParser.parse(args);
		} catch (OptionException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		if (options.has("help")||options.has("hel")||options.has("he")||options.has("h")) {
			try {
				optionParser.printHelpOn(System.out);
				System.exit(0);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}	
	
	public String getInputFile() {
		return inputFile;
	}
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	public String getOutputDir() {
		return outputDir;
	}
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	public File getFileOutputDir() {
		return fileOutputDir;
	}
	public void setFileOutputDir(File fileOutputDir) {
		this.fileOutputDir = fileOutputDir;
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
	 */
	public static void main(String[] args) {
		
		CLIJOptSimple cli = new CLIJOptSimple();
		cli.helpCLI(args);
		try {
			Boolean bl=cli.readCLI(args);
			System.out.println("Success!");
		} catch (OptionException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		if (!cli.getInputFile().equals(null)) System.out.println("input file: " + cli.getInputFile().toString());
		if (!cli.getFileOutputDir().equals(null)) System.out.println("output dir: " + cli.getFileOutputDir().toString() + " exists? " +cli.getFileOutputDir().exists());
		System.out.println("flags: "); 
		if (!cli.getD().equals(null)) System.out.println("d: " + cli.getD());
		if (!cli.getT().equals(null)) System.out.println("t: " + cli.getT());
		if (!cli.getR().equals(null)) System.out.println("r: " + cli.getR());
	}
}
