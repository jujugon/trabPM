package br.uniriotec.svg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGBuilder {
	
	private HashMap<String, SVGBlockPosition> courseBlockPosMap;
	private final String blockWidth = "104";
	private final String blockHeight = "53";
	private final String approvedBlockStyle = "fill:#00FF00;fill-opacity:.5";
	private final String failedBlockStyle = "fill:#FF0000;fill-opacity:.5";
	
	/**
	 * Carrega para a classe as posições dos blocos respectivos as matérias através de um recurso TXT já definido
	 * @throws IOException Em caso de erro na leitura das linhas
	 */
	public void loadBlockPositions() throws IOException{
		HashMap<String, SVGBlockPosition> courseBlockPosMap = new HashMap<String, SVGBlockPosition>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(SVGBuilder.class.getResourceAsStream("PosPerCourseBlock.txt")));
		String line = "", courseCode = "";
		SVGBlockPosition courseBlock = new SVGBlockPosition();
		String x = "0", y = "0";
		
		while ((line = reader.readLine()) != null){
			if (line.contains("COD=")){
				courseCode = line.substring(4);
				courseBlock = new SVGBlockPosition();
			} else if (line.contains("X=")){
				x = line.substring(2);
				courseBlock.setX(x);
			} else {
				y = line.substring(2);
				courseBlock.setY(y);
				courseBlockPosMap.put(courseCode, courseBlock);
			}
		}
		
		reader.close();
		
		this.setCourseBlockPosMap(courseBlockPosMap);
		
	}
	
	/**
	 * Método para montagem do SVG com base nas matérias aprovadas e não aprovadas, e sobre um documento SVG de fluxograma predefinido
	 * @param courseSituationMap Mapa de relação de aprovação e reprovação das matérias
	 * @param document Documento SVG predefinido
	 */
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
				if ((entry.getKey().contains("TIN")) && optNumber < 9 && !isComplementary(entry.getKey())) {
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

	/**
	 * Cria um elemento de retângulo vermelho parcialmente transparente, na posição especificada pelos argumentos
	 * @param document Documento sobre o qual será montado o elemento
	 * @param x Eixo X para posicionamento do retângulo
	 * @param y Eixo Y para posicionamento do retângulo
	 * @return Elemento pronto para ser adicionado ao documento raiz
	 */
	private Element createRedBlockRectangle(Document document, String x, String y){
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
	
	/**
	 * Cria um elemento de retângulo verde parcialmente transparente, na posição especificada pelos argumentos
	 * @param document Documento sobre o qual será montado o elemento
	 * @param x Eixo X para posicionamento do retângulo
	 * @param y Eixo Y para posicionamento do retângulo
	 * @return Elemento pronto para ser adicionado ao documento raiz
	 */
	private Element createGreenBlockRectangle(Document document, String x, String y){
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
	
	
	/**
	 * Método necessário para filtrar os códigos de matérias consideradas formação complementar,
	 *  que possuem prefixo como de Optativas ou Eletivas
	 * @param entry Texto a ser verificado
	 * @return Retorna True se o código da matéria for de formação complementar, e False caso contrário
	 */
	public Boolean isComplementary(String entry){
		if(entry.contains("TIN0151") ||
				entry.contains("TIN0152") ||
				entry.contains("TIN0153") ||
				entry.contains("TIN0154")){
			return true;
		}
		return false;
	}
	
	
	
	public HashMap<String, SVGBlockPosition> getCourseBlockPosMap() {
		return courseBlockPosMap;
	}

	public void setCourseBlockPosMap(HashMap<String, SVGBlockPosition> courseBlockPosMap) {
		this.courseBlockPosMap = courseBlockPosMap;
	}
}
