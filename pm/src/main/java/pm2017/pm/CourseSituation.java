package pm2017.pm;

public enum CourseSituation {
	APV,
	REP,
	REF,
	ASC,
	TRA,
	DIS;
	
	public static String getCourseSituation(String input){
		if (input.contains(CourseSituation.APV.toString())){
			return CourseSituation.APV.toString();
		} else if (input.contains(CourseSituation.REP.toString())){
			return CourseSituation.REP.toString();
		} else if (input.contains(CourseSituation.REF.toString())){
			return CourseSituation.REF.toString();
		} else if (input.contains(CourseSituation.ASC.toString())){
			return CourseSituation.ASC.toString();
		} else if (input.contains(CourseSituation.TRA.toString())){
			return CourseSituation.TRA.toString();
		} else if (input.contains(CourseSituation.DIS.toString())){
			return CourseSituation.DIS.toString();
		} else {
			return null;
		}
		
	}
}
