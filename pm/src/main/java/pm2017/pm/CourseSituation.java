package pm2017.pm;

public enum CourseSituation {
	APV,
	REP,
	REF,
	ASC,
	TRA,
	DIS;
	
	
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
