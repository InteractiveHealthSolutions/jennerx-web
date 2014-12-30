package org.ird.unfepi;

import java.util.Date;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ChildResponse;
import org.junit.Test;

public class ResponseTest {

	@Test
	public void responseTest(){
		ChildResponse c = new ChildResponse();
		
		c.setOriginatorCellNumber("03333333333333");
		c.setRecievedDate(new Date());
		c.setResponseText("testtetsdsdfshfag");
		c.setVaccinationRecordNum(2);
		
		ServiceContext sc = Context.getServices();
		sc.getResponseService().saveChildResponseRecord(c);
		sc.commitTransaction();
	}
}
