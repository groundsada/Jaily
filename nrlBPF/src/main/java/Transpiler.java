import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.nrl.parser.ast.IDeclaration;
import net.sourceforge.nrl.parser.ast.IModelReference;
import net.sourceforge.nrl.parser.ast.IRuleDeclaration;
import net.sourceforge.nrl.parser.ast.IRuleFile;
import net.sourceforge.nrl.parser.ast.action.IAction;
import net.sourceforge.nrl.parser.ast.action.IActionFragmentApplicationAction;
import net.sourceforge.nrl.parser.ast.action.IActionRuleDeclaration;
import net.sourceforge.nrl.parser.ast.action.IAddAction;
import net.sourceforge.nrl.parser.ast.action.ICompoundAction;
import net.sourceforge.nrl.parser.ast.action.IConditionalAction;
import net.sourceforge.nrl.parser.ast.action.ICreateAction;
import net.sourceforge.nrl.parser.ast.action.IForEachAction;
import net.sourceforge.nrl.parser.ast.action.IOperatorAction;
import net.sourceforge.nrl.parser.ast.action.IRemoveAction;
import net.sourceforge.nrl.parser.ast.action.IRemoveFromCollectionAction;
import net.sourceforge.nrl.parser.ast.action.ISetAction;
import net.sourceforge.nrl.parser.ast.action.ISimpleAction;
import net.sourceforge.nrl.parser.ast.action.IVariableDeclarationAction;
import net.sourceforge.nrl.parser.ast.constraints.IArithmeticExpression;
import net.sourceforge.nrl.parser.ast.constraints.IBinaryOperatorStatement;
import net.sourceforge.nrl.parser.ast.constraints.IBinaryPredicate;
import net.sourceforge.nrl.parser.ast.constraints.IBooleanLiteral;
import net.sourceforge.nrl.parser.ast.constraints.ICastExpression;
import net.sourceforge.nrl.parser.ast.constraints.ICollectionIndex;
import net.sourceforge.nrl.parser.ast.constraints.IConstraint;
import net.sourceforge.nrl.parser.ast.constraints.IConstraintRuleDeclaration;
import net.sourceforge.nrl.parser.ast.constraints.IDecimalNumber;
import net.sourceforge.nrl.parser.ast.constraints.IExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IExpression;
import net.sourceforge.nrl.parser.ast.constraints.IForallStatement;
import net.sourceforge.nrl.parser.ast.constraints.IFunctionalExpression;
import net.sourceforge.nrl.parser.ast.constraints.IGlobalExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IIfThenStatement;
import net.sourceforge.nrl.parser.ast.constraints.IIntegerNumber;
import net.sourceforge.nrl.parser.ast.constraints.IIsInPredicate;
import net.sourceforge.nrl.parser.ast.constraints.IIsNotInPredicate;
import net.sourceforge.nrl.parser.ast.constraints.IIsSubtypePredicate;
import net.sourceforge.nrl.parser.ast.constraints.ILiteralString;
import net.sourceforge.nrl.parser.ast.constraints.IMultipleExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IMultipleNotExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.INotExistsStatement;
import net.sourceforge.nrl.parser.ast.constraints.IOperatorInvocation;
import net.sourceforge.nrl.parser.ast.constraints.ISelectionExpression;
import net.sourceforge.nrl.parser.ast.constraints.IValidationFragmentApplication;
import net.sourceforge.nrl.parser.ast.constraints.IVariableDeclaration;

public class Transpiler {
	
