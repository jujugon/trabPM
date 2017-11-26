package pm2017.pm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CoursesHandler {
	
	
	/**
	 * Método para carregar as matérias obrigatórias com base nos códigos da UNIRIO para o curso de BSI, descritos em um TXT
	 * @return ArrayList com todos os códigos
	 * @throws IOException Em caso de arquivo não encontrado ou erro de I/O na leitura da linha
	 */
	public static ArrayList<String> loadCourses() throws IOException {
		ArrayList<String> coursesList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("C:/Users/lucas/Desktop/materias.txt"));
		String line;
		do {
			line = reader.readLine();
			coursesList.add(line);
		} while (line != null);
		
		reader.close();
		
		return coursesList;
	}
	
	public static HashMap<String, Integer> assembleCourseSituations(String input) throws IOException{
		HashMap<String, Integer> mapaCodSit = new HashMap<String, Integer>();
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		Integer situation;
		Boolean readingCourses = false;
		
		while ((line = bufReader.readLine()) != null){
			if (readingCourses = true){
				if ((situation = CourseSituation.getCourseSituation(line)) != null){
					mapaCodSit.put(line.substring(0, 7), situation);
				}
			}
			if (line.contains("Código")){
				readingCourses = true;
			} else if (line.contains("Coeficiente")){
				readingCourses = false;
			}
		}
		System.out.println(mapaCodSit.entrySet());
		return mapaCodSit;
		
	}
	
}
