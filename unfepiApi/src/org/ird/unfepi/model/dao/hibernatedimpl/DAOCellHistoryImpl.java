package org.ird.unfepi.model.dao.hibernatedimpl;
/*package org.ird.immunizationreminder.dao.hibernatedao;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.immunizationreminder.dao.DAOCellHistory;
import org.ird.immunizationreminder.datamodel.entities.CellHistory;

*//**
 * Home object for domain model class CellHistory.
 * @see org.ird.immunizationreminder.datamodel.entities.CellHistory
 * @author Hibernate Tools
 *//*
public class DAOCellHistoryImpl extends DAOHibernateImpl implements DAOCellHistory{

	private SessionFactory sessionFactory;
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	public DAOCellHistoryImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
		this.sessionFactory=sessionFactory;
	}
	@Override
	public CellHistory findById(int id) {
		CellHistory pc= (CellHistory) sessionFactory.getCurrentSession().get("CellHistory",id);
		setLAST_QUERY_TOTAL_ROW__COUNT(pc==null?0:1);
		return pc;
	}
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CellHistory> getAll(int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return sessionFactory.getCurrentSession().createQuery("from CellHistory order by dateAdded desc").setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	@Override
	public Number countAllRows(){
		return (Number) sessionFactory.getCurrentSession().createQuery("select count(*) from CellHistory").uniqueResult();
	}
	*//** search on given criteria, set values null if you want to exclude any criteria : donot give 
	 * invalid values
	 * 
	 * @param child_id
	 * @param begindate
	 * @param enddate
	 * @param cellNumber
	 * @param firstResult 
	 * @param fetchsize 
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	@Override
	public List<CellHistory> findByCriteria(String child_id,Date begindate,Date enddate,String cellNumber, int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(child_id, begindate, enddate, cellNumber));
		Criteria cri= sessionFactory.getCurrentSession().createCriteria(CellHistory.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("dateAdded", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(child_id!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", child_id));
		}
		return cri.addOrder(Order.desc("dateAdded")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	@Override
	public Number countCriteriaRows(String child_id,Date begindate,Date enddate,String cellNumber) {
		Criteria cri= sessionFactory.getCurrentSession().createCriteria(CellHistory.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("dateAdded", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(child_id!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.childId", child_id));
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
	*//**search on given criteria, set values null if you want to exclude any criteria : donot give 
	 * invalid values
	 * @param partOfFirstOrLastName
	 * @param begindate
	 * @param enddate
	 * @param cellNumber
	 * @param firstResult 
	 * @param fetchsize 
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	@Override
	public List<CellHistory> findByCriteriaIncludeName(String partOfFirstOrLastName,Date begindate
															,Date enddate,String cellNumber, int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaIncludeNameRows(partOfFirstOrLastName, begindate, enddate, cellNumber));
		Criteria cri= sessionFactory.getCurrentSession().createCriteria(CellHistory.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("dateAdded", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(partOfFirstOrLastName!=null){
			cri.createAlias("child", "p").add(
					Restrictions.or(Restrictions.like("p.firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		return cri.addOrder(Order.desc("dateAdded")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	@Override
	public Number countCriteriaIncludeNameRows(String partOfFirstOrLastName,Date begindate
															,Date enddate,String cellNumber) {
		Criteria cri= sessionFactory.getCurrentSession().createCriteria(CellHistory.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("dateAdded", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("cellNo", cellNumber,MatchMode.END));
		}
		if(partOfFirstOrLastName!=null){
			cri.createAlias("child", "p").add(
					Restrictions.or(Restrictions.like("p.firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}
}
*/