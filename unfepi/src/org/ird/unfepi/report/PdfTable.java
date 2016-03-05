package org.ird.unfepi.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfTable extends PdfPTable{

	private int columns;
	private String tableName;
	
	public PdfTable(int cloumns, String tableName) {
		super(cloumns);
		this.columns = cloumns;
		this.tableName = tableName;
		
		PdfPCell nameCell = new PdfPCell();
		nameCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		nameCell.setHorizontalAlignment(ALIGN_CENTER);
		nameCell.setColspan(cloumns);
		nameCell.setPhrase(new Phrase(tableName));
		addCell(nameCell);

		//gdfghd
	}
	
	public void addRow(String[] rowData){
		if(rowData.length != columns){
			throw new IllegalArgumentException("rowdata donot have number of columns equal to table's columns");
		}
		
		for (String data : rowData) {
			addCell(data);
		}
	}
	
	public void addHeader(String[] headerRowData){
		if(headerRowData.length != columns){
			throw new IllegalArgumentException("rowdata donot have number of columns equal to table's columns");
		}
		
		for (String data : headerRowData) {
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.DARK_GRAY);
			Phrase pr = new Phrase(data);
			Font font = new Font();
			font.setColor(BaseColor.WHITE);
			font.setStyle(FontStyle.BOLD.getValue());
			font.setSize(10);
			pr.setFont(font);
			cell.addElement(pr);
			addCell(cell);
		}
	}
	
	public void addfooter(){
		
	}
}