	public static String globalOutput = "";
	public static int globalRuleCounter = 1;
	public static int lineCount = 1;
	public static int p = 1; //predicate
	public static String ruleString = "";
	
	
	public static void transpileRuleFile(IRuleFile ruleFile) throws Exception {
		List<IDeclaration> rules = ruleFile.getDeclarations();
		for (int i = 0 ; i < rules.size(); i++) {
			IDeclaration rule_dec = rules.get(i);
			ruleString = "";
			
			ruleString += ";; Rule " + globalRuleCounter + ": Id: " + rule_dec.getId() + ", " + getSimpleType(rule_dec)+"\n";
			
			// here we handle the rules
			if (rule_dec instanceof IConstraintRuleDeclaration) {
				parseConstraintRuleDec(rule_dec);
			}
			else if (rule_dec instanceof IActionRuleDeclaration) {
				parseActionRule(rule_dec);
			}
			
			if (i == rules.size()-1) {
				// last one
				//TODO
			}
			
			ruleString += ruleJump();
			p=1;
			lineCount = 1;
			globalOutput += ruleString + "\n";
			globalRuleCounter++;
		}
		
		globalOutput += returnString();
		System.out.println(globalOutput);
		
		
//		for (IDeclaration rule_dec : ruleFile.getDeclarations()) {
//			if (rule_dec instanceof IConstraintRuleDeclaration) {
//				System.out.println("Constraint Rule " + rule_dec.getId());
//				System.out.println(getConstraintRuleType(rule_dec));
//			}
//			else if (rule_dec instanceof IActionRuleDeclaration) {
//				System.out.println("Action Rule " + rule_dec.getId());
//				System.out.println(getActionRuleType(rule_dec));
//				System.out.println(getActionsType(rule_dec));
//			}
//		}
	}
	
	
	//returns simple type of rule for commenting
	public static String getSimpleType(IDeclaration rule_dec) {
		if (rule_dec instanceof IConstraintRuleDeclaration) {
			return getSimpleTypeConstraint(rule_dec);
		}
		else if (rule_dec instanceof IActionRuleDeclaration) {
			return getSimpleTypeAction(rule_dec);
		}
		return null;
	}
	
	public static String getSimpleTypeConstraint(IDeclaration rule_dec) {
		if (rule_dec instanceof IConstraintRuleDeclaration) {
			return Application.getConstraintRuleType(rule_dec);
		}	
		return null;
	}
	
	public static String getSimpleTypeAction(IDeclaration rule_dec) {
		if (rule_dec instanceof IActionRuleDeclaration) {
			return Application.getActionsType(rule_dec);
		}
			
		return null;
	}
		
	public static void parseConstraintRuleDec(IDeclaration rule_dec) throws Exception {
		IConstraint constIndicator = ((IConstraintRuleDeclaration) rule_dec).getConstraint();
		parseConstraintRule(constIndicator);
	}
	
	public static void parseConstraintRule(IConstraint constIndicator) throws Exception {
		//TODO
		if (constIndicator instanceof IBinaryOperatorStatement) {
			parseIBinaryOperatorStatement(constIndicator);
		}
		else if (constIndicator instanceof IBinaryPredicate) {
			parseIBinaryPredicate(constIndicator);
		}
		
		else if (constIndicator instanceof IIfThenStatement) {
			parseIIfThenStatement(constIndicator);
		}
		
		else if (constIndicator instanceof IArithmeticExpression) {
			parseIArithmeticExpression(constIndicator);
		}
		
		else if (constIndicator instanceof IBooleanLiteral) {
			parseIBooleanLiteral(constIndicator);
		}
		
		else if (constIndicator instanceof ICastExpression) {
			parseICastExpression(constIndicator);
		}
		
		else if (constIndicator instanceof ICollectionIndex) {
			parseICollectionIndex(constIndicator);
		}
		
		else if (constIndicator instanceof IDecimalNumber) {
			parseIDecimalNumber(constIndicator);
		}
		
		else if (constIndicator instanceof IExistsStatement) {
			parseIExistsStatement(constIndicator);
		}
		
		else if (constIndicator instanceof IExpression) {
			
			parseIIfThenStatement(constIndicator );
		}
		
		else if (constIndicator instanceof IForallStatement) {
			parseIForallStatement(constIndicator );
		}
		
		else if (constIndicator instanceof IFunctionalExpression) {
			parseIFunctionalExpression(constIndicator );
		}
		
		else if (constIndicator instanceof IGlobalExistsStatement) {
			parseIGlobalExistsStatement(constIndicator );
		}
		
		else if (constIndicator instanceof IIntegerNumber) {
			parseIIntegerNumber(constIndicator );
		}
		
		else if (constIndicator instanceof IIsInPredicate) {
			parseIIsInPredicate(constIndicator );
		}
		
		else if (constIndicator instanceof IIsNotInPredicate) {
			parseIIsNotInPredicate(constIndicator );
		}
		
		else if (constIndicator instanceof IIsSubtypePredicate) {
			parseIIsSubtypePredicate(constIndicator );
		}

		else if (constIndicator instanceof ILiteralString) {
			parseILiteralString(constIndicator );
		}
		else if (constIndicator instanceof IModelReference) {
			parseIModelReference(constIndicator );
		}
		else if (constIndicator instanceof IMultipleExistsStatement) {
			parseIMultipleExistsStatement(constIndicator );
		}
		else if (constIndicator instanceof IMultipleNotExistsStatement) {
			parseIMultipleNotExistsStatement(constIndicator );
		}
		else if (constIndicator instanceof INotExistsStatement) {
			parseINotExistsStatement(constIndicator );
		}
		else if (constIndicator instanceof IOperatorInvocation) {
			parseIOperatorInvocation(constIndicator );
		}
		else if (constIndicator instanceof ISelectionExpression) {
			parseISelectionExpression(constIndicator );
		}
		else if (constIndicator instanceof IValidationFragmentApplication) {
			parseIValidationFragmentApplication(constIndicator );
		}
		else if (constIndicator instanceof IVariableDeclaration) {
			parseIVariableDeclaration(constIndicator );
		}
	}
	
