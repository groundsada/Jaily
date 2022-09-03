
public class Snippets {
	
	public static String noParentLoadHalf(int offset){
		return "ldh ["+ offset+"]";
	}
	
	public static String noParentLoad(int offset){
		return "ld ["+ offset+"]";
	}
	
	public static String loadOffest(int offset, Field field) throws ImplementationError {
		//FIXME
		if (field.length == 16) {
			return noParentLoadHalf(offset);
		}
		else if (field.length == 32) {
			return noParentLoad(offset);
		}
		else {
			throw new ImplementationError();
		}
	}
	
	public static String noParentHalfBinaryPredicate(int offset , int value) {
		return label() + "ldh ["+offset+"]\n" + 
				label() + "jeq #"+value+" , "+jumpNext()+" , "+jumpDrop()+"\n" + 
				"";
	}
	
	public static String noParentFullBinaryPredicate(int offset , int value) {
		return label() + "ld ["+offset+"]\n" + 
				label() + "jeq #"+value+" , "+jumpNext()+" , "+jumpDrop()+"\n" + 
				"";
	}
	
	public static String parentedBinaryPredicateFull(int offset, int parentOffset, int parentValue) {
		return label()+ "ldh ["+parentOffset+"]\n" + 
				label()+"jeq #"+parentValue+" , "+jumpNextLine()+" , " +jumpNextPredicate()+"\n" + 
				label()+"ld ["+offset+"]";
	}
	public static String parentedBinaryPredicateHalf(int offset, int parentOffset, int parentValue) {
		return label()+ "ldh ["+parentOffset+"]\n" + 
				label()+"jeq #"+parentValue+" , "+jumpNextLine()+" , " +jumpNextPredicate()+"\n" + 
				label()+"ldh ["+offset+"]";
	}
	public static String parentedBinaryPredicateByte(int offset, int parentOffset, int parentValue) {
		return label()+ "ldh ["+parentOffset+"]\n" + 
				label()+"jeq #"+parentValue+" , "+jumpNextLine()+" , " +jumpNextPredicate()+"\n" + 
				label()+"ldb ["+offset+"]";
	}
	
	public static String predicateJump(long value, String predicate) throws ImplementationError {
		// using switch statements on strings will change project compilance, risking NRL library functionality
		if (predicate.equalsIgnoreCase("EQUAL")){
			return label()+"jeq #"+value+" , "+jumpNextPass()+" , " +jumpDrop()+"\n";
		} else if (predicate.equalsIgnoreCase("NOT_EQUAL")){
			return label()+"jeq #"+value+" , "+jumpDrop()+" , " +jumpNextPass()+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER")){
			return label()+"jgt #"+value+" , "+jumpNextPass()+" , " +jumpDrop()+"\n";
		} else if (predicate.equalsIgnoreCase("LESS_OR_EQUAL")){
			return label()+"jgt #"+value+" , "+jumpDrop()+" , " +jumpNextPass()+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER_OR_EQUAL")){
			return label()+"jge #"+value+" , "+jumpNextPass()+" , " +jumpDrop()+"\n";
		} else if (predicate.equalsIgnoreCase("LESS")){
			return label()+"jge #"+value+" , "+jumpDrop()+" , " +jumpNextPass()+"\n";
		}
		throw new ImplementationError();
		
	}
	
	public static String predicateJumpAnd(long value, String predicate, int i) throws ImplementationError {
		// using switch statements on strings will change project compilance, risking NRL library functionality
		if (predicate.equalsIgnoreCase("EQUAL")){
			return label()+"jeq #"+value+" , "+jumpNextI(i)+" , " +jumpDrop()+"\n";
		} else if (predicate.equalsIgnoreCase("NOT_EQUAL")){
			return label()+"jeq #"+value+" , "+jumpDrop()+" , " +jumpNextI(i)+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER")){
			return label()+"jgt #"+value+" , "+jumpNextI(i)+" , " +jumpDrop()+"\n";
		} else if (predicate.equalsIgnoreCase("LESS_OR_EQUAL")){
			return label()+"jgt #"+value+" , "+jumpDrop()+" , " +jumpNextI(i)+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER_OR_EQUAL")){
			return label()+"jge #"+value+" , "+jumpNextI(i)+" , " +jumpDrop()+"\n";
		} else if (predicate.equalsIgnoreCase("LESS")){
			return label()+"jge #"+value+" , "+jumpDrop()+" , " +jumpNextI(i)+"\n";
		}
		throw new ImplementationError();
		
	}
	
