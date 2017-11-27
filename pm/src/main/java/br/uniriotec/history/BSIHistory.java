package br.uniriotec.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BSIHistory extends EducationalHistory {
	
	public BSIHistory(String extractedPDF) {
		super(extractedPDF);
	}

	private final int totalNumberOfCourses = 51;
	
	
	public String isAboveAverageCRA(){
		if(getCRA() >= 7.0d){
			return "O aluno tem CRA acima de 7";
		}
		return "O aluno tem CRA abaixo de 7";
	}
	
	
	/**
	 * Verifica se o aluno está em situação de jubilamento, seguindo a norma de 7 anos limite para ingressantes até 2013.2
	 * e 6 anos para ingressantes a partir de 2014.1, e também verifica através da quantidade de reprovações em uma única matéria
	 * se o estudante também tiver um CRA abaixo de 4.
	 * @return Informa a situação de jubilamento do estudante
	 */
	public String isDismissed(){
		if(isDismissed(getCourseSituationMap(), getCRA(), getRegistry(), getCurrentPeriod())){
			return "O aluno deve ser jubilado por motivo de reprovações excessivas e CRA baixo";
		}
		return "O aluno não deve ser jubilado por motivo de reprovações excessivas e CRA baixo";
	}
	
	/**
	 * Verifica se o aluno está em situação de jubilamento, seguindo a norma de 7 anos limite para ingressantes até 2013.2
	 * e 6 anos para ingressantes a partir de 2014.1, e também verifica através da quantidade de reprovações em uma única matéria
	 * se o estudante também tiver um CRA abaixo de 4.
	 * @param courseSituationMap Hashmap do código da matéria atrelado ao inteiro da situação de reprovações
	 * @param cra Coeficiente de Rendimento Acumulado do estudante
	 * @param registry matrícula do estudante
	 * @return Booleano informando a situação de jubilamento do estudante
	 */
	private Boolean isDismissed(HashMap<String, Integer> courseSituationMap, Double cra, String registry, Integer studentPeriod){
		Integer year = Integer.parseInt(registry.substring(0, 4));
	
		if (year.compareTo(2013) <= 0){ //Checagem caso o aluno seja ingressante até 2013.2
			if (studentPeriod > 14){ //Não pode ultrapassar 7 anos
				return true;
			}
		} else { //Para os alunos ingressantes a partir de 2014.1
			if (studentPeriod > 12){ //Não pode ultrapassar 12 semestres
				return true;
			}
		}
		
		for (Map.Entry<String, Integer> entry : courseSituationMap.entrySet()){
			//Verifica se a entrada de uma determinada matéria tem no mínimo 4 reprovações
			//E se o CRA é menor que 4 para defini-lo como jubilado
			if ((entry.getValue().compareTo(40) >= 0) && cra.compareTo(4.0d) < 0){
				return true;
			}
			
		}
		
		return false;
	}
	
	/**
	 * Verifica se o estudante precisa de integralização, para ingressantes pré e pós 2014.1 as regras são diferentes
	 * @return Informa a situação da necessidade de integralização
	 */
	public String needIntegralization(){
		if (needIntegralization(getRegistry(), getCurrentPeriod())){
			return "O aluno deve apresentar plano de integralização";
		}
		return "O aluno não deve apresentar plano de integralização";
	}
	
	/**
	 * Verifica se o estudante precisa de integralização, para ingressantes pré e pós 2014.1 as regras são diferentes
	 * @param registry Matrícula do estudante
	 * @return Booleano informando a situação da necessidade de integralização
	 */
	private Boolean needIntegralization(String registry, Integer studentPeriod){
		Integer year = Integer.parseInt(registry.substring(0, 4));
		
		if (year.compareTo(2013) <= 0){ //Checagem caso o aluno seja ingressante até 2013.2
			if (studentPeriod >= 11){ //Pedem integralização a partir do sexto ano
				return true;
			}
		} else { //Para os alunos ingressantes a partir de 2014.1
			if (studentPeriod >= 7){ //Pedem integralização a partir do sétimo período
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Verifica se as notas do estudante estão acima do mínimo requerido de 5.0 como CR em um período de integralização
	 * @return Informa a situação do estudante quanto as notas mínimas no período de integralização
	 */
	public String gradesAboveIntegralizationLimit(){
		if(gradesAboveIntegralizationLimit(getCRsPerPeriod(), getRegistry(), getCurrentPeriod())){
			return "O aluno não tem problemas com CR abaixo do limite no plano de integralização";
		}
		return "O aluno está com o CR abaixo do mínimo requerido no plano de integralização";
	}
	
	/**
	 * Verifica se as notas do estudante estão acima do mínimo requerido de 5.0 como CR em um período de integralização
	 * @param listCRs Lista de Coeficientes de Rendimento dos períodos do estudante
	 * @param registry Matrícula do estudante
	 * @return Booleano informando a situação do estudante quanto as notas mínimas no período de integralização
	 */
	private Boolean gradesAboveIntegralizationLimit(ArrayList<Double> listCRs, String registry, Integer studentPeriod){
		Integer year = Integer.parseInt(registry.substring(0, 4));
		
		if (needIntegralization(registry, studentPeriod).equals(false)){
			return true;
		}
		
		if (year.compareTo(2013) <= 0){ //Checagem caso o aluno seja ingressante até 2013.2
			for (int i = 10; i < listCRs.size(); i++){
				if (listCRs.get(i).compareTo(5.0d) < 0){ //Verifica os períodos a partir do tempo de integralização se possuem CR < 5
					return false;
				}
			}
		} else { //Para os alunos ingressantes a partir de 2014.1
			for (int i = 6; i < listCRs.size(); i++){
				if (listCRs.get(i).compareTo(5.0d) < 0){ //Verifica os períodos a partir do tempo de integralização se possuem CR < 5
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Verifica se o mínimo de matérias em curso nesse período vigente está sendo cumprido de acordo com as normas
	 * @return Informa se o aluno está matriculado na quantidade de matérias suficientes
	 */
	public String hasMinimumActiveCourses(){
		if(hasMinimumActiveCourses(numberOfActiveCourses(), numberOfApprovedCourses())){
			return "O aluno está matriculado em matérias suficientes";
		}
		return "O aluno não está matriculado em matérias suficientes";
	}
	
	/**
	 * Verifica se o mínimo de matérias em curso nesse período vigente está sendo cumprido de acordo com as normas
	 * @param numberOfActiveCourses Número de matérias em curso no período vigente
	 * @param numberOfApprovedCourses Número de matérias aprovadas até o periodo atual
	 * @return Retorna True se o estudante cursa no mínimo 3 matérias ou é formando, e False para o restante
	 */
	private Boolean hasMinimumActiveCourses(Integer numberOfActiveCourses, Integer numberOfApprovedCourses){
		if ((numberOfActiveCourses < 3) && (numberOfApprovedCourses < (totalNumberOfCourses - 2))){
			return false;
		}
		return true;
	}
	
	/**
	 * Verifica se o aluno tem condições de se formar dentro do prazo regular
	 * @return Informa a situação do aluno quanto a terminar no prazo regular
	 */
	public String canGraduateWithinTimeLimit(){
		if(canGraduateWithinTimeLimit(getCurrentPeriod(), numberOfApprovedCourses())){
			return "O aluno tem condições de se formar no prazo regular";
		}
		return "O aluno não tem condições de se formar no prazo regular";
	}
	
	/**
	 * Verifica se o aluno tem condições de se formar dentro do prazo regular
	 * @param currentPeriod Período atual do aluno
	 * @param numberOfApprovedCourses Número de matérias em que o aluno já foi aprovado
	 * @return Retorna True se o aluno tiver como cumprir um máximo de 7 matérias vezes o número de períodos faltantes até o oitavo período,
	 *  e completar o número máximo de matérias exigidas pelo curso, e False caso contrário
	 */
	private Boolean canGraduateWithinTimeLimit(Integer currentPeriod, Integer numberOfApprovedCourses){
		if (((totalNumberOfCourses - numberOfApprovedCourses)) > ((8 - (currentPeriod - 1)) * 7)){
			return false;
		}
		
		return true;
	}

	
}