	public static void parseActionRule(IDeclaration rule_dec ) throws Exception {
		//TODO
		ICompoundAction compAction = (ICompoundAction) ((IActionRuleDeclaration) rule_dec).getAction();
		List<ISimpleAction> actions = compAction.getSimpleActions();
		for (ISimpleAction action : actions) {
			if (action instanceof IActionFragmentApplicationAction) {
				parseIActionFragmentApplicationAction(action );
			} else if (action instanceof IAddAction) {
				parseIAddAction(action );
			} else if (action instanceof IConditionalAction) {
				parseIConditionalAction(action );
			} else if (action instanceof ICreateAction) {
				parseICreateAction(action );
			} else if (action instanceof IForEachAction) {
				parseIForEachAction(action );
			} else if (action instanceof IOperatorAction) {
				parseIOperatorAction(action );
			} else if (action instanceof IRemoveAction) {
				parseIRemoveAction(action );
			} else if (action instanceof IRemoveFromCollectionAction) {
				parseIRemoveFromCollectionAction(action );
			} else if (action instanceof ISetAction) {
				parseISetAction(action );
			} else if (action instanceof IVariableDeclarationAction) {
				parseIVariableDeclarationAction(action );
			}
		}
	}
	
	public static void parseIBinaryOperatorStatement(IConstraint constIndicator ) throws Exception{ 
		IBinaryOperatorStatement constraint = (IBinaryOperatorStatement) constIndicator;
		IConstraint leftConstraint = constraint.getLeft();
		IConstraint rightConstraint = constraint.getRight();
		String operator = constraint.getOperator().toString();
		//no switch statements for strings for java nrl compliance
		if (leftConstraint instanceof IBinaryPredicate && rightConstraint instanceof IBinaryPredicate) {
			parseTwoBinaryPredicates(operator, leftConstraint, rightConstraint);
		} else {
			throw new ImplementationError();
		}
	}
	
	public static void parseTwoBinaryPredicates(String operator, IConstraint leftConstraint, IConstraint rightConstraint) throws Exception {
		if (operator.equalsIgnoreCase("and")) {
			parseLeftAnd(leftConstraint);
			parseRightAnd(rightConstraint);
		} else if (operator.equalsIgnoreCase("or")) {
			parseLeftOr(leftConstraint);
			parseRightOr(rightConstraint);
		} else {
			throw new NotYetImplementedException();
		}
	}
	
