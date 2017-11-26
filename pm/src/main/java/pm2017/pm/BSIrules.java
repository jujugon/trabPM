package pm2017.pm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BSIrules {

	/**
	 * Contabiliza o período atual do estudante com base no ano de ingresso e o período atual
	 * @param registry Número de matrícula do estudante para obter ano de ingresso
	 * @return Número do período do estudante
	 */
	public static Integer getStudentCurrentPeriod(String registry){
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
	 * Verifica se o aluno está em situação de jubilamento, seguindo a norma de 7 anos limite para ingressantes até 2013.2
	 * e 6 anos para ingressantes a partir de 2014.1, e também verifica através da quantidade de reprovações em uma única matéria
	 * se o estudante também tiver um CRA abaixo de 4.
	 * @param mapaCodSit Hashmap do código da matéria atrelado ao inteiro da situação de reprovações
	 * @param cra Coeficiente de Rendimento Acumulado do estudante
	 * @param registry matrícula do estudante
	 * @return Booleano informando a situação de jubilamento do estudante
	 */
	public static Boolean isDismissed(HashMap<String, Integer> mapaCodSit, Double cra, String registry){
		Integer year = Integer.parseInt(registry.substring(0, 4)),
				studentPeriod;
		
		studentPeriod = getStudentCurrentPeriod(registry);
	
		if (year.compareTo(2013) <= 0){ //Checagem caso o aluno seja ingressante até 2013.2
			if (studentPeriod > 14){ //Não pode ultrapassar 7 anos
				return true;
			}
		} else { //Para os alunos ingressantes a partir de 2014.1
			if (studentPeriod > 12){ //Não pode ultrapassar 12 semestres
				return true;
			}
		}
		
		for (Map.Entry<String, Integer> entry : mapaCodSit.entrySet()){
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
	 * @param registry Matrícula do estudante
	 * @return Booleano informando a situação da necessidade de integralização
	 */
	public static Boolean needIntegralization(String registry){
		Integer year = Integer.parseInt(registry.substring(0, 4)),
				studentPeriod;
		
		studentPeriod = getStudentCurrentPeriod(registry);
		
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
	 * @param listCRs Lista de Coeficientes de Rendimento dos períodos do estudante
	 * @param registry Matrícula do estudante
	 * @return Booleano informando a situação do estudante quanto as notas mínimas no período de integralização
	 */
	public static Boolean gradesAboveIntegralizationLimit(ArrayList<Double> listCRs, String registry){
		Integer year = Integer.parseInt(registry.substring(0, 4));
		
		if (needIntegralization(registry).equals(false)){
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
	
/*	public static Boolean canGraduateWithinTimeLimit(String registry, ){
		
	}
	*/
	
}
