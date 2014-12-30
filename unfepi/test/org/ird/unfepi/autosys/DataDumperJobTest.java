package org.ird.unfepi.autosys;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.autosys.reporting.DataDumperJob;
import org.ird.unfepi.context.Context;
import org.junit.Test;
import org.quartz.JobExecutionException;

public class DataDumperJobTest {

	@Test
	public void testDump() throws JobExecutionException, InstanceAlreadyExistsException {
		Context.instantiate(null);
		DataDumperJob d = new DataDumperJob();
		d.execute(null);
	}
}
