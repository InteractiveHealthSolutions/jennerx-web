import java.util.Calendar;
import java.util.Date;

public class dateaddtest {

	public static void main(String[] args) {
		System.out.println( new java.sql.Date(1417719600000L));

		Date duedate = new Date(1417633200000L);

		int validityPeriod = 0;
		
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println((int)(System.currentTimeMillis()/4000));
		}

		final long currentTime = System.currentTimeMillis();
		final long maxValidTime = duedate.getTime()+(validityPeriod  * 60 * 60 * 1000L);
		final int hoursLeft = (int) ((maxValidTime - currentTime)/(1000 * 60 * 60L));
		System.out.println( hoursLeft);

		System.out.println(new Date(1351142205093L));

		System.out.println(new Date(1341320400000L));
		
		int firstAllowedDay = 1;//else monday
		
		Calendar actNextAssignedDate = Calendar.getInstance();
		
		int currday = actNextAssignedDate.get(Calendar.DAY_OF_WEEK);

		int dayToAdd = firstAllowedDay >= currday ? (firstAllowedDay-currday) : (7-currday+firstAllowedDay);
		actNextAssignedDate .add(Calendar.DATE, Math.abs(dayToAdd));

		System.out.println(dayToAdd+ "  :  "+ actNextAssignedDate.getTime());
	}
}
