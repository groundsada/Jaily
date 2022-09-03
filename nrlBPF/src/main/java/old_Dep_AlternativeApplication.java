

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class old_Dep_AlternativeApplication {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        File inputFile = new File("schema.xsd");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("xs:element");
        
        NodeList protocols = null;
        int offset = 0;
        String parent = "";
        ArrayList<Field> fieldList = new ArrayList<Field>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Element x = (Element) nNode;
            
            
            if (x.getAttribute("protocolHeader").equalsIgnoreCase("1")) {
            	offset = 112;
            	parent = x.getAttribute("name");
            }
            
            System.out.println(x.getAttribute("name") +"\t|||\t" +x.getAttribute("dfdl:length"));
            try {
            if (x.getAttribute("dfdl:length") != "") {
            	int mult = 1;
            	if (x.getAttribute("type").equalsIgnoreCase("b:hexbyte")) {
            		mult = 8;
            	}
            	
            	fieldList.add(new Field(x.getAttribute("name"), parent, (Integer.parseInt(x.getAttribute("dfdl:length"))*mult)/8, offset/8));
            	offset += (Integer.parseInt(x.getAttribute("dfdl:length"))*mult);
            }
            else if ( x.getAttribute("name").contains("IPSrc") ||x.getAttribute("name").contains("IPDest") ) {
            	fieldList.add(new Field(x.getAttribute("name"), parent, 4, offset/8));
            	offset += 32;
            }
            } catch(Exception e) {
            	
            }
            System.out.println("offset: " + offset);
            
            
    
        }
        
        for (int i = 0 ; i <fieldList.size(); i++) {
        	System.out.println(fieldList.get(i));
        	System.out.println(fieldList.get(i).getDecOffset("IPSrc"));
        	System.out.println(old_BPF_BPFGenerator.genIPethertype(fieldList.get(i)));
        }
        
        String BPF = "";
	}
	
	public static ArrayList<Field> getFields() throws ParserConfigurationException, SAXException, IOException{
		File inputFile = new File("schema.xsd");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("xs:element");
        
        NodeList protocols = null;
        int offset = 0;
        String parent = "";
        ArrayList<Field> fieldList = new ArrayList<Field>();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Element x = (Element) nNode;
            
            
            if (x.getAttribute("protocolHeader").equalsIgnoreCase("1")) {
            	offset = 112;
            	parent = x.getAttribute("name");
            }
            
            System.out.println(x.getAttribute("name") +"\t|||\t" +x.getAttribute("dfdl:length"));
            try {
            if (x.getAttribute("dfdl:length") != "") {
            	int mult = 1;
            	if (x.getAttribute("type").equalsIgnoreCase("b:hexbyte")) {
            		mult = 8;
            	}
            	
            	fieldList.add(new Field(x.getAttribute("name"), parent, (Integer.parseInt(x.getAttribute("dfdl:length"))*mult)/8, offset/8));
            	offset += (Integer.parseInt(x.getAttribute("dfdl:length"))*mult);
            }
            else if ( x.getAttribute("name").contains("IPSrc") ||x.getAttribute("name").contains("IPDest") ) {
            	fieldList.add(new Field(x.getAttribute("name"), parent, 4, offset/8));
            	offset += 32;
            }
            } catch(Exception e) {
            	
            }
            System.out.println("offset: " + offset);
            
            
    
        }
        
        for (int i = 0 ; i <fieldList.size(); i++) {
        	System.out.println(fieldList.get(i));
        	System.out.println(fieldList.get(i).getDecOffset("IPSrc"));
        	System.out.println(old_BPF_BPFGenerator.genIPethertype(fieldList.get(i)));
        }
        
        return fieldList;
	}
}