	public static void parseLeftAnd(IConstraint leftConstraint) throws Exception{
		IBinaryPredicate constraint = (IBinaryPredicate) leftConstraint;
		IExpression leftExpr = constraint.getLeft();
		IExpression rightExpr = constraint.getRight();
		String predicate = constraint.getPredicate().toString();
		String leftString ="";
		String rightString ="";
		if (leftExpr instanceof ILiteralString){
			leftString = ((ILiteralString) leftExpr).getString();
		} else if (leftExpr instanceof IModelReference) {
			leftString = ((IModelReference) leftExpr).getOriginalString();
		} else {
			throw new NotTranspilableException();
		}
		
		if (rightExpr instanceof IBooleanLiteral || rightExpr instanceof IDecimalNumber
				|| rightExpr instanceof ILiteralString|| rightExpr instanceof IIntegerNumber){
			rightString  = rightExpr.toString();
		}
		else {
			throw new NotTranspilableException();
		}
		ArrayList<Field> fields = Application.searchField(leftString);
		int predicateCountHolder = p + fields.size();
		for (int i = 0 ; i < fields.size(); i++) {
			Field field = fields.get(i);
			System.out.println("DIAG: " + field.getParentValue());
			if (field.length == 64) {
				ruleString+= Snippets.parentedBinaryPredicateFull(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 32) {
				ruleString+= Snippets.parentedBinaryPredicateHalf(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 8) {
				ruleString+= Snippets.parentedBinaryPredicateByte(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else {
				throw new ImplementationError();
			}
			ruleString+= Snippets.predicateJumpAnd(parseToDec(rightString), predicate, predicateCountHolder);
			lineCount = 1;
			p++;
		}
	}
	
	public static void parseRightAnd(IConstraint rightConstraint) throws Exception {
		parseConstraintRule(rightConstraint);
	}
	
	public static void parseLeftOr(IConstraint leftConstraint) throws NotTranspilableException, ImplementationError, ModelReferenceException, UnknownHostException {
		IBinaryPredicate constraint = (IBinaryPredicate) leftConstraint;
		IExpression leftExpr = constraint.getLeft();
		IExpression rightExpr = constraint.getRight();
		String predicate = constraint.getPredicate().toString();
		String leftString ="";
		String rightString ="";
		if (leftExpr instanceof ILiteralString){
			leftString = ((ILiteralString) leftExpr).getString();
		} else if (leftExpr instanceof IModelReference) {
			leftString = ((IModelReference) leftExpr).getOriginalString();
		} else {
			throw new NotTranspilableException();
		}
		
		if (rightExpr instanceof IBooleanLiteral || rightExpr instanceof IDecimalNumber
				|| rightExpr instanceof ILiteralString|| rightExpr instanceof IIntegerNumber){
			rightString  = rightExpr.toString();
		}
		else {
			throw new NotTranspilableException();
		}
		ArrayList<Field> fields = Application.searchField(leftString);
		int predicateCountHolder = p + fields.size();
		for (int i = 0 ; i < fields.size(); i++) {
			Field field = fields.get(i);
			System.out.println("DIAG: " + field.getParentValue());
			if (field.length == 64) {
				ruleString+= Snippets.parentedBinaryPredicateFull(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 32) {
				ruleString+= Snippets.parentedBinaryPredicateHalf(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 8) {
				ruleString+= Snippets.parentedBinaryPredicateByte(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else {
				throw new ImplementationError();
			}
			ruleString+= Snippets.predicateJumpOr(parseToDec(rightString), predicate, predicateCountHolder);
			lineCount = 1;
			p++;
		}
	}

	public static void parseRightOr(IConstraint rightConstraint) throws Exception {
		parseConstraintRule(rightConstraint);
	}

	public static void parseLeftIfThen(IConstraint leftConstraint) throws NotTranspilableException, ImplementationError, ModelReferenceException, UnknownHostException {
		IBinaryPredicate constraint = (IBinaryPredicate) leftConstraint;
		IExpression leftExpr = constraint.getLeft();
		IExpression rightExpr = constraint.getRight();
		String predicate = constraint.getPredicate().toString();
		String leftString ="";
		String rightString ="";
		if (leftExpr instanceof ILiteralString){
			leftString = ((ILiteralString) leftExpr).getString();
		} else if (leftExpr instanceof IModelReference) {
			leftString = ((IModelReference) leftExpr).getOriginalString();
		} else {
			throw new NotTranspilableException();
		}
		
		if (rightExpr instanceof IBooleanLiteral || rightExpr instanceof IDecimalNumber
				|| rightExpr instanceof ILiteralString|| rightExpr instanceof IIntegerNumber){
			rightString  = rightExpr.toString();
		}
		else {
			throw new NotTranspilableException();
		}
		ArrayList<Field> fields = Application.searchField(leftString);
		int predicateCountHolder = p + fields.size();
		for (int i = 0 ; i < fields.size(); i++) {
			Field field = fields.get(i);
			System.out.println("DIAG: " + field.getParentValue());
			if (field.length == 64) {
				ruleString+= Snippets.parentedBinaryPredicateFull(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 32) {
				ruleString+= Snippets.parentedBinaryPredicateHalf(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 8) {
				ruleString+= Snippets.parentedBinaryPredicateByte(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else {
				throw new ImplementationError();
			}
			ruleString+= Snippets.predicateJumpIfThen(parseToDec(rightString), predicate, predicateCountHolder);
			lineCount = 1;
			p++;
		}
	}


	public static void parseIBinaryPredicate(IConstraint constIndicator) throws Exception{ 
		IBinaryPredicate constraint = (IBinaryPredicate) constIndicator;
		IExpression leftExpr = constraint.getLeft();
		IExpression rightExpr = constraint.getRight();
		String predicate = constraint.getPredicate().toString();
		String leftString ="";
		String rightString ="";
		if (leftExpr instanceof ILiteralString){
			leftString = ((ILiteralString) leftExpr).getString();
		} else if (leftExpr instanceof IModelReference) {
			leftString = ((IModelReference) leftExpr).getOriginalString();
		} else {
			throw new NotTranspilableException();
		}
		
		if (rightExpr instanceof IBooleanLiteral || rightExpr instanceof IDecimalNumber
				|| rightExpr instanceof ILiteralString|| rightExpr instanceof IIntegerNumber){
			rightString  = rightExpr.toString();
		}
		else {
			throw new NotTranspilableException();
		}
		
		ArrayList<Field> fields = Application.searchField(leftString);
		for (int i = 0 ; i < fields.size(); i++) {
			Field field = fields.get(i);
			System.out.println("DIAG: " + field.getParentValue());
			if (field.length == 64) {
				ruleString+= Snippets.parentedBinaryPredicateFull(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 32 || field.length == 16) {
				ruleString+= Snippets.parentedBinaryPredicateHalf(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 8) {
				ruleString+= Snippets.parentedBinaryPredicateByte(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else {
				throw new ImplementationError();
			}
			ruleString+= Snippets.predicateJump(parseToDec(rightString), predicate);
			lineCount = 1;
			p++;
		}
		
	}
	
	public static void parseIIfThenStatement(IConstraint constIndicator ) throws Exception{ 
		IIfThenStatement constraint = (IIfThenStatement) constIndicator;
		if (constraint.getElse() == null) {
			IConstraint left = constraint.getIf();
			IConstraint right = constraint.getThen();
			parseLeftIfThen(left);
			parseConstraintRule(right);
		}
	}

	public static void parseIArithmeticExpression(IConstraint constIndicator ){ 
		//TODO
	}

	public static void parseIBooleanLiteral(IConstraint constIndicator ){ 
		//TODO
	}

	public static void parseICastExpression(IConstraint constIndicator ){
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}

	public static void parseICollectionIndex(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}

	public static void parseIDecimalNumber(IConstraint constIndicator ){ 
		//TODO}
	}

	public static void parseIExistsStatement(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}

	public static void parseIForallStatement(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
		
	}

	public static void parseIFunctionalExpression(IConstraint constIndicator ){
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}

	public static void parseIGlobalExistsStatement(IConstraint constIndicator ){
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	
	public static void parseIIntegerNumber(IConstraint constIndicator ){
		//TODO
	}

	public static void parseIIsInPredicate(IConstraint constIndicator ){
		//TODO
	}

	public static void parseIIsNotInPredicate(IConstraint constIndicator ){
		//TODO
	}

	public static void parseIIsSubtypePredicate(IConstraint constIndicator ){
		//TODO
	}

	public static void parseILiteralString(IConstraint constIndicator ){
		//TODO
	}
	
	public static void parseIModelReference(IConstraint constIndicator ){ 
		//TODO
	}

	public static void parseIMultipleExistsStatement(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIMultipleNotExistsStatement(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseINotExistsStatement(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIOperatorInvocation(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseISelectionExpression(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIValidationFragmentApplication(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIVariableDeclaration(IConstraint constIndicator ){ 
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	
	public static void parseIActionFragmentApplicationAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	
	public static void parseIAddAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIConditionalAction(ISimpleAction simpAction ) throws Exception {
		IConditionalAction condAction = (IConditionalAction) simpAction;
		if (condAction.getElse() == null) {
			IConstraint left = condAction.getIf();
			ICompoundAction compAction = condAction.getThen();
			parseLeftIfThen(left);
			
			List<ISimpleAction> actions = compAction.getSimpleActions();
			for (ISimpleAction action : actions) {
				if (action instanceof IActionFragmentApplicationAction) {
					parseIActionFragmentApplicationAction(action );
				} else if (action instanceof IAddAction) {
					parseIAddAction(action );
				} else if (action instanceof IConditionalAction) {
					parseIConditionalAction(action );
				} else if (action instanceof ICreateAction) {
					parseICreateAction(action );
				} else if (action instanceof IForEachAction) {
					parseIForEachAction(action );
				} else if (action instanceof IOperatorAction) {
					parseIOperatorAction(action );
				} else if (action instanceof IRemoveAction) {
					parseIRemoveAction(action );
				} else if (action instanceof IRemoveFromCollectionAction) {
					parseIRemoveFromCollectionAction(action );
				} else if (action instanceof ISetAction) {
					parseISetAction(action );
				} else if (action instanceof IVariableDeclarationAction) {
					parseIVariableDeclarationAction(action );
				}
			}
			
		} else {
			throw new NotYetImplementedException();
		}
	}
	public static void parseICreateAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIForEachAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	
	public static void parseIOperatorAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIRemoveAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseIRemoveFromCollectionAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	public static void parseISetAction(ISimpleAction action ) throws Exception {
		ISetAction rule = (ISetAction) action;
		String target = rule.getTarget().getOriginalString();
		IExpression expr = rule.getExpression();
		if (target.equalsIgnoreCase("manually")) {
			parseWriteByte(expr.toString());
		} else {
			parseSetAction(target, expr.toString());
		}
	}

	public static void parseIVariableDeclarationAction(ISimpleAction action ) {
		//TODO
		//not necessary in the context of an NRL -> BPF transpiler
	}
	
	
	//TODO, ask whether we need this feature as it seems that it is not necessary in the context of an NRL -> BPF transpiler
	public static String resolveArithmeticExpression(IArithmeticExpression expression) {
		return null;
	}
	
	public static void incrementLineCount() {
		lineCount++;
	}
	
	public static String returnString() {
		return ";; end of rules, packets are either accepted or dropped\nrule"+globalRuleCounter+"p1line1: ret #262144\ndrop: ret #0";
	}
	
	public static String ruleJump() {
		//FIXME implement this after fixing the "jmp" instruction
		//return "rule"+globalRuleCounter+"p"+p+"line1: "+ "jmp " + Snippets.jumpNext();
		return "rule"+globalRuleCounter+"p"+p+"line1: add #0";
	}
	
	public static long ipToDec(String ipAddress) throws UnknownHostException {
		long x = Long.parseLong(hexIP(ipAddress), 16);
		return x;
	}
	
	public static String hexIP(String reqIpAddr) {
		String hex = "";
		String[] part = reqIpAddr.split("[\\.,]");
		if (part.length < 4) {
			return "00000000";
		}
		for (int i = 0; i < 4; i++) {
			int decimal = Integer.parseInt(part[i]);
			if (decimal < 16) // Append a 0 to maintian 2 digits for every
								// number
			{
				hex += "0" + String.format("%01x", decimal);
			} else {
				hex += String.format("%01x", decimal);
			}
		}
		return hex;
	}
	
	private static final Pattern IP_PATTERN = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static boolean validIP(final String ip) {
	    return IP_PATTERN.matcher(ip).matches();
	}
	
	public static long parseToDec(String value) throws UnknownHostException {
		if (validIP(value)) {
			return ipToDec(value);
		} else {
			return Integer.parseInt(value);
		}
	}
	
	public static void parseWriteByte(String expression){
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = pattern.matcher(expression);
        m.find();
        String offset = m.group(1);
        String value = expression.split(":")[1];
        String bytevalue = byteValue(value);
        ruleString += Snippets.writeRule(offset, bytevalue);
        p++;
	}
	
	public static String byteValue (String value) {
		int i = Integer.parseInt(value);
		byte b = (byte) i;
		int i2 = b & 0xFF;
		return "#"+Integer.toString(i2);
	}
	
	public static void parseSetAction(String target, String expression) throws Exception {
		ArrayList<Field> fields = Application.searchField(target);
		for (int i = 0 ; i < fields.size(); i++) {
			Field field = fields.get(i);
			System.out.println("DIAG: " + field.getParentValue());
			if (field.length == 64) {
				ruleString+= Snippets.parentedBinaryPredicateFull(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 32 || field.length == 16) {
				ruleString+= Snippets.parentedBinaryPredicateHalf(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else if (field.length == 8) {
				ruleString+= Snippets.parentedBinaryPredicateByte(field.getByteOffset(), field.getParentOffset(), field.getParentValue())+"\n";
			} else {
				throw new ImplementationError();
			}
			ruleString += Snippets.setRule(field.getByteOffset(), Long.toString(parseToDec(expression)));
			lineCount = 1;
			p++;
		}
	}
	
}
