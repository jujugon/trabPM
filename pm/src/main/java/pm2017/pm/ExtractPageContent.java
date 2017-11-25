package pm2017.pm;
import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

public class ExtractPageContent {

	public static void main(String[] args) {
        try {
            
            PdfReader reader = new PdfReader("C:/Users/lucas/Desktop/historicoEscolarCRAprovados.pdf");
			System.out.println(reader.toString());
			PdfDocument doc = new PdfDocument(reader);
			PdfDocumentContentParser parser = new PdfDocumentContentParser(doc);
			LocationTextExtractionStrategy strategy;
			String ExtractedText = null;
			int numberOfPages = doc.getNumberOfPages();
			
			for (int i = 1; i < numberOfPages + 1; i++) {
				strategy = parser.processContent(i, new LocationTextExtractionStrategy());
				ExtractedText = strategy.getResultantText().toString();
				System.out.println(ExtractedText);
			}

			//TODO Capturar a situação em cada matéria a partir do
			//código da própria, inserir as situações em um hashmap com os respectivos códigos
			
			System.out.println(CoursesHandler.loadCourses());
			
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
