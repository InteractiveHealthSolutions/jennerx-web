/*
 * 
 */
package org.ird.unfepi.utils.validation.test;

import junit.framework.TestCase;

import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class DataValidationTest.
 */
public class DataValidationTest extends TestCase {

	/**
	 * Test alpha.
	 */
	@Test
	public void testALPHA(){
		String[] valuesvalid=new String[]{       
				"abcde", "ABCDE",
				 "Steven", "Stephe" ,"abcdefsfdsafdsgfsagsf" };
		String[] valuesinvalid=new String[]{"12345", "1a4A5",
				"12a45", "12aBC", "12abc", "12ab5", "12aa5", "12AB5","123-5",
				"12.45", "1a4b5", "1 3 5", "1  45", "1   5", "a  b  ",
				"a b  ca  b  c  d", "a     ", "111.22", "222.33",
				"333.44"};

		for (String string : valuesvalid) {
			assertTrue("vaild not validated:"+string,
					DataValidation.validate(REG_EX.ALPHA, string));
		}
		for (String string : valuesinvalid) {
			assertFalse("invalid validated:"+string,
					DataValidation.validate(REG_EX.ALPHA, string));
		}
	}
	
	/**
	 * Test alph a_ numeric.
	 */
	@Test
	public void testALPHA_NUMERIC (){
		String[] valuesvalid=new String[]{       
				"abcde", "ABCDE","12a45", "12aBC", "12abc", "12ab5", "12aa5", "12AB5",
				 "Steven", "Stephe" ,"abcdefsfdsafdsgfsagsf" ,"12345", "1a4A5", "1a4b5"};
		String[] valuesinvalid=new String[]{
				"123-5","12.45", "1 3 5", "1  45", "1   5", "a  b  ",
				"a b  ca  b  c  d", "a     ", "111.22", "222.33",
				"333.44"};

		for (String string : valuesvalid) {
			assertTrue("vaild not validated:"+string,
					DataValidation.validate(REG_EX.ALPHA_NUMERIC, string));
		}
		for (String string : valuesinvalid) {
			assertFalse("invalid validated:"+string,
					DataValidation.validate(REG_EX.ALPHA_NUMERIC, string));
		}
	}
	
	/**
	 * Test cel l_ number.
	 */
	@Test
	public void testCELL_NUMBER(){
		String[] valuesvalid=new String[]{       
				"03012780770",	"03002085292",
				"03433371661",	"03343328050",
				"03468875925",				"03333611523"
,				"03232975744",				"03452977411",
				"03332270728",				
				"03453681010",				"03212660718",
				"03232453216",				"03022881044",
				"03333632415",				"03453066092",
				"03012142742",				"03212177028",
				"03446475359",				"03333230415",
				"03343066454",				"03443806964",
				"03217360729",				"03365242081",
				"03462640132",				"03453068115",
				"03332367695",				"03312546731",
				"03072748378",				"03463157106",
				"03332099878",				"03458070629",
				"03453864437",				"03002298248",
				"03452413369",				"03362036343",
				"03332277282",				"03006400070",
				"03322379568",				"03009207511",
				"03323671557",				"03312767917",
				"03215929331",				"03003111655",				
				"03002824091",
				"03312052204",				"03343729341",
				"03343729341",				"03212064420",
				"03222215641",				"03332258542",
				"03212620916",				"03332655132",
				"03122728374",				"03003672046",
				"03003388934",				"03332166518",
				"03454245789",				"03342979516",
				"03453491528",				"03464937410",
				"03452357578",				"03333082461",
				"03327377551",				"03343123417",
				"03022865046",				"03002519149",
				"03334510045",				"03002422400",
				"03322322511",				"03213369214",
				"03142482633",				"03326880632",
				"03462139600",				"03443268121",
				"03219113045",				"03333744743",
				"03422360439",				"03062133081",
				"03212317535",				"03002760312",
				"03452807273",				"03342790249",
				"03212025967",				"03003214606",
				"03232170290",				"03453219603",
				"03432243632",				"03367018682",
				"03343066454",				"03322296650",
				"03342375309",				"03022117721",
				"03053502659",				"03462730184",
				"03456166708",				"03068976363",
				"03463199739",				"03442094118",
				"03333839495",				"03412258473",
				"03153093445",				"03433095848"
};
		String[] valuesinvalid=new String[]{
				"123-5","12.45", "1 3 5", "1  45", "1   5", "0323717232","02138456679",
				"a b  ca  b  c  d", "a     ", "111.22", "222.33",
				"333.44","04058456679","034330958480","0343309584","34330958480"
				,"003433095848","034330958480","9234330958480","+9234330958480"
				,"9223433095848","++34330958480","+0934330958480","0034330958480"};

		for (String string : valuesvalid) {
			assertTrue("vaild not validated:"+string,
					DataValidation.validate(REG_EX.CELL_NUMBER, string));
		}
		for (String string : valuesinvalid) {
			assertFalse("invalid validated:"+string,
					DataValidation.validate(REG_EX.CELL_NUMBER, string));
		}
	}
	
	/**
	 * Test email.
	 */
	@Test
	public void testEMAIL(){
		
	}
	
	/**
	 * Test nam e_ characters.
	 */
	@Test
	public void testNAME_CHARACTERS(){
		
	}
	
	/**
	 * Test n o_ specia l_ char.
	 */
	@Test
	public void testNO_SPECIAL_CHAR(){
		
	}
	
	/**
	 * Test numeric.
	 */
	@Test
	public void testNUMERIC(){
		
	}
	
	/**
	 * Test password.
	 */
	@Test
	public void testPASSWORD(){
		
	}
	
	/**
	 * Test phon e_ number.
	 */
	@Test
	public void testPHONE_NUMBER(){
		
	}
	
	/**
	 * Test ptc l_ number.
	 */
	@Test
	public void testPTCL_NUMBER(){
		
	}
	
	/**
	 * Test whitespac e_ word.
	 */
	@Test
	public void testWHITESPACE_WORD(){
		
	}
	
	/**
	 * Test whitespac e_ alpha.
	 */
	@Test
	public void testWHITESPACE_ALPHA(){
		
	}
	
	/**
	 * Test whitespac e_ numeric.
	 */
	@Test
	public void testWHITESPACE_NUMERIC(){
		
	}
	
	/**
	 * Test whitespac e_ alph a_ numeric.
	 */
	@Test
	public void testWHITESPACE_ALPHA_NUMERIC(){
		
	}
	
	/**
	 * Test word.
	 */
	@Test
	public void testWORD(){
		
	}
	
}
