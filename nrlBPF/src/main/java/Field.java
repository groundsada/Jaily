

public class Field {
	public String name;
	public String parent;
	public int length;
	public int offset;
	
	public Field(String name, String parent, int length, int offset) {
		this.name = name;
		this.parent = parent;
		this.length = length;
		this.offset = offset;
	}
	
	public String getName() {
		return name;
	}
	public String getParent() {	
		return parent;
	}
	
	public String toString(){
		return name + ", " + parent + ", " + length + ", " + offset;
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
				hex += String.format("%01x", decimal);
			} else {
				hex += String.format("%01x", decimal);
			}
		}
		return "#0x"+ hex;
	}

	public int getByteOffset() {
		return (this.offset/8);
	}
	
	public int getByteLength() {
		return (this.length/8);
	}
	
	
	public int getParentValue() {
		String parentValue = this.parent.split("\\.")[1];
		return 
				Integer.parseInt(parentValue);
	}
	
	public int getParentOffset() throws ModelReferenceException {
		System.out.println(this.parent);
		String parentName = this.parent.split("\\.")[0];
		Field parentField = Application.searchField(parentName).get(0);
		return parentField.getByteOffset();
	}
	
}
