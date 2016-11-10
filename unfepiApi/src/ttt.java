/*
 * 
 */
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.transform.Transformers;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.utils.OrderBySqlFormula;


/**
 * The Class ttt.
 */
public class ttt {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public static void main(String[] args) {
		
		/*AnnotationConfiguration conf = (new AnnotationConfiguration()).configure();
		new SchemaExport(conf).create(showHqltrue, runfalse);
		
		
		*/
		//System.out.println(new Arm().toString());
		//SessionFactory sf=	new Configuration().configure().buildSessionFactory();
		
		Configuration configuration = new Configuration();
	    configuration.configure();
	    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

	    
	Session session=sessionFactory.openSession();
			/*Transaction t=session.beginTransaction();
		List<Object> res = session.doReturningWork(new ReturningWork<List<Object>  or object type you return from execute method below>() {
			@Override
			 or object type you need to return to process
            public List<Object> execute(Connection conn) throws SQLException 
            {
            	CallableStatement cstmt = conn.prepareCall("temp");
            	//Result list that would return ALL rows of ALL result sets
            	List<Object> result = new ArrayList<Object>();
                try
                {
                	cstmt.execute();                        
                	
                	ResultSet rs = cstmt.getResultSet(); // First resultset
                	while (rs.next()) {//Read items/rows of first resultset
                		// .
                		// Process rows of first resultset
                		
                		//result.add(obj); // add items of resultset 1 to the returning list object
                	}
                	
                	cstmt.getMoreResults(); // Moves to this Statement object's next result, returns true if it is a ResultSet object
                	                                        
                	rs = cstmt.getResultSet(); // Second resultset
                	while (rs.next()) {
                		// .
                		// Process rows of second resultset
                		
                		//result.add(obj); // add items of resultset 2 to the returning list object
                	}
                	rs.close();                           
                }
                finally
                {cstmt.close();}
                
				return result; // this should contain All rows or objects you need for further processing
            }
        });*/
	
	//DetachedCriteria activePublishedCriteria = DetachedCriteria.forClass(Vaccination.class)
		//    .add(Restrictions.sqlRestriction("vaccinationRecordNum LIKE ?", "%13%",StandardBasicTypes.STRING));//(" vaccinationRecordNum LIKE '%"+13+"%'"));
	
	//List ls = activePublishedCriteria.getExecutableCriteria(session).list();
	//ls.get(0);
			  
	List<Vaccine> vl = session.createCriteria(Vaccine.class).list();
	Set<VaccinePrerequisite> pr = vl.get(3).getPrerequisites();
	Criteria criteria = session.createCriteria(Vaccine.class).addOrder(OrderBySqlFormula.sqlFormula("(select CASE WHEN gaptimeunit = 'month' THEN (30*value) " +
			" WHEN gaptimeunit = 'week' THEN (7*value) " +
			" WHEN gaptimeunit = 'day' THEN (0*value) " +
			" WHEN gaptimeunit = 'year' THEN (365*value) " +
			" ELSE 0 END from vaccinegap where vaccinegaptypeid=1 and vaccineid=this_.vaccineId)"));
	  
	  List ll = criteria.list();
ll.size();

