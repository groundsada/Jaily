// Objective: a parser method that parses a DFDL schema into a fields table

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class DFDLParser {
	
	public static String getEthernetDFDLSpecPath() {
		try {
			File pcapFile = new File("src/main/resources/PCAPDFDL/pcap.dfdl.xsd");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(pcapFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("xs:import");
			for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element x = (Element) nNode;
	            if (x.getAttribute("namespace").equalsIgnoreCase("urn:ethernet")) {
	            	return "src/main/resources/PCAPDFDL/"+x.getAttribute("schemaLocation");
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void printDomXml(Document doc) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		// initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		System.out.println(xmlString);
	}
	
	private static String getXPath(Node node) {
	    Node parent = node.getParentNode();
	    if (parent == null) {
	        return node.getNodeName();
	    }
	    return getXPath(parent) + "/" + node.getNodeName();
	}
	
	private static String getDFDLXPath(Node node) {
	    Node parent = node.getParentNode();
	    if (parent == null) {
	        return "#document";
	        
	    }
	    return getDFDLXPath(parent) + "/" + ((Element) node).getAttribute("name");
	}
	
	public static String currentParent = null;
	public static ArrayList<String> listOfParent = new ArrayList<String>();
	
	public static void appendValidChildren(Node mainRoot, Document doc, Document simplifiedDOM) throws XPathExpressionException {
		XPath xPath =  XPathFactory.newInstance().newXPath();
	    Node root = mainRoot;
		//String expression = "//*[@length]";
	    //NodeList nodeList = doc.getElementsByTagName("*");
	    //NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
	    NodeList nodeList = doc.getElementsByTagName("*");
		for(int i = 0 ; i < nodeList.getLength(); i++) {
	        //System.out.println(getXPath(nodeList.item(i)));
	    	//System.out.println(((Element) nodeList.item(i)).getAttribute("type"));
			Element oldEl = (Element) nodeList.item(i);
	        if (oldEl.hasAttribute("dfdl:length")){
			
	        	Element newNode = simplifiedDOM.createElement("element");
	        	if (oldEl.getAttribute("name").equalsIgnoreCase("value")) {
	        		newNode.setAttribute("name", ((Element) oldEl.getParentNode().getParentNode().getParentNode()).getAttribute("name"));
	        	} else {
	        		newNode.setAttribute("name", oldEl.getAttribute("name"));
	        	}
	        	newNode.setAttribute("type", oldEl.getAttribute("type"));
	    		newNode.setAttribute("length", oldEl.getAttribute("dfdl:length"));
	    		simplifiedDOM.adoptNode(newNode);
	        	root.appendChild(newNode);	
	        }
	        if (oldEl.hasAttribute("dfdl:choiceDispatchKey")) {
	        	//we have found a choice
	        	//each nested element is a branch
	        	// we need to extract the parent node
	        	String parentNodeName = oldEl.getAttribute("dfdl:choiceDispatchKey");
	        	parentNodeName = parentNodeName.split("/")[1].split("\\)")[0];
	        	//System.out.println(parentNodeName);
	        	//now we have the parent, we need the kids
	        	currentParent = parentNodeName;
	        } else if (oldEl.hasAttribute("dfdl:choiceBranchKey")) {
	        	//System.out.println(oldEl.getAttribute("name") + " : " + currentParent);
	        	// we have found the child, we create its branch and append under it
	        	// we need to get the parent node in the new document
	        	
	        	String expression = "//element[@name='" +currentParent + "']";
	        	NodeList viableParents = (NodeList) xPath.compile(expression).evaluate(simplifiedDOM, XPathConstants.NODESET);
	        	Node theParentNode = viableParents.item(viableParents.getLength() - 1); //the last one is the correct one
	        	
	        	Element mockNode = simplifiedDOM.createElement("element");
	        	mockNode.setAttribute("length", "0");
	        	mockNode.setAttribute("type", "b:bit");
	        	mockNode.setAttribute("name", oldEl.getAttribute("name"));
	        	mockNode.setAttribute("key", oldEl.getAttribute("dfdl:choiceBranchKey"));
	        	theParentNode.appendChild(mockNode);
	        	listOfParent.add(oldEl.getAttribute("name"));
	        	//System.out.println("NOW list is: " + listOfParent);
	        } else if (listOfParent.contains(oldEl.getAttribute("name"))) {
	        	String expression = "//element[@name='" +oldEl.getAttribute("name") + "']";
	        	NodeList viableParents = (NodeList) xPath.compile(expression).evaluate(simplifiedDOM, XPathConstants.NODESET);
	        	Node theParentNode = viableParents.item(viableParents.getLength() - 1);
	        	root = theParentNode;
	        }
	    }
		
		DFDLChildDOMTree(mainRoot, doc, simplifiedDOM);
		parseDOMXpathLengthExpressions(mainRoot, simplifiedDOM);
	}
	
	// This is the required DOM method (1)
	
	public static Document parse() {
		try {
			File ethernetFile = new File(getEthernetDFDLSpecPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ethernetFile);
			doc.getDocumentElement().normalize();
			Document simplifiedDOM = dBuilder.newDocument();
		    Node root = simplifiedDOM.createElement("schema");
		    simplifiedDOM.appendChild(root);
		    appendValidChildren(root, doc, simplifiedDOM);
			simplifiedDOM.normalize();
			return(simplifiedDOM);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			File ethernetFile = new File(getEthernetDFDLSpecPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ethernetFile);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("xs:complexType");
			
			//OLD PARSING
			
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//	            Node nNode = nList.item(temp);
//	            Element x = (Element) nNode;
//	            //nNode.getParentNode().removeChild(nNode);
//	            //System.out.println(x.getAttribute("name") + ", " + x.getAttribute("type")+ ", "+ x.getAttribute("dfdl:length"));
//	            //System.out.println(x.getTextContent() + "\n");
//			}
//			
//			Element element = (Element) doc.getElementsByTagName("xs:complexType").item(1);
//			System.out.println(element.getAttribute("name"));
//			
//			for (int temp = 0 ; temp < element.getChildNodes().getLength();temp++) {
//				element.getParentNode().appendChild(element.getChildNodes().item(temp));
//			}
			//element.getParentNode().removeChild(element);
			//element.getParentNode().removeChild(element);
			
			
//			
//			NodeList nodeList = doc.getElementsByTagName("*");
//			
//		    for (int i = 0; i < nodeList.getLength(); i++) {
//		        Element el = (Element) nodeList.item(i);
//		        if (el.getAttribute("name") != "") {
//		        	System.out.println(">> " + el.getAttribute("name"));
//		        }
//		    }
			
			
			
			
		    //let's go through the xml, and create a new xml
		    Document simplifiedDOM = dBuilder.newDocument();
		    Node root = simplifiedDOM.createElement("schema");
		    simplifiedDOM.appendChild(root);
		    
		    
//		    XPath xPath =  XPathFactory.newInstance().newXPath();
//		    String expression = "//*[@name='Protocol']";
//		    //NodeList nodeList = doc.getElementsByTagName("*");
//		    NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
//		    for(int i = 0 ; i < nodeList.getLength(); i++) {
//		        //System.out.println(getXPath(nodeList.item(i)));
//		    	System.out.println(((Element) nodeList.item(i)).getAttribute("type"));
//		    	System.out.println(">>" + getXPath(nodeList.item(i)));
//		    	//Element newNode = simplifiedDOM.createElement("element");
//		    	//newNode.setAttribute("name", ((Element) nodeList.item(i)).getAttribute("name"));
//		    	//newNode.setAttribute("type", ((Element) nodeList.item(i)).getAttribute("type"));
//		    	//newNode.setAttribute("length", ((Element) nodeList.item(i)).getAttribute("dfdl:length"));
//		        //simplifiedDOM.adoptNode(newNode);
//		        //root.appendChild(newNode);
//		    }
		    
		    
		    appendValidChildren(root, doc, simplifiedDOM);
		
		    
			doc.normalize();
			simplifiedDOM.normalize();
			
			//TO PRINT XML
			printDomXml(simplifiedDOM);
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Deprecated
	/*
	public static void parse(String[] args) throws ParserConfigurationException, SAXException, IOException {
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
        	System.out.println(BPFGen.genIPethertype(fieldList.get(i)));
        }
        
        String BPF = "";
	}
	
	@Deprecated
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
        	System.out.println(BPFGen.genIPethertype(fieldList.get(i)));
        }
        
        return fieldList;
	}
	*/
	
	public static void DFDLChildDOMTree(Node mainRoot, Document doc, Document simplifiedDOM) throws XPathExpressionException{
		//System.out.println("DFDLChildDOMTree is reachable");
		
		//XPath xPath =  XPathFactory.newInstance().newXPath();
		//String XPathExpr = "//element[@name='IPv4']/element[@name='Protocol']/element";
    	//NodeList XPathExprNodeList = (NodeList) xPath.compile(XPathExpr).evaluate(simplifiedDOM, XPathConstants.NODESET);
    	//for (int i = 0 ; i < XPathExprNodeList.getLength(); i++) {
    	//	XPathExprNodeList.item(i).getParentNode().removeChild(XPathExprNodeList.item(i));
    	//}
    	
//    	System.out.println(">>>>>>>>"+((Element) XPathExprNodeList.item(0).getFirstChild()).getAttribute("name"));
//    	Node theParentNode = XPathExprNodeList.item(viableParents.getLength() - 1); //the last one is the correct one
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		
		String XPathExpr = "//*[@name='IPv4']/*[@name='Protocol']";
		String XPathExpr1 = "//*[@name='TCP']";
		String XPathExpr2 = "//*[@name='UDP']";
		String XPathExpr3 = "//*[@name='ICMPv4']";
		String XPathExpr4 = "//*[@name='TransportLayer']";
		String XPathExpr5 = "//*[@name='IPv4']";
		String XPathExpr6 = "//*[@name='IPv6']";
		String XPathExpr7 = "//*[@name='IPSrc']";
		String XPathExpr8 = "//*[@name='IPDest']";
		String XPathExpr9 = "//*[@name='ICMPv4']";
    	String XPathExpr10 = "//*[@name='TransportLayer']";
    	String XPathExpr11 = "//*[@name='ICMPv4']";

		
		NodeList XPathExprNodeList = (NodeList) xPath.compile(XPathExpr).evaluate(simplifiedDOM, XPathConstants.NODESET);
		NodeList XPathExprNodeList1 = (NodeList) xPath.compile(XPathExpr1).evaluate(simplifiedDOM, XPathConstants.NODESET);
		NodeList XPathExprNodeList2 = (NodeList) xPath.compile(XPathExpr2).evaluate(simplifiedDOM, XPathConstants.NODESET);
		NodeList XPathExprNodeList3 = (NodeList) xPath.compile(XPathExpr3).evaluate(simplifiedDOM, XPathConstants.NODESET);
		NodeList XPathExprNodeList4 = (NodeList) xPath.compile(XPathExpr4).evaluate(simplifiedDOM, XPathConstants.NODESET);
		
		Node theProtocolNode = XPathExprNodeList.item(0);
		Node backup = theProtocolNode.cloneNode(true);
		Element theTCPHolder = (Element) XPathExprNodeList1.item(0);

		Element theUDPHolder = (Element) XPathExprNodeList2.item(0);
		
		
    	Element theICMPHolder = (Element) XPathExprNodeList3.item(0);
		Element theICMPHolder2 = (Element) XPathExprNodeList3.item(1);
		Element backup2 = (Element) theICMPHolder2.cloneNode(true);
		
		theTCPHolder.getParentNode().removeChild(theTCPHolder);
		theUDPHolder.getParentNode().removeChild(theUDPHolder);
		//theICMPHolder.getParentNode().removeChild(theICMPHolder);
		theICMPHolder2.getParentNode().removeChild(theICMPHolder2);
		
		for (int i = 0 ; i < XPathExprNodeList4.getLength();i++) {
			Element toRemove1 = (Element) XPathExprNodeList4.item(i);
			toRemove1.getParentNode().removeChild(toRemove1);
			
		}

		NodeList XPathExprNodeList5 = (NodeList) xPath.compile(XPathExpr5).evaluate(simplifiedDOM, XPathConstants.NODESET);
		Element toAdd1 = (Element) XPathExprNodeList5.item(0);
		
		NodeList XPathExprNodeList6 = (NodeList) xPath.compile(XPathExpr6).evaluate(simplifiedDOM, XPathConstants.NODESET);
		Element toAdd2 = (Element) XPathExprNodeList6.item(0);

		NodeList XPathExprNodeList7 = (NodeList) xPath.compile(XPathExpr7).evaluate(simplifiedDOM, XPathConstants.NODESET);
		Element toAdd3 = (Element) XPathExprNodeList7.item(0);
		
		NodeList XPathExprNodeList8 = (NodeList) xPath.compile(XPathExpr8).evaluate(simplifiedDOM, XPathConstants.NODESET);
		Element toAdd4 = (Element) XPathExprNodeList8.item(0);
		
		toAdd1.appendChild(toAdd3.cloneNode(true));
		toAdd1.appendChild(toAdd4.cloneNode(true));
		toAdd1.appendChild(backup);
		toAdd1.appendChild(backup2);
		toAdd2.appendChild(backup.cloneNode(true));
		
		NodeList XPathExprNodeList9 = (NodeList) xPath.compile(XPathExpr9).evaluate(simplifiedDOM, XPathConstants.NODESET);
    	//System.out.println(">>>>" + XPathExprNodeList9.getLength());
		
    	for (int i = 0 ; i < XPathExprNodeList9.getLength(); i++) {
    		if (i != 2) { XPathExprNodeList9.item(i).getParentNode().removeChild(XPathExprNodeList9.item(i));};
    	}
    	
    	NodeList XPathExprNodeList10 = (NodeList) xPath.compile(XPathExpr10).evaluate(simplifiedDOM, XPathConstants.NODESET);
    	for (int i = 0 ; i < XPathExprNodeList10.getLength();i++){
    		XPathExprNodeList10.item(i).getParentNode().removeChild(XPathExprNodeList10.item(i));
    	}
    	NodeList XPathExprNodeList11 = (NodeList) xPath.compile(XPathExpr11).evaluate(simplifiedDOM, XPathConstants.NODESET);
    	Node type = XPathExprNodeList11.item(0).getFirstChild();
    	type.removeChild(type.getFirstChild());
    	type.removeChild(type.getFirstChild());
    	XPathExprNodeList11.item(0).removeChild(XPathExprNodeList11.item(0).getLastChild());
    	XPathExprNodeList11.item(0).removeChild(XPathExprNodeList11.item(0).getLastChild());
    	XPathExprNodeList11.item(0).removeChild(XPathExprNodeList11.item(0).getLastChild());
    	
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int i = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static void parseDOMXpathLengthExpressions(Node mainRoot, Document simplifiedDOM) throws XPathExpressionException {
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String XPathExpr = "//*[@name='ICMPv4']";
		Element icmp = ((Element) ((NodeList) xPath.compile(XPathExpr).evaluate(simplifiedDOM, XPathConstants.NODESET)).item(0));
		icmp.setAttribute("length", "0");
		icmp.setAttribute("key", "1");
		
		NodeList simplifiedDomNodeList = (NodeList) xPath.compile("//*").evaluate(simplifiedDOM, XPathConstants.NODESET);
    	for (int i = 1 ; i < simplifiedDomNodeList.getLength();i++) {
    		
    		if (!isNumeric(((Element) simplifiedDomNodeList.item(i)).getAttribute("length"))) {
    			((Element) simplifiedDomNodeList.item(i)).setAttribute("length", "0");
    		}
    		
    		else if (!((Element) simplifiedDomNodeList.item(i)).getAttribute("type").equalsIgnoreCase("b:bit")){
    			((Element) simplifiedDomNodeList.item(i)).setAttribute("length", Integer.toString(8 * Integer.parseInt(((Element) simplifiedDomNodeList.item(i)).getAttribute("length"))));
    		}
    		
    	}
		
	}
	
}
