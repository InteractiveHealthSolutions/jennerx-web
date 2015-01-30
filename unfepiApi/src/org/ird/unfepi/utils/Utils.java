/*
 * 
 */
package org.ird.unfepi.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils {
	
	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			return null;
		}

		if (entity instanceof HibernateProxy) {
			Hibernate.initialize(entity);
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}
		return entity;
	}

	
	/** The rnd. */
	private static Random rnd=new Random();
	
	/**
	 * Checks if is number between.
	 *
	 * @param number the number
	 * @param min the min
	 * @param max the max
	 * @return true, if is number between
	 */
	public static boolean isNumberBetween(String number,int min,int max){
		try{
			int num=Integer.parseInt(number);
			if(num<min || num>max){
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the random number.
	 *
	 * @param range the range
	 * @return the random number
	 */
	public static int getRandomNumber(int range) {
		int num=rnd.nextInt(range);
		return num+1;
	}
	
	/**
	 * Gets the random number.
	 *
	 * @param min the min
	 * @param max the max
	 * @return the random number
	 */
	public static int getRandomNumber(int min,int max) {
		int numRange=max-min+1;
		int num=rnd.nextInt(numRange)+min;
		return num;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			System.out.println(getRandomNumber(3,4));
		}
	}
	
	/**
	 * Gets the list as string.
	 *
	 * @param collection the collection
	 * @param separator the separator
	 * @return the list as string
	 */
	public static String getListAsString(List<String> collection,String separator){
		StringBuilder strb=new StringBuilder();
		int len=collection.size();
		
		int index=0;
		for (String string : collection) {
			if(index==len-1){
				strb.append(string);
			}else{
				strb.append(string+separator);
			}
			index++;
		}
		return strb.toString();
	}
	
	/**
	 * Convert stream to string builder.
	 *
	 * @param is the is
	 * @return the string builder
	 * @throws Exception the exception
	 */
	public static StringBuilder convertStreamToStringBuilder(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = reader.readLine()) != null) {
		sb.append(line + "\n");
		}
		is.close();
		return sb;
	}
}
