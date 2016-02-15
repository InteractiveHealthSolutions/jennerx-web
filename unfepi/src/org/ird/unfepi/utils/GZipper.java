package org.ird.unfepi.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class GZipper {

	 public static String compress(String str) throws IOException {
	        if (str == null || str.length() == 0) {
	            return str;
	        }
	        System.out.println("String length : " + str.length());
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        GZIPOutputStream gzip = new GZIPOutputStream(out);
	        gzip.write(str.getBytes());
	        gzip.close();
	        //String outStr = out.toString("ISO-8859-1");
	       // System.out.println("Output String lenght : " + outStr.length());
	       
	        return  Base64.encodeBase64String(out.toByteArray());
	     }
	    
	    public static String decompress(String str) throws IOException {
	        if (str == null || str.length() == 0) {
	            return str;
	        }
	        byte[] bytes = Base64.decodeBase64(str);
	        System.out.println("Input String length : " + str.length());
	        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
	       // BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
	        return IOUtils.toString(gis);
	        
	     }
	
	
}
