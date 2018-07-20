package com.nenerbener;
 
import joptsimple.OptionException;
import joptsimple.ValueConversionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import static joptsimple.util.RegexMatcher.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
 
import joptsimple.util.RegexMatcher;
import static org.junit.Assert.*;
//import org.junit.rules.ExpectedException.*;
 
/**
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 */
public class CLIJOptSimpleTest {
	
	String regexInputFile = "^https?://(www.)?youtube.com/watch\\?v=[\\w]{11}"; //Youtube.com page regex
	String regexOutputDir = "^[^-+&@#/%?=~|!:,;].+"; //Regex to avoid mkdir to make non-alphabet starting output dir
	OptionSet options; //post-parsed options
	String outputDirDefault = System.getProperty( "java.io.tmpdir" ); //returns static, is this legal?
	OptionParser optionParser;
	String[] argsCorrect = {"-i", "https://www.youtube.com/watch?v=E8G_oKr_6H4", "-o", "aaa", "-dtr"};
	String[] argsIncorrectInputFile = {"-i", "htps://www.youtube.com/watch?v=E8G_oKr_6H4", "-o", "aaa", "-dtr"};
	String[] argsIncorrectOutputDir = {"-i", "https://www.youtube.com/watch?v=E8G_oKr_6H4", "-o", "-aaa", "-dtr"};
	String argsIncorrectInputFileString = 
			"Cannot parse argument 'htps://www.youtube.com/watch?v=E8G_oKr_6H4' of option inputFile";
	String optionExceptionString = "TBD";

    @Rule public final ExpectedException thrown = ExpectedException.none();
 
    private RegexMatcher abc;
 
    @Before
    public void setUp() {
    	optionParser = new OptionParser("dtr"); //add option flags
    }
 
    @Test
    public void readCLIParseTest() {

    	thrown.expect(OptionException.class);
        thrown.expectMessage(optionExceptionString);
        
		//create optionParser (arguments and characters template to parse against)
		optionParser.accepts("inputFile").withRequiredArg().withValuesConvertedBy(regex(regexInputFile));
		optionParser.accepts("outputDir").withRequiredArg().withValuesConvertedBy(regex(regexOutputDir))
			.defaultsTo(outputDirDefault);
		options = optionParser.parse(args); //parse args
        assertEquals(argsIncorrectInputFileString, convert(argsIncorrectInputFile) );
    }
 
    @Test( expected = ValueConversionException.class )
    public void rejectsValueThatDoesNotMatchRegex() {
        abc.convert( "abcd" );
    }
 
    @Test
    public void raisesExceptionContainingValueAndPattern() {
        thrown.expect( ValueConversionException.class );
        thrown.expectMessage( "\\d+" );
        thrown.expectMessage( "asdf" );
 
        new RegexMatcher( "\\d+", 0 ).convert( "asdf" );
    }
 
    @Test
    public void shouldOfferConvenienceMethodForCreatingMatcherWithNoFlags() {
        assertEquals( "sourceforge.net", new RegexMatcher( "\\w+\\.\\w+",0).convert( "sourceforge.net" ) );
    }
 
    @Test
    public void shouldAnswerCorrectValueType() {
        assertEquals( String.class, abc.valueType() );
    }
 
    @Test
    public void shouldGiveCorrectValuePattern() {
        assertEquals( "abc", abc.valuePattern() );
    }
}