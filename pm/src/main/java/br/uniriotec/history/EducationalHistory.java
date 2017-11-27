package br.uniriotec.history;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EducationalHistory {
	
	private String registry;
	private Integer currentPeriod;
	private String historyContent;
	private HashMap<String, Integer> courseSituationMap = new HashMap<String, Integer>();
	
	
	public EducationalHistory(String extractedPDF){
		try {
			this.historyContent = extractedPDF;
			this.registry = getRegistry(extractedPDF);
			this.setCourseSituationMap(assembleCourseSituations(extractedPDF));
			this.currentPeriod = getStudentCurrentPeriod(this.registry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Monta o mapa de códigos de matérias e suas respectivas situações
	 * @param input String do PDF do histórico
	 * @return Mapa com chave referente ao código da matéria, e o valor sendo um inteiro
	 *  que tem como dezenas o número de reprovações e unidade a condição de aprovação
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	public HashMap<String, Integer> assembleCourseSituations(String input) throws IOException{
		HashMap<String, Integer> courseSituationMap = new HashMap<String, Integer>();
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		Integer situation = 0, oldSituation = 0;
		Boolean readingCourses = false;
		
		while ((line = bufReader.readLine()) != null){
			if (readingCourses = true){
				if ((situation = CourseSituation.getCourseSituation(line)) != null){
					if (courseSituationMap.get(line.substring(0, 7)) != null){
						oldSituation = courseSituationMap.get(line.substring(0, 7));
					} else {
						oldSituation = 0;
					}
					courseSituationMap.put(line.substring(0, 7), oldSituation + situation);
				}
			}
			if (line.contains("Código")){
				readingCourses = true;
			} else if (line.contains("Coeficiente")){
				readingCourses = false;
			}
		}
		return courseSituationMap;
		
	}
	
	/**
	 * Contabiliza o período atual do estudante com base no ano de ingresso e o período atual
	 * @param registry Número de matrícula do estudante para obter ano de ingresso
	 * @return Número do período do estudante
	 */
	public Integer getStudentCurrentPeriod(String registry){
		Integer year = Integer.parseInt(registry.substring(0, 4)),
				period = Integer.parseInt(registry.substring(4, 5)),
				currYear = Calendar.getInstance().get(Calendar.YEAR),
				currMonth = Calendar.getInstance().get(Calendar.MONTH),
				difYear = currYear - year,
				currPeriod = 1,
				studentPeriod = 0;
		
		if (currMonth.compareTo(7) > 0){
			currPeriod = 2;
		}
		
		if ((period == 2) && currPeriod == 1){
			studentPeriod = difYear * 2;
		} else if ((period == 2) && currPeriod == 2){
			studentPeriod = (difYear * 2) + 1;
		} else {
			studentPeriod = (difYear * 2) + currPeriod;
		}
		
		return studentPeriod;
	}
	
	
	/**
	 * Busca a matrícula na String do histórico
	 * @param input String do PDF do histórico
	 * @return Matrícula do aluno
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	private String getRegistry(String input) throws IOException{
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		String[] words;
		
		while ((line = bufReader.readLine()) != null){
			if (line.contains("Matrícula")){
				words = line.split(" ");
				return words[1];
			}
		}
		
		return null;
	}
	
	
	/**
	 * Captura o valor do coeficiente de rendimento geral do estudante da string do histórico
	 * @return Valor do Coeficiente de Rendimento geral do estudante, ou -1 em caso de erro
	 */
	public Double getCRA(){
		Double output = -1d;
		try {
			output = getCRA(historyContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	
	/**
	 * Captura o valor do coeficiente de rendimento geral do estudante da string do histórico
	 * @param input String do PDF do histórico
	 * @return Valor do Coeficiente de Rendimento geral do estudante
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	private Double getCRA(String input) throws IOException{
		Double output = 0.0d;
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		String[] words;
		
		while ((line = bufReader.readLine()) != null){
			if (line.contains("Total Geral Créditos")){
				words = line.split(" ");
				output = Double.parseDouble(words[13].replace(',', '.'));
			}
		}
		
		return output;
	}
	
	/**
	 * Organiza os CRs do histórico em uma lista de double, desconsiderando o último período vigente
	 * @return ArrayList com a posição representando o período, e o valor 
	 */
	public ArrayList<Double> getCRsPerPeriod(){
		ArrayList<Double> output = new ArrayList<Double>();
		try {
			output = getCRsPerPeriod(historyContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	
	/**
	 * Organiza os CRs do histórico em uma lista de double, desconsiderando o último período vigente
	 * @param input String do PDF do histórico
	 * @return ArrayList com a posição representando o período, e o valor 
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	private ArrayList<Double> getCRsPerPeriod(String input) throws IOException{
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		String[] words;
		int counter = 1;
		ArrayList<Double> listCRs = new ArrayList<Double>();
		
		while ((line = bufReader.readLine()) != null){
			if (line.contains("Total Créditos")){
				if (counter == currentPeriod){
					break;
				}
				words = line.split(" ");
				listCRs.add(Double.parseDouble(words[12].replace(',', '.')));
				counter++;
			}
		}
		
		return listCRs;
	}
	
	/**
	 * Fornece o número de matérias aprovadas do histórico, checando pela unidade da situação de cada código de matéria
	 * @return Número de matérias aprovadas de acordo com o histórico
	 */
	public Integer numberOfApprovedCourses(){
		int output = -1;
		try {
			output = numberOfApprovedCourses(historyContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	/**
	 * Fornece o número de matérias aprovadas do histórico, checando pela unidade da situação de cada código de matéria
	 * @param input String do PDF do histórico
	 * @return Número de matérias aprovadas de acordo com o histórico
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	private Integer numberOfApprovedCourses(String input) throws IOException{
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
	
	/**
	 * Fornece o número de matérias ativas no período vigente do histórico
	 * @return Número de matérias ativas, ou -1 em caso de erros
	 */
	public Integer numberOfActiveCourses(){
		int output = -1;
		
		try {
			output = numberOfActiveCourses(historyContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	/**
	 * Fornece o número de matérias ativas no período vigente do histórico
	 * @param input String do PDF do histórico
	 * @return Número de matérias ativas
	 * @throws IOException Em caso de erros I/O na leitura
	 */
	private Integer numberOfActiveCourses(String input) throws IOException{
		BufferedReader bufReader = new BufferedReader(new StringReader(input));
		String line;
		int counter = 1, activeCourses = 0;
		boolean lastPeriod = false;
		
		
		while ((line = bufReader.readLine()) != null){
			if (line.contains("Total Créditos")){
				if (counter == (currentPeriod - 1)){
					lastPeriod = true;
				} else if (counter == currentPeriod){
					break;
				} else {
					counter++;
				}	
			}
			if (lastPeriod = true){
				if (CourseSituation.getCourseInProgress(line)){
					activeCourses++;
				}
			}
		}
		
		return activeCourses;
	}

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public Integer getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(Integer currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public String getHistoryContent() {
		return historyContent;
	}

	public void setHistoryContent(String historyContent) {
		this.historyContent = historyContent;
	}
	
	public HashMap<String, Integer> getCourseSituationMap() {
		return courseSituationMap;
	}

	public void setCourseSituationMap(HashMap<String, Integer> courseSituationMap) {
		this.courseSituationMap = courseSituationMap;
	}
	
	
}
