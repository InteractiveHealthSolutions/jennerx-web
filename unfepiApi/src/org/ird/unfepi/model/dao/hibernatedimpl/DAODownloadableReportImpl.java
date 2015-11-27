package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.model.dao.DAODownloadableReport;

public class DAODownloadableReportImpl extends DAOHibernateImpl implements DAODownloadableReport{

	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAODownloadableReportImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DownloadableReport getById(int downloadableId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(DownloadableReport.class)
				.add(Restrictions.eq("downloadableId", downloadableId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<DownloadableReport> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DownloadableReport> findByCritria(String downlaodableReportNameLike, String downlaodableReportPathLike
			, String downloadableReportType,
			Integer sizeBytesLower, Integer sizeBytesUpper, Date createdDateLower, Date createdDateUpper
			, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(DownloadableReport.class);
		
		if(downlaodableReportNameLike != null){
			cri.add(Restrictions.like("downloadableName", downlaodableReportNameLike, MatchMode.ANYWHERE));
		}
		
		if(downlaodableReportPathLike != null){
			cri.add(Restrictions.like("downloadablePath", downlaodableReportPathLike, MatchMode.ANYWHERE));
		}
		
		if(downloadableReportType != null){
			cri.add(Restrictions.eq("downloadableType", downloadableReportType));
		}
		
		if(sizeBytesLower!=null && sizeBytesUpper!=null){
			cri.add(Restrictions.between("sizeBytes", sizeBytesLower, sizeBytesUpper));
		}
		
		if(createdDateLower!=null && createdDateUpper!=null){
			cri.add(Restrictions.between("createdDate", createdDateLower, createdDateUpper));
		}
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		return cri.addOrder(Order.desc("createdDate"))
				.setReadOnly(readonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
	}
}
