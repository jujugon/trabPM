package br.uniriotec.svg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.uniriotec.history.EducationalHistory;

public class SVGBuilder {
	
	private HashMap<String, SVGBlockPosition> courseBlockPosMap;
	private final String blockWidth = "104";
	private final String blockHeight = "53";
	private final String approvedBlockStyle = "fill:#00FF00;fill-opacity:.5";
	private final String failedBlockStyle = "fill:#FF0000;fill-opacity:.5";
	
	
	public void loadBlockPositions() throws IOException{
		HashMap<String, SVGBlockPosition> courseBlockPosMap = new HashMap<String, SVGBlockPosition>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(SVGBuilder.class.getResourceAsStream("PosPerCourseBlock.txt")));
		String line = "", courseCode = "";
		SVGBlockPosition courseBlock = new SVGBlockPosition();
		String x = "0", y = "0";
		
		while ((line = reader.readLine()) != null){
			if (line.contains("COD=")){
				if (y != "0"){             //Checagem para que o hashmap só seja preenchido após o primeiro ciclo da matéria e as posições de X e Y
					courseBlockPosMap.put(courseCode, courseBlock);
				}
				courseCode = line.substring(4);
				courseBlock = new SVGBlockPosition();
			} else if (line.contains("X=")){
				x = line.substring(2);
				courseBlock.setX(x);
			} else {
				y = line.substring(2);
				courseBlock.setY(y);
			}
		}
		
		reader.close();
		
		this.setCourseBlockPosMap(courseBlockPosMap);
		
	}
	
	public void build(HashMap<String, Integer> courseSituationMap, Document document){
        String x = "0", y = "0";
		int optNumber = 1; //Contador de optativas
		int eleNumber = 1; //Contador de eletivas
		
        Element root = document.getDocumentElement();
        root.setAttributeNS(null, "width", "1075");
        root.setAttributeNS(null, "height", "650");
        
		
		for (Map.Entry<String, Integer> entry : courseSituationMap.entrySet()) {
			if (courseBlockPosMap.containsKey(entry.getKey())) {
				x = courseBlockPosMap.get(entry.getKey()).getX();
				y = courseBlockPosMap.get(entry.getKey()).getY();

				if ((entry.getValue() % 2 == 0) && entry.getValue() >= 10) {
					root.appendChild(createRedBlockRectangle(document, x, y));
				} else if ((entry.getValue() % 2 == 1)) {
					root.appendChild(createGreenBlockRectangle(document, x, y));
				} 	
			} else if (entry.getValue() % 2 == 1) {
				if ((entry.getKey().contains("TIN")) && optNumber < 9) {
					x = courseBlockPosMap.get("OPTATIVA0" + optNumber).getX();
					y = courseBlockPosMap.get("OPTATIVA0" + optNumber).getY();
					root.appendChild(createGreenBlockRectangle(document, x, y));
					optNumber++;
				} else if (eleNumber < 5) {
					x = courseBlockPosMap.get("ELETIVA0" + eleNumber).getX();
					y = courseBlockPosMap.get("ELETIVA0" + eleNumber).getY();
					root.appendChild(createGreenBlockRectangle(document, x, y));
					eleNumber++;
				}
			}
		}	
	}

	public Element createRedBlockRectangle(Document document, String x, String y){
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Element e;
		e = document.createElementNS(svgNS, "rect");
        e.setAttributeNS(null, "x", x);
        e.setAttributeNS(null, "y", y);
        e.setAttributeNS(null, "width", blockWidth);
        e.setAttributeNS(null, "height", blockHeight);
        e.setAttributeNS(null, "style", failedBlockStyle);     
        return e;
	}
	
	public Element createGreenBlockRectangle(Document document, String x, String y){
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Element e;
		e = document.createElementNS(svgNS, "rect");
        e.setAttributeNS(null, "x", x);
        e.setAttributeNS(null, "y", y);
        e.setAttributeNS(null, "width", blockWidth);
        e.setAttributeNS(null, "height", blockHeight);
        e.setAttributeNS(null, "style", approvedBlockStyle);     
        return e;
	}
	
	
	
	public HashMap<String, SVGBlockPosition> getCourseBlockPosMap() {
		return courseBlockPosMap;
	}

	public void setCourseBlockPosMap(HashMap<String, SVGBlockPosition> courseBlockPosMap) {
		this.courseBlockPosMap = courseBlockPosMap;
	}
}
