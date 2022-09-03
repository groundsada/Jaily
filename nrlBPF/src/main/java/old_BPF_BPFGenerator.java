
public class old_BPF_BPFGenerator {
	
	public static String loadEthernet() {
		return "ldh\t[12]\n";
	}
	
	public static String filterDrop() {
		return "keep: ret\t#-1\n" + 
				"drop: ret\t#0\n" + 
				"";
	}
	
	public static String binaryPredicateNonRecursive(String ethertype, String fieldOffset, String value){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld " +fieldOffset+ "\n" + 
				"jeq      " +value+ ", keep, drop\n";
	}
	
	public static String logicOrNonRecursive(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", keep, tf\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	
	public static String logicAndNonRecursive(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", tf, drop\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	
	public static String genIPethertype(Field field) {
		if (field.parent.equalsIgnoreCase("IPv6Header")){
			return "#0x" + Integer.toHexString(34525);
		} else {
			return "#0x" + Integer.toHexString(2048);
		}
	}
	
	public static String genIPethertype() {
		Field field = new Field("a", "IPv4", 10, 10);
		if (field.parent.equalsIgnoreCase("IPv6Header")){
			return "#0x" + Integer.toHexString(34525);
		} else {
			return "#0x" + Integer.toHexString(2048);
		}
	}
	
	//TODO
	
	public static String logicRecursive(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" +  "tr: ld       "+fieldOffset1+"\n" +  "jeq      "+value1+", keep, tf\n" + 
				"tf: ld       "+fieldOffset2+"\n" +  "jeq      "+value2+", keep, drop\n" +  "";
	}
	
	public static String test(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", keep, tf\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	public static String binaryOperatorStatement(String ethertype, String fieldOffset, String value){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld " +fieldOffset+ "\n" + 
				"jeq      " +value+ ", keep, drop\n";
	}
	
	public static String binaryAND(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", keep, tf\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	
	public static String binaryOR(String ethertype, String fieldOffset1, String value1, String fieldOffset2, String value2){
		return "jeq      "+ethertype+", tr, drop\n" + 
				"tr: ld       "+fieldOffset1+"\n" + 
				"jeq      "+value1+", tf, drop\n" + 
				"tf: ld       "+fieldOffset2+"\n" + 
				"jeq      "+value2+", keep, drop\n" + 
				"";
	}
	
}
