package br.uniriotec.svg;


public class SVGBlockPosition {
		
	private String x;
	private String y;
	
	
	
	public SVGBlockPosition(String x, String y){
		this.x = x;
		this.y = y;
	}
	
	public SVGBlockPosition(){
		
	}

	
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
		
}
