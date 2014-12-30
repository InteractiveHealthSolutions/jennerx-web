import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierStatus;
import org.ird.unfepi.model.Notifier.NotifierType;

import com.mysql.jdbc.StringUtils;


public class testnotifier {

	public static void main(String[] args) throws IOException 
	{
		Notifier notifier = new Notifier();
		//notifier.setColumnsHeaderList("childId");
		notifier.setColumnsHeaderList("childId,vaccinationCenterId,vaccinatorId,vaccinationDuedate");

		//notifier.setNotifierCron(notifierCron);
		//notifier.setNotifierMessage(notifierMessage);
		notifier.setNotifierName("NOTF_COUNT");
		notifier.setNotifierQuery("select childId,vaccinationCenterId,vaccinatorId,vaccinationDuedate from vaccination");
		
		//notifier.setNotifierQuery("select childId from vaccination");
		HashSet<String> rec = new HashSet<String>();
		rec.add("maimoona.kausar@irdinformatics.org");
		notifier.setNotifierRecipient(rec);
		notifier.setNotifierStatus(NotifierStatus.ACTIVE);
		//notifier.setNotifierSubject(notifierSubject);
		notifier.setNotifierType(NotifierType.EMAIL_CSV);
		//notifier.setQueryDescription(queryDescription);
		
		if(notifier.getNotifierType().equals(NotifierType.EMAIL_CSV))
		{
			ByteArrayOutputStream fw = new ByteArrayOutputStream();
			
			String[] headerFields = notifier.getColumnsHeaderList().split(",");
			
			for (int i = 0 ; i < headerFields.length ; i++) 
			{
				fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(headerFields[i])?"":headerFields[i].replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
				if(i < (headerFields.length -1)){
					fw.write(',');
				}
			}
			
			fw.write('\n');
			
			ServiceContext sc = Context.getServices();
			List list = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierQuery());
			for (Object object : list) 
			{
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int i = 0 ; i < coldata.length ; i++) {
						fw.write(("\""+(coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
						if(i < (coldata.length -1)){
							fw.write(',');
						}
					}
					fw.write('\n');
				}
				else{
					fw.write(("\""+(object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
					fw.write('\n');
				}
				
			}
			
			byte[] b=fw.toString().replace("\"null\"", "\"\"").getBytes();
			System.out.println(new String(b));
			ByteArrayOutputStream zo = new ByteArrayOutputStream();
			ZipOutputStream zip = new ZipOutputStream(zo);
			zip.putNextEntry(new ZipEntry(notifier.getNotifierName()+".csv"));
			zip.write(b);
			zip.closeEntry();
			zip.close();
			
			/*try {
				EmailEngine.getInstance().getEmailer().postEmailWithAttachment(notifier.getNotifierRecipient().toArray(new String[0]), notifier.getNotifierSubject(), notifier.getNotifierMessage(), "epi@ird.org", zo.toByteArray(), "ziptestfile.zip",AttachmentType.ZIP);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
}
