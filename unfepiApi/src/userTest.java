import java.util.Calendar;


public class userTest {

	public static void main(String[] args) {
		float f = (float) 2000.12;
		System.out.println((int)f);
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.HOUR_OF_DAY, 24);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		
		System.out.println(cal1.getTime());
		System.out.println(cal2.getTime());
		/*Permission per = new Permission("VIEW_CHILDREN_DATA");
		ServiceContext sc = Context.getServices();
		sc.getCustomQueryService().save(per);
		
		Role role = new Role("Admin");
		role.addPermission(per);
		
		try {
			sc.getUserService().addRole(role);
		} catch (UserServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		User user = new User(111, "administrator");
		user.setClearTextPassword("123456");
		user.addRole(role);
		user.setStatus(UserStatus.ACTIVE);
		user.setFirstName("Administrator");
		try {
			sc.getUserService().createUser(user);
		} catch (UserServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sc.commitTransaction();*/
	}
}
