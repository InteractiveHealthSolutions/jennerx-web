/*
 * 
 */

package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.mysql.jdbc.StringUtils;

public class HibernateUtil {
	
 	private static SessionFactory sessionFactory;

	/* 	public synchronized static SessionFactory getSessionFactory() {
	        if (sessionFactory == null) {
	        	sessionFactory = new Configuration().configure().buildSessionFactory();
	        	 try {
	                MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

                    final ObjectName objectName = new ObjectName("org.hibernate:type=statistics");
                    final StatisticsService mBean = new StatisticsService();

                    mBean.setStatisticsEnabled(true);
                    mBean.setSessionFactory(sessionFactory);
                    mbeanServer.registerMBean(mBean, objectName);

	            } catch (MalformedObjectNameException e) {
	                	LoggerUtil.logIt("unable to register mbean"+ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            } catch (InstanceAlreadyExistsException e) {
	                	LoggerUtil.logIt("unable to register mbean" +ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            } catch (MBeanRegistrationException e) {
	                	LoggerUtil.logIt("unable to register mbean" +ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            } catch (NotCompliantMBeanException e) {
	                	LoggerUtil.logIt("unable to register mbean" +ExceptionUtil.getStackTrace(e));
	                    throw new RuntimeException(e);
	            }
	        }
	            return sessionFactory;
	    }*/
 	/**
 	 * Gets the sessionFactory with given Properties. 
 	 * @param properties Properties that would be used to configure hibernate. Null if should be default i.e. read from cfg.xml file
 	 * @param configFileName File where hibernate mapping are defined. Null if should be default i.e. hibernate.cfg.xml
 	 * @return
 	 */
 	public synchronized static SessionFactory getSessionFactory (Properties properties, String configFileName) {
		if (sessionFactory == null) {
			Configuration conf = new Configuration();
			
			if(properties != null){
				conf.setProperties(properties);
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(configFileName)){
				conf.configure(configFileName);
			}
			else {
				conf.configure();
			}
			
			sessionFactory = conf.buildSessionFactory();
		}
		return sessionFactory;
	}
}
