package org.ird.unfepi.web.dwr;


public class DWRCsvService {

	/*public String getCsvUploaded(long recordId){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			return "Your session has expired . Please login again.";
		}
		ServiceContext sc = Context.getServices();
		try{
			Scanner scanner = new Scanner(new FileInputStream(sc.getTransactionLogService().getUploadReport(recordId).toString()));
			StringBuilder sb = new StringBuilder();
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine()+"\n");
			}
			return sb.toString();
		}catch (Exception e) {
			return "Error occurred while retrieving report. "+e.getMessage();
		}finally{
			sc.closeSession();
		}
	}*/
}
