/*
 * 
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;


// TODO: Auto-generated Javadoc
/**
 * The Class guidtest.
 */
public class guidtest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		  List<Long> keys = new ArrayList<Long>();

		  Set<String> set = new HashSet<String>();
		  
		  for (int i = 0; i < 1000000; i++) {
			  StringBuffer finalCode = new StringBuffer();
			  
		         Random ran = new Random();
		         ran.setSeed(new DateTime().getMillis()+Math.abs(ran.nextInt(Integer.MAX_VALUE)));
		         long l1 = Math.abs(ran.nextInt(Integer.MAX_VALUE));
		         long l2 = Math.abs(ran.nextInt(Integer.MAX_VALUE));
		         long l3 = Math.abs(ran.nextInt(Integer.MAX_VALUE));
		         finalCode.append(Long.toString(Math.abs(new DateTime().getMillis()))).reverse().toString();
		       
		         finalCode.append(String.valueOf(Math.abs((Long.parseLong(finalCode.toString())-(l1-l2+l3))))).reverse();
		         finalCode.setLength(17);
		         finalCode=new StringBuffer(String.valueOf(Math.abs((Long.parseLong(finalCode.toString())-(l1-l2+l3)))));
		         finalCode.append("000000000000000");
		         finalCode.setLength(13);
		         System.out.println(finalCode);

		         set.add(finalCode.toString());
		}
		  System.out.println(set.size());

	}
}
