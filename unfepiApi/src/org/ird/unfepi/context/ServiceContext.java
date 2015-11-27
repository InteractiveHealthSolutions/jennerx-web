package org.ird.unfepi.context;

import java.sql.Clob;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.ird.unfepi.model.dao.DAO;
import org.ird.unfepi.model.dao.DAOAddress;
import org.ird.unfepi.model.dao.DAOArm;
import org.ird.unfepi.model.dao.DAOCalendarDay;
import org.ird.unfepi.model.dao.DAOChildIncentive;
import org.ird.unfepi.model.dao.DAOChildLotteryParams;
import org.ird.unfepi.model.dao.DAOCommunicationNote;
import org.ird.unfepi.model.dao.DAOContactNumber;
import org.ird.unfepi.model.dao.DAODailySummary;
import org.ird.unfepi.model.dao.DAODailySummaryVaccineGiven;
import org.ird.unfepi.model.dao.DAODirectQuery;
import org.ird.unfepi.model.dao.DAODownloadableReport;
import org.ird.unfepi.model.dao.DAOEncounter;
import org.ird.unfepi.model.dao.DAOEncounterResults;
import org.ird.unfepi.model.dao.DAOEthnicity;
import org.ird.unfepi.model.dao.DAOIdMapper;
import org.ird.unfepi.model.dao.DAOIncentiveParams;
import org.ird.unfepi.model.dao.DAOLanguage;
import org.ird.unfepi.model.dao.DAOLotterySms;
import org.ird.unfepi.model.dao.DAONotifier;
import org.ird.unfepi.model.dao.DAORelationship;
import org.ird.unfepi.model.dao.DAOReligion;
import org.ird.unfepi.model.dao.DAOResponse;
import org.ird.unfepi.model.dao.DAOScreening;
import org.ird.unfepi.model.dao.DAOStorekeeper;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveEvent;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveParams;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveTransaction;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.dao.DAOUserSms;
import org.ird.unfepi.model.dao.DAOVaccinationCenter;
import org.ird.unfepi.model.dao.DAOVaccinationCenterVaccineDay;
import org.ird.unfepi.model.dao.DAOVaccinator;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentive;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveEvent;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveParams;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveTransaction;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveWorkProgress;
import org.ird.unfepi.model.dao.DAOVariableSetting;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOAddressImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOArmImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOCalendarDayImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOChildImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOChildIncentiveImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOChildLotteryParamsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOCommunicationNoteImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOContactNumberImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAODailySummaryImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAODailySummaryVaccineGivenImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAODirectQueryImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAODownloadableReportImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOEncounterImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOEncounterResultsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOEthnicityImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOHibernateImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOIdMapperImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOIncentiveParamsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOLanguageImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOLotterySmsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAONotifierImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOPermissionImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAORelationshipImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOReligionImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOReminderImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOReminderSmsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOResponseImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAORoleImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOScreeningImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOSettingImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOStorekeeperImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOStorekeeperIncentiveEventImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOStorekeeperIncentiveParamsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOStorekeeperIncentiveParticipantImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOStorekeeperIncentiveTransactionImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOStorekeeperIncentiveWorkProgressImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOUserImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOUserSmsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinationCenterImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinationCenterVaccineDayImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinationImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinatorImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinatorIncentiveEventImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinatorIncentiveImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinatorIncentiveParamsImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinatorIncentiveParticipantImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinatorIncentiveTransactionImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccinatorIncentiveWorkProgressImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVaccineImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOVariableSettingImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOWomenImpl;
import org.ird.unfepi.model.dao.hibernatedimpl.DAOWomenVaccinationImpl;
import org.ird.unfepi.service.ChildService;
import org.ird.unfepi.service.CommunicationService;
import org.ird.unfepi.service.CustomQueryService;
import org.ird.unfepi.service.DemographicDetailsService;
import org.ird.unfepi.service.EncounterService;
import org.ird.unfepi.service.IdMapperService;
import org.ird.unfepi.service.IncentiveService;
import org.ird.unfepi.service.ReminderService;
import org.ird.unfepi.service.ReportService;
import org.ird.unfepi.service.SettingService;
import org.ird.unfepi.service.StorekeeperService;
import org.ird.unfepi.service.UserService;
import org.ird.unfepi.service.UserSmsService;
import org.ird.unfepi.service.VaccinationService;
import org.ird.unfepi.service.WomenService;
import org.ird.unfepi.service.WomenVaccinationService;
import org.ird.unfepi.service.impl.ChildServiceImpl;
import org.ird.unfepi.service.impl.CommunicationServiceImpl;
import org.ird.unfepi.service.impl.CustomQueryServiceImpl;
import org.ird.unfepi.service.impl.DemographicDetailsServiceImpl;
import org.ird.unfepi.service.impl.EncounterServiceImpl;
import org.ird.unfepi.service.impl.IdMapperServiceImpl;
import org.ird.unfepi.service.impl.IncentiveServiceImpl;
import org.ird.unfepi.service.impl.ReminderServiceImpl;
import org.ird.unfepi.service.impl.ReportServiceImpl;
import org.ird.unfepi.service.impl.SettingServiceImpl;
import org.ird.unfepi.service.impl.StorekeeperServiceImpl;
import org.ird.unfepi.service.impl.UserServiceImpl;
import org.ird.unfepi.service.impl.UserSmsServiceImpl;
import org.ird.unfepi.service.impl.VaccinationServiceImpl;
import org.ird.unfepi.service.impl.WomenServiceImpl;
import org.ird.unfepi.service.impl.WomenVaccinationServiceImpl;