	public static String predicateJumpIfThen(long value, String predicate, int i) throws ImplementationError {
		// using switch statements on strings will change project compilance, risking NRL library functionality
		if (predicate.equalsIgnoreCase("EQUAL")){
			return label()+"jeq #"+value+" , "+jumpNextI(i)+" , " +jumpNextPass()+"\n";
		} else if (predicate.equalsIgnoreCase("NOT_EQUAL")){
			return label()+"jeq #"+value+" , "+jumpNextPass()+" , " +jumpNextI(i)+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER")){
			return label()+"jgt #"+value+" , "+jumpNextI(i)+" , " +jumpNextPass()+"\n";
		} else if (predicate.equalsIgnoreCase("LESS_OR_EQUAL")){
			return label()+"jgt #"+value+" , "+jumpNextPass()+" , " +jumpNextI(i)+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER_OR_EQUAL")){
			return label()+"jge #"+value+" , "+jumpNextI(i)+" , " +jumpNextPass()+"\n";
		} else if (predicate.equalsIgnoreCase("LESS")){
			return label()+"jge #"+value+" , "+jumpNextPass()+" , " +jumpNextI(i)+"\n";
		}
		throw new ImplementationError();
		
	}
	
	public static String predicateJumpOr(long value, String predicate, int i) throws ImplementationError {
		// using switch statements on strings will change project compilance, risking NRL library functionality
		if (predicate.equalsIgnoreCase("EQUAL")){
			return label()+"jeq #"+value+" , "+jumpNextPass()+" , " +jumpNextI(i)+"\n";
		} else if (predicate.equalsIgnoreCase("NOT_EQUAL")){
			return label()+"jeq #"+value+" , "+jumpNextI(i)+" , " +jumpNextPass()+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER")){
			return label()+"jgt #"+value+" , "+jumpNextPass()+" , " +jumpNextI(i)+"\n";
		} else if (predicate.equalsIgnoreCase("LESS_OR_EQUAL")){
			return label()+"jgt #"+value+" , "+jumpNextI(i)+" , " +jumpNextPass()+"\n";
		} 
		
		else if (predicate.equalsIgnoreCase("GREATER_OR_EQUAL")){
			return label()+"jge #"+value+" , "+jumpNextPass()+" , " +jumpNextI(i)+"\n";
		} else if (predicate.equalsIgnoreCase("LESS")){
			return label()+"jge #"+value+" , "+jumpNextI(i)+" , " +jumpNextPass()+"\n";
		}
		throw new ImplementationError();
		
	}
	
	public static String jumpNextI(int i) {
		return "rule"+Transpiler.globalRuleCounter +"p"+i+"line1";
	}
	
	public static String noParentBinaryPredicate(int offset, int value, Field field) throws ImplementationError {
		if (field.length == 32) {
			return noParentHalfBinaryPredicate(offset, value);
		}
		else if (field.length == 64) {
			return noParentFullBinaryPredicate(offset, value);
		}
		else {
			throw new ImplementationError();
		}
	}
	
	public static String jumpNext() {
		return "rule"+(Transpiler.globalRuleCounter + 1)+"p"+Transpiler.p+"line1";
	}
	
	public static String jumpNextLine() {
		return "rule"+Transpiler.globalRuleCounter+"p"+Transpiler.p+"line"+Transpiler.lineCount;
	}
	
	public static String jumpNextPredicate() {
		return "rule"+Transpiler.globalRuleCounter+"p"+(Transpiler.p+1)+"line1";
	}
	
	public static String jumpNextPass() {
		return "rule"+(Transpiler.globalRuleCounter + 1)+"p1line1";
	}
	
	
	public static String jumpDrop() {
		return "drop";
	}
	
	public static String label() {
		String label = "rule"+Transpiler.globalRuleCounter+"p"+Transpiler.p+"line"+Transpiler.lineCount+": ";
		Transpiler.incrementLineCount();
		return label;
	}
	
	public static String writeRule(String offset, String value) {
		return label() + "ldx #" + offset + "\n"
				+ label() + "ld " + value + "\n"
				 + label() + "stb [x+0]\n";
	}
	
	
	public static String setRule(int offset, String expression) {
		return label()+"ldx #"+offset + "\n" + label() + "ld #" + expression + "\n" + label() +"st [x+0]\n";
	}
	
	/* Extra helpful strings for testing */
	
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
}
