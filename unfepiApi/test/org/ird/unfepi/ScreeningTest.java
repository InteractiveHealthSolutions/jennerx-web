package org.ird.unfepi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.VaccinationCenterVaccineDayId;
import org.junit.Test;

public class ScreeningTest {

	
	
/*	@Test
	public void testVaccinationCenter(){
		for (int ind = 1; ind < 10; ind++) {
			
		
		ServiceContext sc = Context.getServices();
		
		IdMapper id = new IdMapper(0, "0100"+ind);
		id.setRole(sc.getUserService().getRole("vaccination_center", false, null));
		Serializable i = sc.getIdMapperService().saveIdMapper(id);
		id.setMappedId(Integer.parseInt(i.toString()));
		
		ContactNumber c = new ContactNumber();
		c.setCreatedByUserId(sc.getUserService().findUser("100"));
		c.setCreatedDate(new Date());
		c.setMappedId(Integer.parseInt(i.toString()));
		c.setNumber("0334567"+ind+""+ind+""+ind+""+ind);
		c.setNumberType(ContactType.PRIMARY);
		c.setOwnerFirstName("korinab khan");
		c.setOwnerRelationId(sc.getDemographicDetailsService().getRelationshipByName("other").getRelationshipId());
		c.setOtherOwnerRelation("manager".toUpperCase());

		try {
			sc.getDemographicDetailsService().saveUniqueContactNumber(c);
		} catch (ChildDataInconsistencyException e1) {
			e1.printStackTrace();
		}
		
		
		Address a = new Address();
		a.setAddArea("malir");
		a.setAddColony("colony");
		a.setAddressType(ContactType.PRIMARY);
		a.setCreatedByUserId(sc.getUserService().findUser("100"));
		a.setCreatedDate(new Date());
		a.setMappedId(Integer.parseInt(i.toString()));
		sc.getDemographicDetailsService().saveAddress(a);
		
		VaccinationCenter v = new VaccinationCenter(Integer.parseInt(i.toString()));
		v.setCreatedByUserId(new User(sc.getUserService().findUser("100").getMappedId()));
		v.setCreatedDate(new Date());
		v.setDateRegistered(new Date());
		v.setFullName("VaccinationCenter"+ind);
		v.setName("vcenter2"+ind);
		
		Serializable vid = sc.getVaccinationService().saveVaccinationCenter(v);
		
		sc.commitTransaction();
		sc.closeSession();
		}
	}
*/
    @Test
	public void vaccinationCenterDaysTest(){
		try{
		ServiceContext sc = Context.getServices();
		
		List<VaccinationCenterVaccineDay> list2 = sc.getVaccinationService().getAllVaccinationCenterVaccineDay(false);
		for (int i = 0; i < list2.size(); i++) {
			sc.getVaccinationService().deleteVaccinationCenterVaccineDay(list2.get(i));
		}
		
		sc.commitTransaction();
		sc.closeSession();
		
		sc = Context.getServices();
		
		List<VaccinationCenter> list = sc.getVaccinationService().getAllVaccinationCenter(false, null);
		
		
		
		//BCG
		for (VaccinationCenter v : list) 
		{
			Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();

			VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
			vcd.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("bcg").getVaccineId(),sc.getVaccinationService().getByFullName("monday").getDayNumber()));
			
			av.add(vcd);
			
			VaccinationCenterVaccineDay vcd1 = new VaccinationCenterVaccineDay();
			vcd1.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("bcg").getVaccineId(),sc.getVaccinationService().getByFullName("tuesday").getDayNumber()));
			
			av.add(vcd1);
			
			VaccinationCenterVaccineDay vcd2 = new VaccinationCenterVaccineDay();
			vcd2.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("bcg").getVaccineId(),sc.getVaccinationService().getByFullName("wednesday").getDayNumber()));
			
			av.add(vcd2);
			
			VaccinationCenterVaccineDay vcd3 = new VaccinationCenterVaccineDay();
			vcd3.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("bcg").getVaccineId(),sc.getVaccinationService().getByFullName("thursday").getDayNumber()));
			
			av.add(vcd3);
			
			VaccinationCenterVaccineDay vcd4 = new VaccinationCenterVaccineDay();
			vcd4.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("bcg").getVaccineId(),sc.getVaccinationService().getByFullName("friday").getDayNumber()));
			
			av.add(vcd4);
			
			VaccinationCenterVaccineDay vcd5 = new VaccinationCenterVaccineDay();
			vcd5.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("bcg").getVaccineId(),sc.getVaccinationService().getByFullName("saturday").getDayNumber()));
			
			av.add(vcd5);
			
			for (VaccinationCenterVaccineDay vcdli : av) {
				sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
			}
		}
		
		sc.commitTransaction();

		//Penta1
		for (VaccinationCenter v : list) 
		{
			Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();

			VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
			vcd.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta1").getVaccineId(),sc.getVaccinationService().getByFullName("monday").getDayNumber()));
			
			av.add(vcd);
			
			VaccinationCenterVaccineDay vcd1 = new VaccinationCenterVaccineDay();
			vcd1.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta1").getVaccineId(),sc.getVaccinationService().getByFullName("tuesday").getDayNumber()));
			
			av.add(vcd1);
			
			VaccinationCenterVaccineDay vcd2 = new VaccinationCenterVaccineDay();
			vcd2.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta1").getVaccineId(),sc.getVaccinationService().getByFullName("wednesday").getDayNumber()));
			
			av.add(vcd2);
			
			VaccinationCenterVaccineDay vcd3 = new VaccinationCenterVaccineDay();
			vcd3.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta1").getVaccineId(),sc.getVaccinationService().getByFullName("thursday").getDayNumber()));
			
			av.add(vcd3);
			
			VaccinationCenterVaccineDay vcd4 = new VaccinationCenterVaccineDay();
			vcd4.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta1").getVaccineId(),sc.getVaccinationService().getByFullName("friday").getDayNumber()));
			
			av.add(vcd4);
			
			VaccinationCenterVaccineDay vcd5 = new VaccinationCenterVaccineDay();
			vcd5.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta1").getVaccineId(),sc.getVaccinationService().getByFullName("saturday").getDayNumber()));
			
			av.add(vcd5);
			
			for (VaccinationCenterVaccineDay vcdli : av) {
				sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
			}
		}
		
		sc.commitTransaction();

		//Penta2
		for (VaccinationCenter v : list) 
		{
			Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();

			VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
			vcd.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta2").getVaccineId(),sc.getVaccinationService().getByFullName("monday").getDayNumber()));
			
			av.add(vcd);
			
			VaccinationCenterVaccineDay vcd1 = new VaccinationCenterVaccineDay();
			vcd1.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta2").getVaccineId(),sc.getVaccinationService().getByFullName("tuesday").getDayNumber()));
			
			av.add(vcd1);
			
			VaccinationCenterVaccineDay vcd2 = new VaccinationCenterVaccineDay();
			vcd2.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta2").getVaccineId(),sc.getVaccinationService().getByFullName("wednesday").getDayNumber()));
			
			av.add(vcd2);
			
			VaccinationCenterVaccineDay vcd3 = new VaccinationCenterVaccineDay();
			vcd3.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta2").getVaccineId(),sc.getVaccinationService().getByFullName("thursday").getDayNumber()));
			
			av.add(vcd3);
			
			VaccinationCenterVaccineDay vcd4 = new VaccinationCenterVaccineDay();
			vcd4.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta2").getVaccineId(),sc.getVaccinationService().getByFullName("friday").getDayNumber()));
			
			av.add(vcd4);
			
			VaccinationCenterVaccineDay vcd5 = new VaccinationCenterVaccineDay();
			vcd5.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta2").getVaccineId(),sc.getVaccinationService().getByFullName("saturday").getDayNumber()));
			
			av.add(vcd5);
			
			for (VaccinationCenterVaccineDay vcdli : av) {
				sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
			}
		}
		
		sc.commitTransaction();

		//Penta3
		for (VaccinationCenter v : list) 
		{
			Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();

			VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
			vcd.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta3").getVaccineId(),sc.getVaccinationService().getByFullName("monday").getDayNumber()));
			
			av.add(vcd);
			
			VaccinationCenterVaccineDay vcd1 = new VaccinationCenterVaccineDay();
			vcd1.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta3").getVaccineId(),sc.getVaccinationService().getByFullName("tuesday").getDayNumber()));
			
			av.add(vcd1);
			
			VaccinationCenterVaccineDay vcd2 = new VaccinationCenterVaccineDay();
			vcd2.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta3").getVaccineId(),sc.getVaccinationService().getByFullName("wednesday").getDayNumber()));
			
			av.add(vcd2);
			
			VaccinationCenterVaccineDay vcd3 = new VaccinationCenterVaccineDay();
			vcd3.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta3").getVaccineId(),sc.getVaccinationService().getByFullName("thursday").getDayNumber()));
			
			av.add(vcd3);
			
			VaccinationCenterVaccineDay vcd4 = new VaccinationCenterVaccineDay();
			vcd4.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta3").getVaccineId(),sc.getVaccinationService().getByFullName("friday").getDayNumber()));
			
			av.add(vcd4);
			
			VaccinationCenterVaccineDay vcd5 = new VaccinationCenterVaccineDay();
			vcd5.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("penta3").getVaccineId(),sc.getVaccinationService().getByFullName("saturday").getDayNumber()));
			
			av.add(vcd5);
			
			for (VaccinationCenterVaccineDay vcdli : av) {
				sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
			}
		}
		
		sc.commitTransaction();

		//Measles1
		for (VaccinationCenter v : list) 
		{
			Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();

			VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
			vcd.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles1").getVaccineId(),sc.getVaccinationService().getByFullName("monday").getDayNumber()));
			
			av.add(vcd);
			
			VaccinationCenterVaccineDay vcd1 = new VaccinationCenterVaccineDay();
			vcd1.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles1").getVaccineId(),sc.getVaccinationService().getByFullName("tuesday").getDayNumber()));
			
			av.add(vcd1);
			
			VaccinationCenterVaccineDay vcd2 = new VaccinationCenterVaccineDay();
			vcd2.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles1").getVaccineId(),sc.getVaccinationService().getByFullName("wednesday").getDayNumber()));
			
			av.add(vcd2);
			
			VaccinationCenterVaccineDay vcd3 = new VaccinationCenterVaccineDay();
			vcd3.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles1").getVaccineId(),sc.getVaccinationService().getByFullName("thursday").getDayNumber()));
			
			av.add(vcd3);
			
			VaccinationCenterVaccineDay vcd4 = new VaccinationCenterVaccineDay();
			vcd4.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles1").getVaccineId(),sc.getVaccinationService().getByFullName("friday").getDayNumber()));
			
			av.add(vcd4);
			
			VaccinationCenterVaccineDay vcd5 = new VaccinationCenterVaccineDay();
			vcd5.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles1").getVaccineId(),sc.getVaccinationService().getByFullName("saturday").getDayNumber()));
			
			av.add(vcd5);
			
			for (VaccinationCenterVaccineDay vcdli : av) {
				sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
			}
		}
		
		sc.commitTransaction();

		//Measles2
		for (VaccinationCenter v : list) 
		{
			Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();

			VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
			vcd.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles2").getVaccineId(),sc.getVaccinationService().getByFullName("monday").getDayNumber()));
			
			av.add(vcd);
			
			VaccinationCenterVaccineDay vcd1 = new VaccinationCenterVaccineDay();
			vcd1.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles2").getVaccineId(),sc.getVaccinationService().getByFullName("tuesday").getDayNumber()));
			
			av.add(vcd1);
			
			VaccinationCenterVaccineDay vcd2 = new VaccinationCenterVaccineDay();
			vcd2.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles2").getVaccineId(),sc.getVaccinationService().getByFullName("wednesday").getDayNumber()));
			
			av.add(vcd2);
			
			VaccinationCenterVaccineDay vcd3 = new VaccinationCenterVaccineDay();
			vcd3.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles2").getVaccineId(),sc.getVaccinationService().getByFullName("thursday").getDayNumber()));
			
			av.add(vcd3);
			
			VaccinationCenterVaccineDay vcd4 = new VaccinationCenterVaccineDay();
			vcd4.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles2").getVaccineId(),sc.getVaccinationService().getByFullName("friday").getDayNumber()));
			
			av.add(vcd4);
			
			VaccinationCenterVaccineDay vcd5 = new VaccinationCenterVaccineDay();
			vcd5.setId(new VaccinationCenterVaccineDayId(v.getMappedId(), sc.getVaccinationService().getByName("measles2").getVaccineId(),sc.getVaccinationService().getByFullName("saturday").getDayNumber()));
			
			av.add(vcd5);
			
			for (VaccinationCenterVaccineDay vcdli : av) {
				sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
			}
		}
		
		sc.commitTransaction();

		sc.closeSession();
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

    /*	@Test
	public void testVaccinationCenterAppr2(){
		ServiceContext sc = Context.getServices();
		IdMapper id = new IdMapper(0, "01016");
		id.setRole(sc.getUserService().getRole("vaccination_center"));
		Serializable i = sc.getIdMapperService().saveIdMapper(id);
		id.setMappedId(Integer.parseInt(i.toString()));
		
		ContactNumber c = new ContactNumber();
		try {
			c.setCreatedByUserId(sc.getUserService().getUser("admin"));
		} catch (UserServiceException e) {
			
			e.printStackTrace();
		}
		c.setCreatedDate(new Date());
		c.setIdMapper(id);
		c.setNumber("03345645454");
		c.setNumberType(ContactType.PRIMARY);
		c.setOwnerName("shahaab");
		c.setOwnerRelation(sc.getDemographicDetailsService().getRelationshipByName("unknown"));
		try {
			sc.getDemographicDetailsService().saveUniqueContactNumber(c);
		} catch (ChildDataInconsistencyException e1) {
			e1.printStackTrace();
		}
		
		Address a = new Address();
		a.setAddArea("addArea");
		a.setAddressType(ContactType.PRIMARY);
		a.setCreatedByUserId(sc.getUserService().findUser("100"));
		a.setCreatedDate(new Date());
		a.setMappedId(Integer.parseInt(i.toString()));
		sc.getDemographicDetailsService().saveAddress(a);
		
		VaccinationCenter v = new VaccinationCenter(Integer.parseInt(i.toString()));
		v.setCreatedByUserId(new User(sc.getUserService().findUser("100").getMappedId()));
		v.setCreatedDate(new Date());


		v.setFullName("VaccinationCenter6");
		v.setName("vc6");
		Serializable vid = sc.getVaccinationService().saveVaccinationCenter(v);

		VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
		vcd.setId(new VaccinationCenterVaccineDayId(Integer.parseInt(vid.toString()), sc.getVaccinationService().getByName("bcg/opv").getVaccineId(),sc.getVaccinationService().getByFullName("monday").getDayNumber()));
		
		VaccinationCenterVaccineDay vcd1 = new VaccinationCenterVaccineDay();
		vcd1.setId(new VaccinationCenterVaccineDayId(Integer.parseInt(vid.toString()), sc.getVaccinationService().getByName("penta1/opv").getVaccineId(),sc.getVaccinationService().getByFullName("tuesday").getDayNumber()));
	
		VaccinationCenterVaccineDay vcd4 = new VaccinationCenterVaccineDay();
		vcd4.setId(new VaccinationCenterVaccineDayId(Integer.parseInt(vid.toString()), sc.getVaccinationService().getByName("penta1/opv").getVaccineId(),sc.getVaccinationService().getByFullName("wednesday").getDayNumber()));

		VaccinationCenterVaccineDay vcd2 = new VaccinationCenterVaccineDay();
		vcd2.setId(new VaccinationCenterVaccineDayId(Integer.parseInt(vid.toString()), sc.getVaccinationService().getByName("penta1/opv").getVaccineId(),sc.getVaccinationService().getByFullName("thursday").getDayNumber()));
		
		VaccinationCenterVaccineDay vcd3 = new VaccinationCenterVaccineDay();
		vcd3.setId(new VaccinationCenterVaccineDayId(Integer.parseInt(vid.toString()), sc.getVaccinationService().getByName("measles1").getVaccineId(),sc.getVaccinationService().getByFullName("friday").getDayNumber()));
		
		sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcd);
		sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcd1);
		sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcd2);
		sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcd3);
		sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcd4);
		sc.commitTransaction();
		sc.closeSession();
	}*/
	
	/*@Test
	public void testVaccinator(){
		for (int ind = 1; ind < 10; ind++) {
			
		
		ServiceContext sc = Context.getServices();
		IdMapper id = new IdMapper(0, "0100"+ind+"0"+ind);
		id.setRole(sc.getUserService().getRole("vaccinator"));
		Serializable i = sc.getIdMapperService().saveIdMapper(id);
		id.setMappedId(Integer.parseInt(i.toString()));
		
		ContactNumber c = new ContactNumber();
		c.setCreatedByUserId(sc.getUserService().findUser("100"));
		c.setCreatedDate(new Date());
		c.setMappedId(Integer.parseInt(i.toString()));
		c.setNumber("03600111"+ind+""+ind+""+ind);
		c.setNumberType(ContactType.PRIMARY);
		c.setOwnerFirstName("yalmaaz");
		c.setOwnerRelationId(sc.getDemographicDetailsService().getRelationshipByName("other").getRelationshipId());
		c.setOtherOwnerRelation("own");
		try {
			sc.getDemographicDetailsService().saveUniqueContactNumber(c);
		} catch (ChildDataInconsistencyException e1) {
			e1.printStackTrace();
		}
		
		Address a = new Address();
		a.setAddArea("addArea");
		a.setAddressType(ContactType.PRIMARY);
		a.setCreatedByUserId(sc.getUserService().findUser("100"));
		a.setCreatedDate(new Date());
		a.setMappedId(Integer.parseInt(i.toString()));
		sc.getDemographicDetailsService().saveAddress(a);

		Vaccinator v = new Vaccinator(Integer.parseInt(i.toString()));
		v.setCreatedByUserId(new User(sc.getUserService().findUser("100").getMappedId()));
		v.setCreatedDate(new Date());
		v.setFirstName("hamzaad"+ind);
		//v.setVaccinationCenterId(vaccinationCenterId);
		sc.getVaccinationService().saveVaccinator(v);
		sc.commitTransaction();
		sc.closeSession();
		
		}
	}*/
	
	/*@Test
	public void testScreening(){
		
		ServiceContext sc = Context.getServices();
		IdMapper id = new IdMapper(0, "0101220113171");
		id.setRole(sc.getUserService().getRole("Child"));
		Serializable i = sc.getIdMapperService().saveIdMapper(id);
		
		Screening scr = new Screening();
		scr.setCellNumber("03348585858");
		scr.setCellNumberOwnerFirstName("abcdefgh");
		scr.setCellNumberOwnerLastName("xyz");
		scr.setEpiId("20113221");
		scr.setHasApprovedLottery(true);
		scr.setHasCellPhoneAccess(true);
		//scr.setIdMapper(idMapper);
		scr.setIsChildHealthy(true);
		scr.setLivingDuration((byte) 9);
		scr.setMappedId(Integer.parseInt(i.toString()));
		scr.setVaccinationCenter(new VaccinationCenter(sc.getIdMapperService().findIdMapper("01014").getMappedId()));
		scr.setVaccinator(new Vaccinator(sc.getIdMapperService().findIdMapper("0101201").getMappedId()));
		scr.setScreeningDate(new Date());
		
		try {
			sc.getChildService().saveScreening(scr);
		} catch (ChildDataInconsistencyException e) {
			
			e.printStackTrace();
		}
		sc.commitTransaction();
		sc.closeSession();
	}*/
}