public class ServiceContext {

	//private ArmService	armService;
	
	private ChildService childService;
	
	private CustomQueryService customQueryService;
	
	private DemographicDetailsService demographicDetailsService;
	
	private EncounterService encounterService;
	
	private IdMapperService idMapperService;
	
	private IncentiveService incentiveService;
	
	private SettingService irSettingService;
	
	//private TransactionLogService logService;
	
	private ReminderService reminderService;
	
	private ReportService reportService;
	
	private CommunicationService communicationService;
	
	private Session session;
	
	private Transaction transaction;
	
	private StorekeeperService storekeeperService;

	private UserService userService;
	
	private UserSmsService userSmsService;

	private VaccinationService vaccinationService;
	
	private WomenService womenService;
	
	private WomenVaccinationService womenVaccinationService;

	ServiceContext(SessionFactory sessionObj) 
	{
		session = sessionObj.openSession();
		transaction = session.beginTransaction();

		DAOUserImpl udao = new DAOUserImpl(session);
		DAORoleImpl rdao = new DAORoleImpl(session);
		DAOPermissionImpl pdao = new DAOPermissionImpl(session);
		this.userService = new UserServiceImpl(udao, rdao, pdao);

		DAOIdMapper idmapperDao = new DAOIdMapperImpl(session);
		this.idMapperService = new IdMapperServiceImpl(idmapperDao);

		DAOChildImpl chldoa = new DAOChildImpl(session);
		DAOScreening scrdao = new DAOScreeningImpl(session);
		DAOLotterySms daolotsms = new DAOLotterySmsImpl(session);
		DAOArm	daoarm 	= new DAOArmImpl(session);
		this.childService = new ChildServiceImpl(this, chldoa, scrdao, daolotsms,daoarm);
		
		DAOWomenImpl womendao = new DAOWomenImpl(session);
		this.womenService = new WomenServiceImpl(this,womendao);
		
		DAODirectQuery dirqudao = new DAODirectQueryImpl(session);
		DAO dao = new DAOHibernateImpl(session);
		this.customQueryService = new CustomQueryServiceImpl(this, dirqudao, dao);

		/*DAOPerson daoper = new DAOPersonImpl(session);*/
		DAOAddress daoaddr = new DAOAddressImpl(session);
		DAOContactNumber daocont = new DAOContactNumberImpl(session);
		DAOEthnicity daoethn = new DAOEthnicityImpl(session);
		DAORelationship daorelation = new DAORelationshipImpl(session);
		DAOReligion daoreligion = new DAOReligionImpl(session);
		DAOLanguage daolang = new DAOLanguageImpl(session);
		this.demographicDetailsService = new DemographicDetailsServiceImpl(this, 
				/*daoper,*/ daoaddr, daocont, daoethn, daorelation, daoreligion, daolang);

		DAOStorekeeper daoStorkeep = new DAOStorekeeperImpl(session);
		this.storekeeperService = new StorekeeperServiceImpl(this, daoStorkeep);

		DAOVaccineImpl vaccdao = new DAOVaccineImpl(session);
		DAOVaccinationImpl pvdao = new DAOVaccinationImpl(session);
		DAOVaccinationCenter vacccentdao = new DAOVaccinationCenterImpl(session);
		DAOVaccinator vaccinatordao = new DAOVaccinatorImpl(session);
		DAOCalendarDay daocalendar = new DAOCalendarDayImpl(session);
		DAOVaccinationCenterVaccineDay daovcday = new DAOVaccinationCenterVaccineDayImpl(session);
		this.vaccinationService = new VaccinationServiceImpl(this, vaccdao, pvdao,
				vacccentdao, vaccinatordao, daocalendar, daovcday);
		
		DAOWomenVaccinationImpl womenvaccdao = new DAOWomenVaccinationImpl(session);
		this.womenVaccinationService = new WomenVaccinationServiceImpl(this, vaccdao, womenvaccdao,
				vacccentdao, vaccinatordao, daocalendar, daovcday);
		
		DAOReminderImpl remdao = new DAOReminderImpl(session);
		DAOReminderSmsImpl remsmsdao = new DAOReminderSmsImpl(session);
		this.reminderService = new ReminderServiceImpl(this, remdao, remsmsdao);

		DAOResponse daoresp=new DAOResponseImpl(session);
		DAOCommunicationNote daocomm = new DAOCommunicationNoteImpl(session);
		this.communicationService= new CommunicationServiceImpl(daoresp, daocomm);

		// HibernateDAOFlagData fd=new HibernateDAOFlagData(session);
		// this.fs=new FlagDataServiceImpl(fd);

		DAOSettingImpl irdao = new DAOSettingImpl(session);
		DAOVariableSetting vsdao = new DAOVariableSettingImpl(session);
		this.irSettingService = new SettingServiceImpl(this, irdao, vsdao);

		/*DAOCsvUploadImpl csvdao = new DAOCsvUploadImpl(session);
		this.logService = new TransactionLogServiceImpl(this, csvdao);
*/
		/*DAOArmImpl armdao = new DAOArmImpl(session);
		// DAOArmIdMapImpl armidmapdao=new DAOArmIdMapImpl(session);
		DAOArmDayReminderImpl armdayremdao = new DAOArmDayReminderImpl(session);
		this.armService = new ArmServiceImpl(this, armdao, armdayremdao ,armidmapdao );*/

		DAONotifier daonot = new DAONotifierImpl(session);
		DAODailySummary daodsum = new DAODailySummaryImpl(session);
		DAODailySummaryVaccineGiven daodsumvaccgvn = new DAODailySummaryVaccineGivenImpl(session);
		DAODownloadableReport daodownl = new DAODownloadableReportImpl(session);
		this.reportService = new ReportServiceImpl(daonot, daodsum, daodsumvaccgvn, daodownl);
		
		DAOEncounter daoenc = new DAOEncounterImpl(session);
		DAOEncounterResults daoencres = new DAOEncounterResultsImpl(session);
		this.encounterService = new EncounterServiceImpl(daoenc, daoencres);
		
		DAOVaccinatorIncentiveEvent daovlottevent = new DAOVaccinatorIncentiveEventImpl(session);
		DAOVaccinatorIncentiveParticipant daovlottparti = new DAOVaccinatorIncentiveParticipantImpl(session);
		DAOVaccinatorIncentiveParams daovlottparams = new DAOVaccinatorIncentiveParamsImpl(session);
		DAOVaccinatorIncentiveTransaction daovlotttrans = new DAOVaccinatorIncentiveTransactionImpl(session);
		DAOVaccinatorIncentiveWorkProgress daovlottwinrcords = new DAOVaccinatorIncentiveWorkProgressImpl(session);
		
		DAOStorekeeperIncentiveEvent daoincentevent = new DAOStorekeeperIncentiveEventImpl(session);
		DAOStorekeeperIncentiveParticipant daoincentparti = new DAOStorekeeperIncentiveParticipantImpl(session);
		DAOStorekeeperIncentiveParams daoincentparams = new DAOStorekeeperIncentiveParamsImpl(session);
		DAOStorekeeperIncentiveTransaction daoincenttrans = new DAOStorekeeperIncentiveTransactionImpl(session);
		DAOStorekeeperIncentiveWorkProgress daoincentwinrcords = new DAOStorekeeperIncentiveWorkProgressImpl(session);

		DAOChildLotteryParams daoclottparams = new DAOChildLotteryParamsImpl(session);
		DAOChildIncentive daochildincentive = new DAOChildIncentiveImpl(session);
		DAOVaccinatorIncentive daovincentive = new DAOVaccinatorIncentiveImpl(session);
		DAOIncentiveParams daoincentiveparams = new DAOIncentiveParamsImpl(session);
		this.incentiveService = new IncentiveServiceImpl(daovlottevent, daovlottparti, daovlottparams, daovlotttrans, daovlottwinrcords, daoincentevent, daoincentparti, daoincentparams, daoincenttrans, daoincentwinrcords, daoclottparams, daochildincentive, daovincentive, daoincentiveparams, this);
		
		DAOUserSms daousms = new DAOUserSmsImpl(session);
		
		this.userSmsService = new UserSmsServiceImpl(daousms);
	}
	
