package org.ird.unfepi;

import java.io.Serializable;
import java.util.Date;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;
import org.ird.unfepi.service.exception.UserServiceException;
import org.junit.Test;

public class UserTest {

	@Test
	public void testPwd() throws UserServiceException, Exception{
		Context.instantiate(null);
		Context.getAuthenticatedUser("administrator", "Admin123");
	}
	//@Test
	public void userTest()
	{
		String[] users = new String[]{/*"administrator","saira","tariq","aamir","ali","hamidah","saba","fahad","vijay","subhash"*/};
		ServiceContext sc = Context.getServices();
		
/*		int ind=0;
		for (String string : users) {*/
			IdMapper id = new IdMapper();
			id.setRoleId(sc.getUserService().getRole("ADMIN", false, null).getRoleId());
			Serializable i = sc.getIdMapperService().saveIdMapper(id);
			
			User u = new User();
			u.setClearTextPassword("123456");
			u.setFirstName(/*string*/"Ali");
			u.setLastName("Habib");
			u.setEmail("ali.habib@irdresearch.org");
			u.setMappedId(Integer.parseInt(i.toString()));
			u.setStatus(UserStatus.ACTIVE);
			u.setUsername(/*string*/"alih");
			u.setCreatedByUserId(new User(u.getMappedId()));
			u.setCreatedDate(new Date());
			
			try {
				sc.getUserService().createUser(u);
			} catch (UserServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*ind++;
		}*/
		
		sc.commitTransaction();
		sc.closeSession();
	}
}
