package org.ird.unfepi.autosys;

import junit.framework.Assert;

import org.ird.unfepi.autosys.smser.ResponseReaderJob;
import org.junit.Test;

public class EpiCertificateTest {

	@Test
	public void test() {
		String[] s = "3232::::  ".split(":");
		System.out.println(s.length);
		for (String string : s) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testEligibilityRegex(){
		String[] fail = new String[]{"VS42382"};
		String[] pass = new String[]{"vs ","VS ", "vs-", "vs:", "vs_", "vs ", "vs-78745", "vs-DFS--676",
				"vs-632847328 fkdlfda"};

		for (String string : pass) {
			Assert.assertTrue(string.matches(ResponseReaderJob.INQUIRY_ELIGIBILTIY_REGEX));
		}
		
		for (String string : fail) {
			Assert.assertFalse(string.matches(ResponseReaderJob.INQUIRY_ELIGIBILTIY_REGEX));
		}
		
	}
}
