package pm2017.pm;

public enum CourseSituation {
	APV,
	REP,
	REF,
	ASC,
	TRA,
	DIS;
	
	
	/**
	 * Checa na string de entrada se há algum valor presente de situação, para retornar um valor respectivo para a situação encontrada
	 * @param input Uma linha da String do histórico
	 * @return Para situações de aprovação retorna 1, para situações de reprovação retorna 10, para as situações indiferentes retorna 0
	 */
	public static Integer getCourseSituation(String input){
		for (CourseSituation sit : CourseSituation.values()){
			if(input.contains(sit.toString())){
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
}
