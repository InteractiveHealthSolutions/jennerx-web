import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.ird.unfepi.context.Context;


public class sqlrestrictiontest {

	public static void main(String[] args) throws InstanceAlreadyExistsException {
		Context.instantiate(null);
		Session ss = Context.getNewSession();   
		List llll = ss.createSQLQuery("SELECT 1/100 FROM DUAL")
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		
/*		Query q = ss.createQuery("from org.ird.unfepi.model.Vaccination v where v.childId=777 " +
				" and vaccinationDate is not null " +
				" and date(vaccinationDate) = (select min(date(vaccinationDate)) from Vaccination where childId= v.childId) " +
				" and vaccinationRecordNum = CASE WHEN (select count(vaccinationRecordNum) from Vaccination where childId=v.childId " +
				" and date(vaccinationDate)=date(v.vaccinationDate)) = 1 " +
				" THEN v.vaccinationRecordNum " +
				" ELSE (select min(vaccinationRecordNum) from Vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate)) END" +
				"  (select min(vaccinationRecordNum) from Vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate)) ELSE " +
				"  v.vaccinationRecordNum END)");
*/		
		//String qr = "select v.childId from Vaccination v,User u inner join v.vaccine vc  where u.firstName like 'admin%' ";
		String qr = "select cast(1/100 as big_decimal),vaccineId from Vaccination where vaccineId=1 and vaccinationRecordNum < 66";

		Query q = ss.createQuery(qr);//ss.createQuery(qr);
		List l = q.list();
		l.get(0);
		List l1 = ss.createSQLQuery("SELECT 1/100 FROM DUAL").list(); //0.0100
		List l2 = ss.createQuery("SELECT cast(1/100 as big_decimal),vaccineId from Vaccination where vaccineId=1 and vaccinationRecordNum < 66").list(); //0.01
		l2.get(0);
		//ServiceContext sc = Context.getServices();
		
		//sc.getVaccinationService().getAllVaccinationRecord(0, 1000, true, null, new String[]{"vaccineid <> 1 and vaccineid <> 2"});
	}
}
