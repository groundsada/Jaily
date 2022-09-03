import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;

import net.sourceforge.nrl.parser.NRLParser;
import net.sourceforge.nrl.parser.ast.IDeclaration;
import net.sourceforge.nrl.parser.ast.IModelReference;
import net.sourceforge.nrl.parser.ast.INRLAstNode;
import net.sourceforge.nrl.parser.ast.IRuleFile;
import net.sourceforge.nrl.parser.ast.action.IActionFragmentApplicationAction;
import net.sourceforge.nrl.parser.ast.action.IActionFragmentDeclaration;
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
import net.sourceforge.nrl.parser.model.IPackage;
import net.sourceforge.nrl.parser.model.ModelCollection;
import net.sourceforge.nrl.parser.model.loader.StandaloneModelLoader;

/*
 * This is the Application class.
 * It loads the NRL rule files from src/main/resources
 * Currently, it loads the file name inputted in filename, and it outputs filename.bpf (symbolic)
 */
public class Application {
	
	public static String filename = "r5";
	
	public static ArrayList<Field> fieldList = populate();
	
	//loads rulefile into an IRuleFile object
	public static IRuleFile parseRuleFile(File file) {
		try {
			//StandaloneModelLoader modelLoader = new StandaloneModelLoader();
			//IPackage model = modelLoader.loadModel(new URI("src/main/resources/PCAPDFDL/com/owlcyberdefense/dfdl/xsd/ethernetIP.dfdl.xsd"));
			NRLParser parser = new NRLParser();
			//Rules files
			@SuppressWarnings("deprecation")
			IRuleFile ruleFile = parser.parse(new FileReader(file));
			
			//ModelCollection models = new ModelCollection();
			//models.addModelPackage(model);
			
			//parser.resolveModelReferences(ruleFile, models);
			//Resolve model errors
			if (parser.getErrors().size() > 0) {
				System.err.println(parser.getErrors().get(0));
				throw new Exception("Rule file has model errors.");
			}
			return ruleFile;
		}
		catch (Exception e) {
				e.printStackTrace();
		}
		return null;
	}
	

