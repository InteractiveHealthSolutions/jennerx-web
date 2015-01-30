package org.ird.unfepi;

import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.User;
import org.ird.unfepi.utils.SecurityUtils;
import org.junit.Test;

public class testTest {

	@Test
	public void addPermissionsToRole() throws InstanceAlreadyExistsException{
		Context.instantiate(null);
		ServiceContext sc = Context.getServices();
		sc.beginTransaction();
		sc.flushSession();
		sc.commitTransaction();
		List idm = sc.getCustomQueryService().getDataBySQL("select mappedId, programId from idmapper where roleid=4");
		for (Object pid : idm) {
			Object[] o = (Object[]) pid;
			User u = sc.getUserService().findUser(Integer.parseInt(o[0].toString()));
			try {
				System.out.println("ID:"+o[1]+"; username:"+u.getUsername()+"; pw:"+SecurityUtils.decrypt(u.getPassword(), u.getUsername())+"; Name: "+u.getFullName()+"; ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*List<User> l = sc.getUserService().getAllUser(0, 100, true, false, null);
		for (User user : l) {
			try {
				System.out.println(user.getUsername()+":"+SecurityUtils.decrypt(user.getPassword(), user.getUsername()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		//sc.commitTransaction();
		sc.closeSession();
	}
}
