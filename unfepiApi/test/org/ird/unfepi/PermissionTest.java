package org.ird.unfepi;

import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Permission;
import org.ird.unfepi.model.Role;
import org.junit.Test;

public class PermissionTest {

	@Test
	public void addPermissionsToRole() throws InstanceAlreadyExistsException{
		Context.instantiate(null);
		ServiceContext sc = Context.getServices();
		int ln = SystemPermissions.values().length;
		for (SystemPermissions pr : SystemPermissions.values()) {
			if(sc.getUserService().getPermission(pr.name(), true) == null){
			Permission p = new Permission(pr.name());
			sc.getCustomQueryService().save(p);
			}
		}
		List<Permission> pl = sc.getUserService().getAllPermissions(false);
		Role r = sc.getUserService().getRole("admin", false, null);
		r.addPermissions(pl);
		sc.getUserService().updateRole(r);
		sc.commitTransaction();
		sc.closeSession();
	}
}