	//main method
	public static void main (String[] args) throws Exception {
		// we pass the name of the file that we are parsing without the .nrl extension
		File dir = new File("src/main/resources/RuleFiles");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File file : directoryListing) {
				IRuleFile ruleFile = parseRuleFile(file);
				System.out.println("\n\\\\\\\\\\\\\\\\");
				printRuleTypes(ruleFile);
			 	Transpiler.transpileRuleFile(ruleFile);
				FileWriter myWriter = new FileWriter("output/" + file.getName() + ".output");
			    myWriter.write(Transpiler.globalOutput);
			    myWriter.close();
			}
		}
	}
	
	
	//search an arraylist of fields, and return by name
	public static ArrayList<Field> searchField(String name) throws ModelReferenceException{
		ArrayList<Field> matchFieldList = new ArrayList<Field>();
		for (int i = 0; i < fieldList.size() ; i++) {
			if (fieldList.get(i).name.contains(name)) {
				Field match = fieldList.get(i);
				matchFieldList.add(match);
			}
		}
		if (matchFieldList.size() == 0) {
			throw new ModelReferenceException();
		}
		return matchFieldList;
	}
	
	//prints the types of rules in rule file
	public static void printRuleTypes(IRuleFile ruleFile) {
		for (IDeclaration rule_dec : ruleFile.getDeclarations()) {
			if (rule_dec instanceof IConstraintRuleDeclaration) {
				System.out.println("Constraint Rule " + rule_dec.getId());
				System.out.println(getConstraintRuleType(rule_dec));
			}
			else if (rule_dec instanceof IActionRuleDeclaration) {
				System.out.println("Action Rule " + rule_dec.getId());
				System.out.println(getActionRuleType(rule_dec));
				System.out.println(getActionsType(rule_dec));
			}
		}
	}
	
	//return fields from populate for testing
	public static ArrayList<Field> populate(){
		return FieldPopulate.returnList();
		
	}
	
	
	//returns the constraint rule type
	public static String getConstraintRuleType(IDeclaration rule_dec) {
		INRLAstNode indicator = null;
		
		if (rule_dec instanceof IConstraintRuleDeclaration) {
			indicator = ((IConstraintRuleDeclaration) rule_dec).getConstraint();
			IConstraint constIndicator = (IConstraint) indicator;
			if (constIndicator instanceof IBinaryOperatorStatement) {
				return "Constraint type: IBinaryOperatorStatement";
			}
			else if (constIndicator instanceof IBinaryPredicate) {
				return "Constraint type: IBinaryPredicate";
			}
			
			else if (constIndicator instanceof IArithmeticExpression) {
				return "Constraint type: IArithmeticExpression";
			}
			
			else if (constIndicator instanceof IBooleanLiteral) {
				return "Constraint type: IBooleanLiteral";
			}
			
			else if (constIndicator instanceof ICastExpression) {
				return "Constraint type: ICastExpression";
			}
			
			else if (constIndicator instanceof ICollectionIndex) {
				return "Constraint type: ICollectionIndex";
			}
			
			else if (constIndicator instanceof IDecimalNumber) {
				return "Constraint type: IDecimalNumber";
			}
			
			else if (constIndicator instanceof IExistsStatement) {
				return "Constraint type: IExistsStatement";
			}
			
			else if (constIndicator instanceof IIfThenStatement) {
				
				return "Constraint type: IIfThenStatement";
			}
			
			
			else if (constIndicator instanceof IForallStatement) {
				return "Constraint type: IForallStatement";
			}
			
			else if (constIndicator instanceof IFunctionalExpression) {
				return "Constraint type: IFunctionalExpression";
			}
			
			else if (constIndicator instanceof IGlobalExistsStatement) {
				return "Constraint type: IGlobalExistsStatement";
			}
			
			else if (constIndicator instanceof IIntegerNumber) {
				return "Constraint type: IIntegerNumber";
			}
			
			else if (constIndicator instanceof IIsInPredicate) {
				return "Constraint type: IIsInPredicate";
			}
			
			else if (constIndicator instanceof IIsNotInPredicate) {
				return "Constraint type: IIsNotInPredicate";
			}
			
			else if (constIndicator instanceof IIsSubtypePredicate) {
				return "Constraint type: IIsSubtypePredicate";
			}

			else if (constIndicator instanceof ILiteralString) {
				return "Constraint type: ILiteralString";
			}
			else if (constIndicator instanceof IModelReference) {
				return "Constraint type: IModelReference";
			}
			else if (constIndicator instanceof IMultipleExistsStatement) {
				return "Constraint type: IMultipleExistsStatement";
			}
			else if (constIndicator instanceof IMultipleNotExistsStatement) {
				return "Constraint type: IMultipleNotExistsStatement";
			}
			else if (constIndicator instanceof INotExistsStatement) {
				return "Constraint type: INotExistsStatement";
			}
			else if (constIndicator instanceof IOperatorInvocation) {
				return "Constraint type: IOperatorInvocation";
			}
			else if (constIndicator instanceof ISelectionExpression) {
				return "Constraint type: ISelectionExpression";
			}
			else if (constIndicator instanceof IValidationFragmentApplication) {
				return "Constraint type: IValidationFragmentApplication";
			}
			else if (constIndicator instanceof IVariableDeclaration) {
				return "Constraint type: IVariableDeclaration";
			}
			
			
		}
		return null;
	}
	
	
	//returns the action rule type (compound / simple)
	public static String getActionRuleType(IDeclaration rule_dec) {
		INRLAstNode indicator = null;
		
		if (rule_dec instanceof IActionRuleDeclaration) {
			indicator = ((IActionRuleDeclaration) rule_dec).getAction();
			if (indicator instanceof ICompoundAction) {
				return "Action type: ICompoundAction";
			} else if (indicator instanceof ISimpleAction) {
				return "Action type: ISimpleAction";
			}
		}
		return null;
	}
	
	//returns the action type for simple action
	public static String getActionRuleTypeFromAction(ISimpleAction simp_act) {
		if (simp_act instanceof IActionFragmentApplicationAction) {
			return "Action type: IActionFragmentApplicationAction";
		} else if (simp_act instanceof IAddAction) {
			return "Action type: IAddAction";
		} else if (simp_act instanceof IConditionalAction) {
			return "Action type: IConditionalAction";
		} else if (simp_act instanceof ICreateAction) {
			return "Action type: ICreateAction";
		} else if (simp_act instanceof IForEachAction) {
			return "Action type: IForEachAction";
		} else if (simp_act instanceof IOperatorAction) {
			return "Action type: IOperatorAction";
		} else if (simp_act instanceof IRemoveAction) {
			return "Action type: IRemoveAction";
		} else if (simp_act instanceof IRemoveFromCollectionAction) {
			return "Action type: IRemoveFromCollectionAction";
		} else if (simp_act instanceof ISetAction) {
			return "Action type: ISetAction";
		} else if (simp_act instanceof IVariableDeclarationAction) {
			return "Action type: IVariableDeclarationAction";
		}
		return null;
	}
	
	//returns the action types for simple actions in compound action
	public static String getActionsType(IDeclaration rule_dec) {
		String ret = "";
		ICompoundAction indicator = (ICompoundAction) ((IActionRuleDeclaration) rule_dec).getAction();
		for (ISimpleAction simple_action : indicator.getSimpleActions()) {
			ret +=  getActionRuleTypeFromAction(simple_action) +", ";
		}
		return ret;
	}
	
}
