package org.ird.unfepi.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Csvee {

	//private String name;
	private List<CsveeRow> headerrows;
	private List<CsveeRow> footerrows;
	private List<CsveeRow> datarows;
	//private ByteArrayOutputStream bao;
	
	public Csvee(/*String name*/) {
		//this.name = name;
		headerrows = new ArrayList<CsveeRow>();
		footerrows = new ArrayList<CsveeRow>();
		datarows = new ArrayList<CsveeRow>();
		//bao = new ByteArrayOutputStream();
	}
	public void addHeader(CsveeRow headerrow){
		headerrows.add(headerrow);
	}
	public void addFooter(CsveeRow footerrow){
		footerrows.add(footerrow);
	}
	public void addData(CsveeRow datarow){
		datarows.add(datarow);
	}
	public byte[] getCsv(boolean replaceNulls) throws IOException{
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		for (CsveeRow r : headerrows) {
			bao.write(r.getRowData().getBytes());
		}
		
		for (CsveeRow r : datarows) {
			bao.write(r.getRowData().getBytes());
		}
		
		for (CsveeRow r : footerrows) {
			bao.write(r.getRowData().getBytes());
		}
		
		byte[] b;
		if(replaceNulls){
			b = bao.toString().replace("\"null\"", "\"\"").getBytes();
		}
		else{
			b = bao.toString().getBytes();
		}
		bao.flush();
		bao.close();
		return b;
	}
	
	public ByteArrayOutputStream getCsvStream(boolean replaceNulls) throws IOException{
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		for (CsveeRow r : headerrows) {
			bao.write(r.getRowData().getBytes());
		}
		
		for (CsveeRow r : datarows) {
			bao.write(r.getRowData().getBytes());
		}
		
		for (CsveeRow r : footerrows) {
			bao.write(r.getRowData().getBytes());
		}
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();

		if(replaceNulls){
			b.write(bao.toString().replace("\"null\"", "\"\"").getBytes());
		}
		else{
			b.write(bao.toString().getBytes());
		}
		bao.flush();
		bao.close();
		return b;
	}
	
	public void populateCsvFromList(List list, boolean appendSerial){
		int i1=1;
		for(Object object : list){
			CsveeRow datarow = new CsveeRow();

			if(appendSerial)
			{
				datarow.addRowElement(i1);
			}
			
			if(object instanceof Object[]){
				Object[] coldata = (Object[]) object;
				for (int ind = 0 ; ind < coldata.length ; ind++) {
					datarow.addRowElement(coldata[ind]);
				}
			}
			else{
				datarow.addRowElement(object);
			}
			
			this.addData(datarow);
			
			i1++;
		}
	}
}
