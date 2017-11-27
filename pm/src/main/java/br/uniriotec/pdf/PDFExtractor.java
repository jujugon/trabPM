package br.uniriotec.pdf;

import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

public abstract class PDFExtractor {
			
	/**
	 * Método para extrair o conteúdo das páginas do PDF do histórico para uma String Java
	 * @param path Caminho do pdf a ser lido
	 * @return String com o conteúdo concatenado de todas as páginas do pdf
	 * @throws IOException Em caso de arquivo não encontrado
	 */
	public static String extractPageContent(String path) throws IOException{
		PdfReader reader = new PdfReader(path);
		PdfDocument doc = new PdfDocument(reader);
		PdfDocumentContentParser parser = new PdfDocumentContentParser(doc);
		LocationTextExtractionStrategy strategy;
		String ExtractedText = "", AssembledText = "";
		int numberOfPages = doc.getNumberOfPages();
		
		for (int i = 1; i < numberOfPages + 1; i++) {
			strategy = parser.processContent(i, new LocationTextExtractionStrategy());
			ExtractedText = strategy.getResultantText().toString();
			AssembledText = AssembledText + ExtractedText;
		}
		
		return AssembledText;
	}
	

}
