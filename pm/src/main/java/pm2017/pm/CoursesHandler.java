package pm2017.pm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	
	/**
	 * Monta o mapa de códigos de matérias e suas respectivas situações
	 * @param input String do PDF do histórico
	 * @return Mapa com chave referente ao código da matéria, e o valor sendo um inteiro
	 *  que tem como dezenas o número de reprovações e unidade a condição de aprovação
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	public static HashMap<String, Integer> assembleCourseSituations(String input) throws IOException{
		HashMap<String, Integer> mapaCodSit = new HashMap<String, Integer>();
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		Integer situation = 0, oldSituation = 0;
		Boolean readingCourses = false;
		
		while ((line = bufReader.readLine()) != null){
			if (readingCourses = true){
				if ((situation = CourseSituation.getCourseSituation(line)) != null){
					if (mapaCodSit.get(line.substring(0, 7)) != null){
						oldSituation = mapaCodSit.get(line.substring(0, 7));
					} else {
						oldSituation = 0;
					}
					mapaCodSit.put(line.substring(0, 7), oldSituation + situation);
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
	
	/**
	 * Busca a matrícula na String do histórico
	 * @param input String do PDF do histórico
	 * @return Matrícula do aluno
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	public static String getRegistry(String input) throws IOException{
		HashMap<String, Integer> mapaCodSit = new HashMap<String, Integer>();
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		String[] words;
		Integer situation = 0, oldSituation = 0;
		Boolean readingCourses = false;
		
		while ((line = bufReader.readLine()) != null){
			if (line.contains("Matrícula")){
				words = line.split(" ");
				return words[1];
			}
		}
		
		return null;
	}
	
	/**
	 * Organiza os CRs do histórico em uma lista de double, desconsiderando o último período vigente
	 * @param input String do PDF do histórico
	 * @return ArrayList com a posição representando o período, e o valor 
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	public static ArrayList<Double> getCRsPerPeriod(String input) throws IOException{
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		String[] words;
		int counter = 1;
		ArrayList<Double> listCRs = new ArrayList<Double>();
		Integer period = BSIrules.getStudentCurrentPeriod(getRegistry(input));
		
		while ((line = bufReader.readLine()) != null){
			if (line.contains("Total Créditos")){
				if (counter == period){
					break;
				}
				words = line.split(" ");
				listCRs.add(Double.parseDouble(words[12]));
				counter++;
			}
		}
		
		return listCRs;
	}
	
/*	public static Integer numberOfApprovedCourses(){
		numberOfApprovedCourses(input);
	}*/
	
	/**
	 * Fornece o número de matérias aprovadas do histórico, checando pela unidade da situação de cada código de matéria
	 * @param input String do PDF do histórico
	 * @return Número de matérias aprovadas de acordo com o histórico
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	private static Integer numberOfApprovedCourses(String input) throws IOException{
		HashMap<String, Integer> coursesSituations = assembleCourseSituations(input);
		int counter = 0;
		
		
		for(Map.Entry<String, Integer> entry : coursesSituations.entrySet()){
			if ((entry.getValue() % 2) == 1){
				counter++;
			}
		}
		
		//Known bug
		//Não identifica se do número total de matérias foram cursadas matérias eletivas ou optativas acima da cota necessária
		
		return counter;
	}
	
}
