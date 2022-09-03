import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class old_BPF_Field {
	public String name;
	public int length;
	public int offset;
	public old_BPF_Field parent;
	public String lengthExpr;
	public String calcExpr;
	public int key;
	
	public old_BPF_Field() {
		this.name = null;
		this.length = 0;
		this.offset = 0;
		this.parent = null;
		this.lengthExpr = null;
		this.calcExpr = null;
		this.key = -1 ;
	}
	
	public old_BPF_Field(String name, int length, int offset) {
		this.name = name;
		this.length = length;
		this.offset = offset;
		this.parent = null;
		this.lengthExpr = null;
		this.calcExpr = null;
	}
	
	public old_BPF_Field(String name, int length, int offset, old_BPF_Field parent) {
		this.name = name;
		this.length = length;
		this.offset = offset;
		this.parent = parent;
		this.lengthExpr = null;
		this.calcExpr = null;
	}
	
	public old_BPF_Field(String name, int length, int offset, old_BPF_Field parent, String lengthExpr) {
		this.name = name;
		this.length = length;
		this.offset = offset;
		this.parent = parent;
		this.lengthExpr = lengthExpr;
		this.calcExpr = null;
	}
	
	public old_BPF_Field(String name, int length, int offset, old_BPF_Field parent, String lengthExpr, String calcExpr) {
		this.name = name;
		this.length = length;
		this.offset = offset;
		this.parent = parent;
		this.lengthExpr = lengthExpr;
		this.calcExpr = calcExpr;
	}
	
	
	public void setField(Element element) {
		if (element.getAttribute("name").equals("")){
			this.name = null;
		}
		this.name = element.getAttribute("name");
		parseLength(element);
		this.offset = parseOffset(element);
		parseParent(element);
		parseLengthExpr(element.getAttribute("length"));
		parseCalcExpr(element);
		if (!(element.getAttribute("key").equals(""))){
			this.key = Integer.parseInt(element.getAttribute("key"));
		}
	}
	
	
	public void parseLength(Element element) {
		if (DFDLParser.isNumeric(element.getAttribute("length"))) {
			this.length = Integer.parseInt(element.getAttribute("length"));
		}
	}
	
	public int parseOffset(Element element) {
		//offset of parent + offset of predecessor siblings
		int siblingsOffsets = 0;
		if (!(element.getParentNode() instanceof Element)) {
			return 0;
		}
		Element curParent = (Element) element.getParentNode();
		if (!curParent.getAttribute("name").equals("")) {
			NodeList siblings = curParent.getChildNodes();
			for (int i = 0 ; i < siblings.getLength(); i++) {
				siblingsOffsets += Integer.parseInt(((Element) siblings.item(i)).getAttribute("length"));
			}
		}
		return (parseOffset((Element) element.getParentNode()) + siblingsOffsets);
	}
	
	public void parseParent(Element element) {
		if (!(element.getParentNode() instanceof Element)) {
			this.parent = null;
		} else {
			Element curParent = (Element) element.getParentNode();
			if (!curParent.getAttribute("name").equals("")) {
				this.parent = new old_BPF_Field();
				this.parent.setField(curParent);
			}
		}
	}
	
	public void parseLengthExpr(String s) {
		if (!DFDLParser.isNumeric(s)) {
			this.lengthExpr = s;
		}
	}
	
	
	
	public String getParenthood() {
		if (this.parent != null) {
			return this.parent.getParenthood() + "." + this.getName();
		}
		return this.getName();
	}
	
	public String getSerialParenthood() {
		if (this.parent != null) {
			if (this.key != -1) {
				return this.parent.getSerialParenthood() + "." + this.key;
			}
			else {
				return this.parent.getSerialParenthood() + "." + this.getName();
					
			}
		}
		return this.getName();
	}
	
	
	
	public String toString(){
		if (this.parent != null) {
			return name + ", " + length + ", " + offset+"\t\t\tparent: " + this.getSerialParenthood();
		}
		return name + ", " + length + ", " + offset;
	}
	
	private String getName() {
		
		return this.name;
	}

	public String getHexOffset(String inputName) {
		if (inputName.equalsIgnoreCase(this.name)) {
			return "0x" + Integer.toHexString(this.offset);
		}
		return "";
	}
	
	public String getDecOffset(String inputName) {
		if (inputName.equalsIgnoreCase(this.name)) {
			return "[" + (this.offset) + "]";
		}
		return "";
	}
	
	public static String iptohex(String reqIpAddr) {
		String hex = "";
		String[] part = reqIpAddr.split("[\\.,]");
		if (part.length < 4) {
			return "00000000";
		}
		for (int i = 0; i < 4; i++) {
			int decimal = Integer.parseInt(part[i]);
			if (decimal < 16) // Append a 0 to maintain 2 digits for every
								// number
			{
				hex += "0" + String.format("%01x", decimal);
			} else {
				hex += String.format("%01x", decimal);
			}
		}
		return "#0x"+ hex;
	}
	
	public void parseCalcExpr(Element e) {
		if (!DFDLParser.isNumeric(e.getAttribute("length"))) {
			this.calcExpr = "";
		}
	}
	
}
