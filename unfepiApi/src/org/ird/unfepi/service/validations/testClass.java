package org.ird.unfepi.service.validations;
/*package ird.xoutTB.dataValidation;
import ird.xoutTB.db.entity.Message;
import ird.xoutTB.db.entity.MessageCategory;
import ird.xoutTB.db.entity.Child;
import ird.xoutTB.db.entity.ChildCell;
import ird.xoutTB.db.entity.Response;
import ird.xoutTB.db.entity.Permission;
import ird.xoutTB.db.entity.Reminder;
import ird.xoutTB.db.entity.User;
import ird.xoutTB.db.hibernate.dao.HibernateDAOMessage;
import ird.xoutTB.db.hibernate.dao.HibernateDAOMessageCategory;
import ird.xoutTB.db.hibernate.dao.HibernateDAOChild;
import ird.xoutTB.db.hibernate.dao.HibernateDAOChildCell;
import ird.xoutTB.db.hibernate.dao.HibernateDAOReminder;
import ird.xoutTB.db.hibernate.dao.HibernateDAOUser;
import ird.xoutTB.db.hibernate.dao.HibernateUtil;
import ird.xoutTB.reporting.exceptions.ChildDataException;
import ird.xoutTB.reporting.exceptions.ReminderException;
import ird.xoutTB.service.serviceImpl.ChildServiceImpl;

import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class testClass {

	
	
	
	*//**
	 * @param args
	 * @throws NamingException 
	 * @throws SystemException 
	 * @throws NotSupportedException 
	 *//*
	public static void main(String[] args) throws NamingException, NotSupportedException, SystemException {
		// TODO Auto-generated method stub
		SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
		sessionFactory.getCurrentSession().beginTransaction();
		Date date=new Date();
		HibernateDAOChild pd=new HibernateDAOChild(sessionFactory);
		HibernateDAOReminder r=new HibernateDAOReminder(sessionFactory);
		HibernateDAOChildCell pc=new HibernateDAOChildCell(sessionFactory);
			
		ChildServiceImpl ps=new ChildServiceImpl(pd,r,pc);
		
	Child p;
	try {
		p = ps.getChildById("1");
			p.getStreetNum();

	} catch (ChildDataException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		CellHistory cell=new CellHistory();
		cell.setCellNo("55555555544");
			Child p=new Child();
			p.setChildId("5");
			p.setFirstName("fi");
			p.setMiddleName("v");
			p.setLastName("e");
			p.addCellNumber(cell);
			//p.addReminder(reminder);
			
		try {
	ps.createChild(p);
} catch (ChildDataException e) {
	System.out.println(e.errorMessage());
	//e.printStackTrace();
}
					
		Session session=null;
		SessionFactory sessionFactory=new Configuration().configure().buildSessionFactory();
		//session=HibernateUtil.getSessionFactory().openSession();
		//UserTransaction tx = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
		session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
		//transaction.begin();
								HibernateDAOMessage h=new HibernateDAOMessage();
								
							HibernateDAOChild p=new HibernateDAOChild();
							//p.setSessionFactory(sessionFactory);
							Child pat= new Child();
							pat.setChildId("testP1");
							pat.setFirstName("child1111");
							//p.addChild(pat);
								session.save(pat);								
								Query q=session.createQuery("from Message");
								List<Message> me=q.list();
								
									
								for (Message m : me) {
									
								
									System.out.println(m.getMessageId());
									System.out.println(m.getMessageName());
									System.out.println(m.getMessageText());
								}
								transaction.commit();
								
								session.close();
							
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		HibernateDAOUser h=new HibernateDAOUser();
		h.setSessionFactory(HibernateUtil.getSessionFactory());
		List<Permission> p=h.getAllPermission();
		
		//m.setCategoryId(1);
	
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
								HibernateDAOMessage h=new HibernateDAOMessage();
										h.setSessionFactory(HibernateUtil.getSessionFactory());
										
										
										Message msg=new Message();
										msg.setMessageId(4);
										msg.setDateAdded(new Date());
										msg.setLastUpdated(new Date());
										msg.setMessageDescription("77777777");
										msg.setMessageName("77777777");
										msg.setMessageText("hsjdhsjhdjsagfjhdjfgdgfdhjg");
										h.updateMessage(msg);
									
										
								HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
										
										//HibernateDAOMessage h=new HibernateDAOMessage();
										//h.setSessionFactory(HibernateUtil.getSessionFactory());
										
										
										Message msg2=new Message();
										msg.setMessageId(5);
										msg.setDateAdded(new Date());
										msg.setLastUpdated(new Date());
										msg.setMessageDescription("888888  ");
										msg.setMessageName("88888888");
										msg.setMessageText("hsjdhsjhdjsagfjhdjfgdgfdhjg");
										h.updateMessage(msg);
									
										HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
										
								HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
										
										//HibernateDAOMessage h=new HibernateDAOMessage();
										//h.setSessionFactory(HibernateUtil.getSessionFactory());
										
										
										Message msg3=new Message();
										msg.setMessageId(6);
										msg.setDateAdded(new Date());
										msg.setLastUpdated(new Date());
										msg.setMessageDescription("999999");
										msg.setMessageName("999999999");
										msg.setMessageText("hsjdhsjhdjsagfjhdjfgdgfdhjg");
										h.updateMessage(msg);
									
										HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
										
		//transaction.commit();
		//session.close();
		session=sessionFactory.openSession();
		Transaction transaction=session.beginTransaction();
				
		
		//Query query=session.createQuery("from empMap empPerInfo");//or
		//Query query=session.createQuery("from empMap as empPerInfo");//orr
		Query query=session.createQuery("from Child");
		
		System.out.println("executing query");
		//this query not working
		//Query query=session.createQuery("select e.* from empMap as e");
		
		Query query=session.createQuery("from empMap empPerInfo where empDesig=?");
		query.setString(0, "architecht");
		
		
		List<Child> lemp=query.list();
		System.out.println("obtained list query");
		
		for (Child e : lemp) {
			//CellHistory p=new CellHistory(e.getChildId(),"0dfsdfdsf11");
			System.out.print(e.getChildId()+"   ");
			System.out.print(e.getFirstName()+"   ");
			System.out.println(e.getCurrentlyUnderTreatment()+"   ");
			//System.out.print(e.getReminders().iterator().next()+"   ");
			//e.changeCellNum(p);
			
			//System.out.print(e.getAllCellNums().iterator().next().getCellNo());
			//System.out.print(e.getBirthdate());
			//session.save(p);
		//	session.save(e);
			//System.out.print(e.getEmpDesig()+"   ");
			//System.out.print(e.getEmpName()+"   ");
			//System.out.println(e.getDeptID().getDeptID()+"   ");		}
			}
		
			transaction.commit();
			session.flush();
			session.close();

	
		
	}
	
}
*/