		SQLQuery qq = session.createSQLQuery("call ExporterDailySummary()");
		List list = qq.list();
		list.toString();
		/*String[] mappingsToJoin=new String[]{"dailySummaryVaccineGiven"};
		Criteria cri = session.createCriteria(DailySummary.class,"rol");
				.add(Restrictions.eq("rolename", roleName)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Role> list = cri.list();
		list.get(0);*/
		/*List lis = session.createQuery("from ArmDayReminder ad where ad.id.armId = 1").list();
		Criteria cri = session.createCriteria(Child.class).setReadOnly(true)
				.createAlias("idMapper.identifiers", "idm").addOrder(Order.asc("idm.identifier"))
				.setFetchMode("idMapper", (true ? FetchMode.JOIN : FetchMode.SELECT));

		if(false){
			cri.setFetchMode("guardianRelation", FetchMode.JOIN);
			cri.setFetchMode("nicOwnerRelation", FetchMode.JOIN);
			cri.setFetchMode("religion", FetchMode.JOIN);
			cri.setFetchMode("ethnicity", FetchMode.JOIN);
		}
		
		if(false){
			cri.setFetchMode("createdByUserId", FetchMode.JOIN);
			cri.setFetchMode("lastEditedByUserId", FetchMode.JOIN);
		}
		
		System.out.println("sizePrj:"+cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Child> list = cri.setFirstResult(3).setMaxResults(6).list();
		System.out.println("size:"+list.size());*/
		
/*		Criteria cri = session.createCriteria(Screening.class,"scr").setReadOnly(true)
				
				.setFetchMode("idMapper", (true ? FetchMode.JOIN : FetchMode.SELECT));
		
		cri.createAlias("vaccinationCenter", "vc").add(Restrictions.eq("vc.mappedId", 2));
		if(true){
	cri.setFetchMode("vaccinationCenter", FetchMode.JOIN);
	cri.createAlias("vaccinationCenter.idMapper", "vcidm", JoinType.LEFT_OUTER_JOIN);
	}
if(true){
	cri.setFetchMode("vaccinator", FetchMode.JOIN);
	cri.createAlias("vaccinator.idMapper", "vtidm", JoinType.LEFT_OUTER_JOIN);
}
System.out.println("sizePrj:"+cri.setProjection(Projections.rowCount()).uniqueResult());
cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
List<Screening> ls =  cri.setFirstResult(20).setMaxResults(20).list();
System.out.println("size:"+ls.size());*/
		/*if(true){
			cri.setFetchMode("vaccinator", (true ? FetchMode.JOIN : FetchMode.SELECT));
			cri.createAlias("vaccinator.idMapper", "vtidm")
			.setFetchMode("vtidm", FetchMode.JOIN);
		}
		if(true){
			cri.setFetchMode("town", FetchMode.JOIN)
			.setFetchMode("city", FetchMode.JOIN);
		}
		if(true){
			cri.setFetchMode("createdByUserId", FetchMode.JOIN);
			cri.setFetchMode("lastEditedByUserId", FetchMode.JOIN);
			
		}*/
		
		
		session.close();

		/*for (Screening s : ls) {
			System.out.println("---"+s.getVaccinationCenter().getMappedId()+":"+s.getVaccinationCenter().getIdMapper().getIdentifiers().get(0).getIdentifier());
			System.out.println(s.getVaccinator().getMappedId()+":"+s.getVaccinator().getIdMapper().getIdentifiers().get(0).getIdentifier());

		}*/
		/*Criteria cri = session.createCriteria(Screening.class).setFirstResult(0).setMaxResults(3);
		List a = cri.list();
		cri.setProjection(Projections.rowCount());
		Number num = (Number) cri.uniqueResult();
		System.out.print(num);*/
		//session.createQuery("from Child").list();
		/*Criteria cri = session.createCriteria(Ctu.class);
		cri.list();*/
		/*if(true){
			cri.setFetchMode("idMapper",(true ? FetchMode.JOIN : FetchMode.SELECT))
			.createAlias("idMapper.identifiers", "idm")
			.addOrder(Order.asc("idm.identifier"));
			
			if(true){
				cri.createAlias("idm.role", "role")
					.setFetchMode("role",(true ? FetchMode.JOIN : FetchMode.SELECT));
				
				if(true){
					cri.createAlias("role.permissions", "perms",JoinType.LEFT_OUTER_JOIN)
						.setFetchMode("perms",(true ? FetchMode.JOIN : FetchMode.SELECT));
				}
			}
		}*/
		
		/*@SuppressWarnings("unchecked")
		List<User> list = cri.list();

		System.out.println(list.size());*/
/*		List<IdMapper> l;
		l = session.createQuery("from IdMapper").list();
		session.close();
		System.out.println(l.get(0).toString().replace(";", "\n"));

		for (IdMapper object : l) {
			System.out.println(object);
		}*/
	}
}
