package org.ird.unfepi.context;

import java.util.HashMap;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ird.unfepi.model.Setting;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.dao.hibernatedimpl.HibernateUtil;
import org.ird.unfepi.service.exception.UserServiceException;
import org.ird.unfepi.utils.SecurityUtils;
import org.ird.unfepi.utils.reporting.LoggerUtil;

/** MUST be instantiated ONCE and ONLY ONCE by calling Context.instantiate
 *
 * @author maimoonak
 */
public class Context 
{
	private static Context _instance;
	private HashMap<String,Setting> currentSettings;

	private Context() {
		System.out.println("\nLoading Settings....");
		currentSettings = getAllSettings();
		System.out.println("\nSettings loaded successfully....");
	}
	
	private static SessionFactory sessionFactory;

	public static void instantiate(Properties properties) throws InstanceAlreadyExistsException{
		if(_instance != null){
			throw new InstanceAlreadyExistsException("An instance of Context already exists in system. Make sure to maintain correct flow.");
		}
		
		// session factory must have been instantiated before we could use any method involving data
		sessionFactory = HibernateUtil.getSessionFactory(properties, null);

		_instance = new Context();
	}
	
	private HashMap<String, Setting> getAllSettings() {
		HashMap<String,Setting> curSettings=new HashMap<String, Setting>();
		ServiceContext sc = Context.getServices();
		try{
			for (Setting setting : sc.getIRSettingService().getIrSettings()) {
				curSettings.put(setting.getName().trim(), setting);
			}
		}
		finally{
			sc.closeSession();
		}
		
		return curSettings;
	}
	
	public static String getSetting(String name,String defaultvalue){
		if(_instance.currentSettings.get(name)!=null){
			return _instance.currentSettings.get(name).getValue();
		}
		return defaultvalue;
	}
	
	/** Send only validated values. this function donot validate setting values.
	 *
	 * @param name the name
	 * @param newValue the new value
	 * @param user the user
	 * @return true, if successful
	 */
	public static void updateSetting(String name,String newValue,LoggedInUser user) {
		Setting setting = _instance.currentSettings.get(name.trim());
		setting.setEditor(user.getUser());
		setting.setValue(newValue.trim());
		
		ServiceContext sc = Context.getServices();

		try{
			LoggerUtil.logIt("\nUpdating Setting :"+name);
			sc.getIRSettingService().updateIrSetting(setting);
			sc.commitTransaction();
			LoggerUtil.logIt("\nSetting :"+name+" updated to :"+newValue);
		}
		finally{
			sc.closeSession();
		}
	}
	
	/** Gets the authenticated user.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the authenticated user
	 * @throws UserServiceException the user service exception
	 * @throws Exception the exception
	 */
	public static LoggedInUser getAuthenticatedUser(String username,String password) throws UserServiceException, Exception{
		ServiceContext sc = Context.getServices();

		User user=sc.getUserService().findUserByUsername(username, true, true, true, true, null);
		sc.closeSession();

		if(user == null){
			throw new UserServiceException(UserServiceException.AUTHENTICATION_EXCEPTION);
		}

		if(password.compareTo(SecurityUtils.decrypt(user.getPassword(),user.getUsername()))!= 0 ){
			throw new UserServiceException(UserServiceException.AUTHENTICATION_EXCEPTION);
		}
		if(user.getStatus() == User.UserStatus.DISABLED){
			throw new UserServiceException(UserServiceException.ACCOUNT_DISABLED);
		}
		return new LoggedInUser(user);
	}
	
	/*public static Statistics getStatistics(){
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true);
		return stats;
	}*/
	
	/** Before calling this method make sure that Context has been instantiated ONCE and ONLY ONCE by calling {@linkplain Context#instantiate} method
	 */
	public static Session getNewSession() {
		return sessionFactory.openSession();
	}
	
	/** Before calling this method make sure that Context has been instantiated ONCE and ONLY ONCE by calling {@linkplain Context#instantiate} method
	 *  
	 * NOTE: For assurance of prevention from synchronization and consistency be sure to get new ServiceContext Object
	 * for each bulk or batch of transactions. i.e Using single object for whole application may produce undesired results
	 *
	 * @return the services
	 */
	public static ServiceContext getServices(){
		return new ServiceContext(sessionFactory);
	}
}
