package br.uniriotec.svg;

import java.io.*;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.w3c.dom.Document;

public class DOMRasterizer {

    public Document createDocument(String uri) throws IOException {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory( parser );
        Document document = factory.createDocument( uri );
        
        return document;
    }

    public void save(Document document) throws Exception {
        JPEGTranscoder t = new JPEGTranscoder();
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                             new Float(.8));
        TranscoderInput input = new TranscoderInput(document);
        OutputStream ostream = new FileOutputStream("out.jpg");
        TranscoderOutput output = new TranscoderOutput(ostream);
        t.transcode(input, output);
        ostream.flush();
        ostream.close();
    }

    public static void main(String [] args) throws Exception {
        DOMRasterizer rasterizer = new DOMRasterizer();
        Document document = rasterizer.createDocument("file:///C:/Users/lucas/Desktop/grade_curricular.svg");
        rasterizer.save(document);
        System.exit(0);
    }
}