	public void beginTransaction(){
		if(transaction == null){
			transaction = session.beginTransaction();
		}
	}
	
	public void closeSession(){
		try{session.close();
		}catch (Exception e) {}
	}
	
	public void commitTransaction(){
		transaction.commit();
	}
	
	public Clob createClob(String clobString) {
		return Hibernate.getLobCreator(session).createClob(clobString);
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		closeSession();
	}

	public void rollbackTransaction() {
		if(transaction != null){
			transaction.rollback();
		}
	}
	
	public void  flushSession() {
		session.flush();
	}
	
/*	public ArmService getArmService(){
		//if(!session.getTransaction().isActive()) beginTransaction();
		return armService;
	}*/

	public ChildService getChildService(){
		//if(!session.getTransaction().isActive()) beginTransaction();
		return childService;
	}	

	public CustomQueryService getCustomQueryService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return customQueryService;
	}

	public DemographicDetailsService getDemographicDetailsService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return demographicDetailsService;
	}
	
	public EncounterService getEncounterService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return encounterService;
	}
	public IdMapperService getIdMapperService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return idMapperService;
	}
	
	public IncentiveService getIncentiveService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return incentiveService;
	}

	/**
	 * Should only be used via context to maintain Settings loaded in memory consistent
	 */
	public SettingService getIRSettingService(){
		//if(!session.getTransaction().isActive()) beginTransaction();
		return irSettingService;
	}

	public ReminderService getReminderService(){
		//if(!session.getTransaction().isActive()) beginTransaction();
		return reminderService;
	}
	
	public ReportService getReportService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return reportService;
	}
	
	public CommunicationService getCommunicationService(){
		//if(!session.getTransaction().isActive()) beginTransaction();
		return communicationService;
	}

	public StorekeeperService getStorekeeperService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return storekeeperService;
	}
	
/*	public TransactionLogService getTransactionLogService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return logService;
	}*/

	public UserService getUserService(){
		//if(!session.getTransaction().isActive()) beginTransaction();
		return userService;
	}

	public UserSmsService getUserSmsService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return userSmsService;
	}

	public VaccinationService getVaccinationService() {
		//if(!session.getTransaction().isActive()) beginTransaction();
		return vaccinationService;
	}
	
	public WomenService getWomenService() {
		return womenService;
	}
	
	public WomenVaccinationService getWomenVaccinationService() {
		return womenVaccinationService;
	}
	
}