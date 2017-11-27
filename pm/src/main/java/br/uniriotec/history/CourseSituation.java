package br.uniriotec.history;

public enum CourseSituation {
	APV,
	REP,
	REF,
	ASC,
	TRA,
	DIS;
	
	
	/**
	 * Checa na string de entrada se há algum valor presente de situação, para retornar um valor respectivo para a situação encontrada
	 * @param line Uma linha da String do histórico
	 * @return Para situações de aprovação retorna 1, para situações de reprovação retorna 10, para as situações indiferentes retorna 0
	 */
	public static Integer getCourseSituation(String line){
		for (CourseSituation sit : CourseSituation.values()){
			if(line.contains(sit.toString())){
				if (sit.equals(APV) || sit.equals(DIS)){
					return 1;
				} else if (sit.equals(TRA) || sit.equals(ASC)){
					return 0;
				} else {
					return 10;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Checa na string de entrada de há um valor referente a situação de matrícula da matéria
	 * @param line Uma linah de string do histórico
	 * @return Retorna True para linha havendo uma matéria em situação de matrícula, e False caso contrário
	 */
	public static Boolean getCourseInProgress(String line){
		if(line.contains(ASC.toString())){
			return true;
		}
		return false;
	}
}
