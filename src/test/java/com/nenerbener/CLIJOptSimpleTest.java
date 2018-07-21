package com.nenerbener;
 
import joptsimple.OptionException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
 
import static org.junit.Assert.*;
 
/**
 * Junit test for CLIJOptSimple class
 * @author mm
 */
public class CLIJOptSimpleTest {
	
	CLIJOptSimple cli;
	String[] argsCorrect = {"-i", "https://www.youtube.com/watch?v=E8G_oKr_6H4", "-o", "aaa", "-dtr"};
	String[] argsIncorrectInputFile = {"-i", "htps://www.youtube.com/watch?v=E8G_oKr_6H4", "-o", "aaa", "-dtr"};
	String[] argsIncorrectOutputDir = {"-i", "https://www.youtube.com/watch?v=E8G_oKr_6H4", "-o", "-aaa", "-dtr"};
	String[] argsNull = {""};

    @Rule public final ExpectedException thrown = ExpectedException.none();
 
    @Before
    public void setUp() {
    	cli = new CLIJOptSimple();
    }
 
    /**
     * Malformed Youtube input URL test
     */
    @Test
    public void readCLIInputFileOptionExceptionTest() {
    	thrown.expect(OptionException.class);
        cli.readCLI(argsIncorrectInputFile);
    }
 
    /**
     * Malformed output directory test (e.g., outputDir = -aaa)
     */
    @Test
    public void readCLIOutputDirOptionExceptionTest() {
    	thrown.expect(OptionException.class);
        cli.readCLI(argsIncorrectOutputDir);
    }
 
    /**
     *  no commandline arguments or flags are specified
     */
    @Test
    public void readCLINullPointerExceptionTest() {
    	thrown.expect(NullPointerException.class);
        cli.readCLI(argsNull);
    }
}