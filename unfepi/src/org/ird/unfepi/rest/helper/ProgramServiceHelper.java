package org.ird.unfepi.rest.helper;

import java.util.List;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.HealthProgram;

public class ProgramServiceHelper {
	
	public static List<String> getAllHealthPrograms(){
		
		ServiceContext sc = Context.getServices();
		
//		List<HealthProgram> programL = sc.getCustomQueryService().getDataByHQL("from HealthProgram");
		
		List<String> list = sc.getCustomQueryService().getDataByHQL("select name from HealthProgram");
		
//		for (HealthProgram hp : programL) {
//			System.out.println(hp.getName());
//		}
		
//		for (String string : list) {
//			System.out.println(string);
//		}
		return list;
	}

}
