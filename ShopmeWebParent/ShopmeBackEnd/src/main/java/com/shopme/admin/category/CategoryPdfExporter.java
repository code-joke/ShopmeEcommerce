package com.shopme.admin.category;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.librepdf.openpdf.fonts.Liberation;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;

public class CategoryPdfExporter extends AbstractExporter {

	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf;charset=UTF-8", ".pdf", "categories_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = Liberation.SANS.create();
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of category", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 4.5f, 4.5f, 1.7f});
		
		writeTableHeader(table);
		writeTableData(table, listCategories);
		
		document.add(table);
		
		document.close();
		
	}

	private void writeTableData(PdfPTable table, List<Category> listCategories) {
		for (Category category : listCategories) {
			table.addCell(String.valueOf(category.getId()));
			table.addCell(category.getName());
			table.addCell(category.getAlias());
			table.addCell(String.valueOf(category.isEnabled()));
		}
	}

	private void writeTableHeader(PdfPTable table) throws IOException {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = Liberation.SANS_BOLD.create();
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Category Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Alias", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
	}
	
}
