package br.uniriotec.bsi;


import org.w3c.dom.Document;

import br.uniriotec.history.*;
import br.uniriotec.pdf.PDFExtractor;
import br.uniriotec.svg.DOMRasterizer;
import br.uniriotec.svg.SVGBuilder;

public class Main {

	
	public static void main(String[] args) throws Exception {
        	String path = "C:/Users/lucas/Desktop/historicoEscolarCRAprovados.pdf";
        	
        	BSIHistory bsiHistory = new BSIHistory(PDFExtractor.extractPageContent(path));
        	
        	System.out.println(bsiHistory.isDismissed());
        	
        	System.out.println(bsiHistory.needIntegralization());
        	
        	System.out.println(bsiHistory.gradesAboveIntegralizationLimit());
        	
        	System.out.println(bsiHistory.hasMinimumActiveCourses());
        	
        	System.out.println(bsiHistory.canGraduateWithinTimeLimit());
        	
        	System.out.println(bsiHistory.isAboveAverageCRA());
        	
    		
    		//Rasterizer responsável por criar a base do documento SVG e também por converter e salvar a saída em PNG
            DOMRasterizer rasterizer = new DOMRasterizer();
            Document document = rasterizer.createDocument("file:///C:/Users/lucas/Desktop/grade_curricular.svg");
            
            //SVG Builder para carregar as matérias e o cruzamento das situações para preenchimento no SVG
            SVGBuilder builder = new SVGBuilder();
            builder.loadBlockPositions();
            builder.build(bsiHistory.getCourseSituationMap(), document);
            
            
            rasterizer.save(document);
            
            System.exit(0);
			
      
	}
}
