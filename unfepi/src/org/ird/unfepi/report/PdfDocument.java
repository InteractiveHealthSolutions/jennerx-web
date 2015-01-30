package org.ird.unfepi.report;

import java.io.OutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PdfDocument extends Document
{
	private String documentName;

	public PdfDocument(OutputStream ostream, String documentName, String author, String creator, Rectangle pageSizeRect) throws DocumentException {
		this.documentName = documentName;
		addAuthor(author);
		addCreator(creator);
		setPageSize(pageSizeRect);
		
		PdfWriter writer = PdfWriter.getInstance(this, ostream);
		//new Rectangle(llx, lly, urx, ury);
		Rectangle rect = new Rectangle(pageSizeRect.getLeft()+15, pageSizeRect.getBottom()+15, pageSizeRect.getRight()-15, pageSizeRect.getTop()-15);
		writer.setBoxSize("header", rect);
		writer.setPageEvent(new HeaderFooter(this.getDocumentName()));
		
		this.open();
	}

	public String getDocumentName() {
		return documentName;
	}
	public void addTable(PdfTable table) throws DocumentException{
		add(table);
	}
	
	/** Inner class to add a header and a footer. */
    static class HeaderFooter extends PdfPageEventHelper 
    {
    	private String documentName;
    	public HeaderFooter(String documentName) {
			this.documentName = documentName;
		}
    	@Override
    	public void onStartPage(PdfWriter writer, Document document) {
    		 Rectangle rect = writer.getBoxSize("header");
    		 
             ColumnText.showTextAligned(writer.getDirectContent(),
                 Element.ALIGN_LEFT, new Phrase(documentName),
                 rect.getLeft(), rect.getTop(), 0);
             
             ColumnText.showTextAligned(writer.getDirectContent(),
                     Element.ALIGN_CENTER, new Phrase(new Chunk(new LineSeparator())),
                     (rect.getLeft() + rect.getRight()) / 2, rect.getTop()-3, 0);
    	}
    	
        public void onEndPage (PdfWriter writer, Document document) {
   		 Rectangle rect = writer.getBoxSize("header");

   		 ColumnText.showTextAligned(writer.getDirectContent(),
                 Element.ALIGN_CENTER, new Phrase(new Chunk(new LineSeparator())),
                 (rect.getLeft() + rect.getRight()) / 2, rect.getBottom()+10, 0);
   		 
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("page %d", writer.getPageNumber())),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom(), 0);
        }
    }
    
    public void finishAndCloseDocument(){
    	this.close();
    }
}
