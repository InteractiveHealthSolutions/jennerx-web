package org.ird.unfepi;

import org.junit.Test;

public class DemographicServiceTest {

/*	@Test
	public void ethnicityTest(){
		ServiceContext sc = Context.getServices();
		Ethnicity e1= new Ethnicity();
		e1.setEthnicityName("Punjabi");
		Ethnicity e2= new Ethnicity();
		e2.setEthnicityName("Barohi");
		Ethnicity e3= new Ethnicity();
		e3.setEthnicityName("Seestani");
		Ethnicity e4= new Ethnicity();
		e4.setEthnicityName("Makrani");
		Ethnicity e5= new Ethnicity();
		e5.setEthnicityName("Baloch");
		
		sc.getCustomQueryService().save(e1);
		sc.getCustomQueryService().save(e2);
		sc.getCustomQueryService().save(e3);
		sc.getCustomQueryService().save(e4);
		sc.getCustomQueryService().save(e5);

		sc.commitTransaction();
		
		List<Ethnicity> list = sc.getDemographicDetailsService().getAllEthnicity();
		assertTrue("saved object not found", list.size() >= 5);
		
		assertTrue("name not found", sc.getDemographicDetailsService().getEthnicityByName("punjabi").getEthnicityName().equals("Punjabi"));
	}
	
	@Test
	public void relationshipTest(){
		ServiceContext sc = Context.getServices();
		Relationship e1= new Relationship();
		e1.setRelationName("Mother");
		Relationship e2= new Relationship();
		e2.setRelationName("Father");
		Relationship e3= new Relationship();
		e3.setRelationName("Sister");
		Relationship e4= new Relationship();
		e4.setRelationName("Brother");
		Relationship e5= new Relationship();
		e5.setRelationName("Uncle");
		
		sc.getCustomQueryService().save(e1);
		sc.getCustomQueryService().save(e2);
		sc.getCustomQueryService().save(e3);
		sc.getCustomQueryService().save(e4);
		sc.getCustomQueryService().save(e5);

		sc.commitTransaction();
		
		List<Relationship> list = sc.getDemographicDetailsService().getAllRelationship();
		assertTrue("saved object not found", list.size() >= 5);
		
		assertTrue("name not found", sc.getDemographicDetailsService().getRelationshipByName("sister").getRelationName().equals("Sister"));
	}
	@Test
	public void ReligionTest(){
		ServiceContext sc = Context.getServices();
		Religion e1= new Religion();
		e1.setReligionName("Islam");
		Religion e2= new Religion();
		e2.setReligionName("Christanity");
		Religion e3= new Religion();
		e3.setReligionName("Jewism");
		Religion e4= new Religion();
		e4.setReligionName("Sikhism");
		Religion e5= new Religion();
		e5.setReligionName("Buddhism");
		
		sc.getCustomQueryService().save(e1);
		sc.getCustomQueryService().save(e2);
		sc.getCustomQueryService().save(e3);
		sc.getCustomQueryService().save(e4);
		sc.getCustomQueryService().save(e5);
		
		sc.commitTransaction();
		
		List<Religion> list = sc.getDemographicDetailsService().getAllReligion();
		assertTrue("saved object not found", list.size() >= 5);
		
		assertTrue("name not found", sc.getDemographicDetailsService().getReligionByName("islam").getReligionName().equals("Islam"));
	}
	
*/	

	@Test
	public void personTest(){
		/*ServiceContext sc = Context.getServices();
		
		long id = 100220112343L;
		Person per = new Person();
		per.setDomicile("domicile");
		per.setEthnicity(sc.getDemographicDetailsService().getEthnicityByName("punjabi"));
		per.setGender(Gender.MALE);
		per.setGuardianRelation(sc.getDemographicDetailsService().getRelationshipByName("mother"));
		per.setPersonId(id);
		per.setReligion(sc.getDemographicDetailsService().getReligionByName("islam"));
		
		assertEquals(per, sc.getDemographicDetailsService().findPersonById(Long.parseLong(sc.getDemographicDetailsService().savePerson(per).toString())));
	
		Child c = new Child();
		c.setBirthdate(new Date());
		c.setChildId(id);
		c.setFirstName("firstName");
		c.setStatus(STATUS.FOLLOW_UP);
		c.setCreator(new User(271219733, "lfdkfjdsk", "dfmdmf,s"));
		c.setDateEnrolled(new Date());
		c.setArm(Globals.DEFAULT_ARM);
		Child ch = null;
		try {
			ch = sc.getChildService().getChildbyChildId(Long.parseLong(sc.getChildService().saveChild(c, null, false, null).toString()), true, false, false);
			sc.commitTransaction();

			assertEquals(c, ch);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ChildDataException e) {
			e.printStackTrace();
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChildDataInconsistencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Child chil = sc.getChildService().getChildbyChildId(id, true, true, true);
			//ch.getAddresses();
		} catch (ChildDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}
