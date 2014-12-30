package org.ird.unfepi;

import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.User;
import org.ird.unfepi.utils.SecurityUtils;

public class updateusers {

	public static void main(String[] args) throws Exception {
		Context.instantiate(null);
		ServiceContext sc = Context.getServices();
		List<User> ul = sc.getUserService().getAllUser(0, 1000, false, false, null);
		
		for (User user : ul) {
//			String newname=user.getFirstName().toLowerCase();
			if(user.getIdMapper().getRole().getRolename().toLowerCase().contains("vaccinator"))
			System.out.println(user.getUsername()+":"+user.getFirstName()+"--"+SecurityUtils.decrypt(user.getPassword(), user.getUsername()));
			//user.setUsername(newname);
			//user.setClearTextPassword("hiddenword");
			try {
				//sc.getUserService().updateUser(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//sc.commitTransaction();
		sc.closeSession();
	}
}
