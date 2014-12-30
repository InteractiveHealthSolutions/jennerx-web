package org.ird.unfepi;

import java.util.ArrayList;
import java.util.List;

public class CsveeRow {
	List<StringBuilder> row = new ArrayList<StringBuilder>();
	public CsveeRow() {
		
	}
	
	public void addRowElement(Object value){
		row.add(value == null? null : new StringBuilder(value.toString()));
	}
	
	public String getRowData()
	{
		StringBuilder sb = new StringBuilder();
		
		for (StringBuilder rd : row) {
			//fw.write(("\"\"").getBytes());
			//fw.write(',');
			sb.append("\""); // "\"
			sb.append(rd);   //value
			sb.append("\",");// "\",
		}
		sb.append("\n");//fw.write('\n');
		return sb.toString();
	}
	
	public char[] getRowAsCharArr()
	{
		StringBuilder sb = new StringBuilder();
		
		for (StringBuilder rd : row) {
			//fw.write(("\"\"").getBytes());
			//fw.write(',');
			sb.append("\""); // "\"
			sb.append(rd);   //value
			sb.append("\",");// "\",
		}
		sb.append("\n");//fw.write('\n');
		char[] arr = new char[sb.length()];
		sb.getChars(0, sb.length(), arr, 0);
		
		return arr;
	}
	
	public StringBuilder getRowAsSB()
	{
		StringBuilder sb = new StringBuilder();
		
		for (StringBuilder rd : row) {
			//fw.write(("\"\"").getBytes());
			//fw.write(',');
			sb.append("\""); // "\"
			sb.append(rd);   //value
			sb.append("\",");// "\",
		}
		sb.append("\n");//fw.write('\n');
		return sb;
	}
}
