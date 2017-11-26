package pm2017.pm;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

public class ExtractPageContent {

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
	
	//Arquivo resource não funcionando
	//TODO corrigir o método para trocar o caminho direto pelo TXT do resources
	public String getFile(String fileName) {

		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	  }
	
	
	public static void main(String[] args) {
        try {
        	ExtractPageContent extract = new ExtractPageContent();
        	String path = "C:/Users/lucas/Desktop/historicoEscolarCRAprovados.pdf";
        	HashMap<String, Integer> testOutput;
            String output;
            
            output = extractPageContent(path);
            
    		testOutput = CoursesHandler.assembleCourseSituations(output);
    		
    		System.out.println(testOutput);
    		
    		System.out.println(testOutput.size());
    		
    		System.out.println(CoursesHandler.loadCourses());

			
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

	

}
