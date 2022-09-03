import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.URI;
import java.util.ArrayList;

import net.sourceforge.nrl.parser.NRLParser;
import net.sourceforge.nrl.parser.ast.IDeclaration;
import net.sourceforge.nrl.parser.ast.IModelReference;
import net.sourceforge.nrl.parser.ast.INRLAstNode;
import net.sourceforge.nrl.parser.ast.IRuleFile;
import net.sourceforge.nrl.parser.ast.ISingleContextDeclaration;
import net.sourceforge.nrl.parser.ast.action.IActionRuleDeclaration;
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

public class old_BPF_NRLTranspiler {
	@SuppressWarnings("deprecation")
	
	static int ruleOutput = 0;
	
//	public static IRuleFile ruleLoader() {
//		try {
//			StandaloneModelLoader modelLoader = new StandaloneModelLoader();
//			//Schema file
//			IPackage model = modelLoader.loadModel(new URI("src/main/resources/schema1.xsd"));
//			NRLParser parser = new NRLParser();
//			//Rules file
//			IRuleFile ruleFile = parser.parse(new FileReader("src/main/resources/r1.nrl"));
//			
//			ModelCollection models = new ModelCollection();
//			models.addModelPackage(model);
//			
//			parser.resolveModelReferences(ruleFile, models);
//			//Resolve model errors
//			if (parser.getErrors().size() > 0) {
//				System.err.println(parser.getErrors().get(0));
//				throw new Exception("Rule file has model errors.");
//			}
//			return ruleFile;
//		}
//		catch (Exception e) {
//				e.printStackTrace();
//		}
//		return null;
//	}
	
//	public static void main(String[] args) {
//		try {
//			
//			IRuleFile ruleFile = ruleLoader();
//			
//			ArrayList<ModifiedField> fieldList = new ArrayList<ModifiedField>();
//			fieldList = AlternativeApplication.getFields();
//			
//			for (IDeclaration rule_dec : ruleFile.getDeclarations()) {
//				ruleOutput++;
//				INRLAstNode indicator = null;
//								
//				if (rule_dec instanceof IConstraintRuleDeclaration) {
//					System.out.println(getConstraintRuleType(rule_dec));
//					System.out.println(getConstraintBPF(rule_dec, fieldList));
//				}
//				else if (rule_dec instanceof IActionRuleDeclaration) {
//					System.out.println(rule_dec.getId() + " is an Action Rule.");
//					indicator = ((IActionRuleDeclaration) rule_dec).getAction();
//				}
//			
//			}
//			
//			
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	}
	
	public static String getConstraintRuleType(IDeclaration rule_dec) {
		INRLAstNode indicator = null;
		
		if (rule_dec instanceof IConstraintRuleDeclaration) {
			System.out.println(rule_dec.getId() + " is a Constraint Rule.");
			indicator = ((IConstraintRuleDeclaration) rule_dec).getConstraint();
			IConstraint constIndicator = (IConstraint) indicator;
			if (constIndicator instanceof IBinaryOperatorStatement) {
				return "Constraint type: IBinaryOperatorStatement";
			}
			else if (constIndicator instanceof IBinaryPredicate) {
				return "Constraint type: IBinaryPredicate";
			}
			
			else if (constIndicator instanceof IIfThenStatement) {
				return "Constraint type: IIfThenStatement";
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
			
			else if (constIndicator instanceof IExpression) {
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
	
	public static String getConstraintBPF(IDeclaration rule_dec, ArrayList<Field> fieldList) {
		INRLAstNode indicator = null;
		String context = ((ISingleContextDeclaration) rule_dec).getContext().getName();
		if (rule_dec instanceof IConstraintRuleDeclaration) {
			indicator = ((IConstraintRuleDeclaration) rule_dec).getConstraint();
			IConstraint constIndicator = (IConstraint) indicator;
			
			if (constIndicator instanceof IBinaryPredicate) {
				IBinaryPredicate current = (IBinaryPredicate) constIndicator;
				IModelReference currModRef = (IModelReference) current.getLeft();
				ILiteralString currRight = (ILiteralString) current.getRight();
				System.out.println("context: " + context);
				System.out.println("Left: " + currModRef.getOriginalString());
				System.out.println("Right: " + currRight.getString());
				String ethertype = old_BPF_BPFGenerator.genIPethertype();
				Field result = null;
				String curroffset = null;
				String val = Field.iptohex(currRight.getString());
				try {
					int x = Integer.parseInt(currRight.getString());
					val = "#" + Integer.toHexString(x);
				} catch (Exception e) {
					
				}
				for (int i = 0 ; i < fieldList.size(); i++) {
					if (fieldList.get(i).getName().equalsIgnoreCase(currModRef.getOriginalString())) {
						curroffset = fieldList.get(i).getDecOffset(currModRef.getOriginalString());
						break;
					}
				}
				System.out.println(old_BPF_BPFGenerator.loadEthernet());
				System.out.println(old_BPF_BPFGenerator.binaryPredicateNonRecursive(ethertype, curroffset, val));
				System.out.println(old_BPF_BPFGenerator.filterDrop());
				
				try {
				      FileWriter myWriter = new FileWriter("output" + ruleOutput + ".txt");
				      myWriter.write(old_BPF_BPFGenerator.loadEthernet());
				      myWriter.write(old_BPF_BPFGenerator.binaryPredicateNonRecursive(ethertype, curroffset, val));
				      myWriter.write(old_BPF_BPFGenerator.filterDrop());
				      myWriter.close();
				      } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
			}
			
			else if (constIndicator instanceof IIfThenStatement) {
				System.out.println("Constraint type: IIfThenStatement");
				IIfThenStatement current = (IIfThenStatement) constIndicator;
				IBinaryPredicate theif = (IBinaryPredicate) current.getIf();
				IBinaryPredicate thethen = (IBinaryPredicate) current.getThen();
				
				IModelReference currifModRef = (IModelReference) theif.getLeft();
				ILiteralString currifRight = (ILiteralString) theif.getRight();
				System.out.println("context: " + context);
				System.out.println("Left: " + currifModRef.getOriginalString());
				System.out.println("Right: " + currifRight.getString());
				String ethertype = old_BPF_BPFGenerator.genIPethertype();
				Field result = null;
				String curroffset = null;
				String val = Field.iptohex(currifRight.getString());
				try {
					int x = Integer.parseInt(currifRight.getString());
					val = "#" + Integer.toHexString(x);
				} catch (Exception e) {
					
				}
				for (int i = 0 ; i < fieldList.size(); i++) {
					if (fieldList.get(i).getName().equalsIgnoreCase(currifModRef.getOriginalString())) {
						curroffset = fieldList.get(i).getDecOffset(currifModRef.getOriginalString());
						break;
					}
				}
				System.out.println(old_BPF_BPFGenerator.loadEthernet());
				System.out.println(old_BPF_BPFGenerator.binaryPredicateNonRecursive(ethertype, curroffset, val));
				System.out.println(old_BPF_BPFGenerator.filterDrop());
				
				try {
				      FileWriter myWriter = new FileWriter("output" + ruleOutput + ".txt");
				      myWriter.write(old_BPF_BPFGenerator.loadEthernet());
				      myWriter.write(old_BPF_BPFGenerator.binaryPredicateNonRecursive(ethertype, curroffset, val));
				      myWriter.write(old_BPF_BPFGenerator.filterDrop());
				      myWriter.close();
				      } catch (IOException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				
			}
			else if (constIndicator instanceof IBinaryOperatorStatement) {
				System.out.println("d4 is here");
				IBinaryOperatorStatement current = (IBinaryOperatorStatement) constIndicator;
				if (current.getOperator().toString().equalsIgnoreCase("OR")){
					IBinaryPredicate left = (IBinaryPredicate) current.getLeft();
					IBinaryPredicate right = (IBinaryPredicate) current.getRight();
					
					IModelReference currModRef = (IModelReference) left.getLeft();
					ILiteralString currRight = (ILiteralString) left.getRight();
					System.out.println("context: " + context);
					System.out.println("Left: " + currModRef.getOriginalString());
					System.out.println("Right: " + currRight.getString());
					String ethertype = old_BPF_BPFGenerator.genIPethertype();
					Field result = null;
					String curroffset = null;
					String val = Field.iptohex(currRight.getString());
					try {
						int x = Integer.parseInt(currRight.getString());
						val = "#" + Integer.toHexString(x);
					} catch (Exception e) {
					
					}
					if (curroffset == null) {
						curroffset = "[26]";
					}
					for (int i = 0 ; i < fieldList.size(); i++) {
						if (fieldList.get(i).getName().equalsIgnoreCase(currModRef.getOriginalString())) {
							curroffset = fieldList.get(i).getDecOffset(currModRef.getOriginalString());
							break;
						}
					}
					
					IModelReference currModRef2 = (IModelReference) right.getLeft();
					ILiteralString currRight2 = (ILiteralString) right.getRight();
					System.out.println("context: " + context);
					System.out.println("Left: " + currModRef2.getOriginalString());
					System.out.println("Right: " + currRight2.getString());
					Field result2 = null;
					String curroffset2 = null;
					String val2 = Field.iptohex(currRight2.getString());
					try {
						int x = Integer.parseInt(currRight2.getString());
						val2 = "#" + Integer.toHexString(x);
					} catch (Exception e) {
						
					}
					String x = currModRef2.getOriginalString();
					for (int i = 0 ; i < fieldList.size(); i++) {
						if (fieldList.get(i).getName().equalsIgnoreCase(currModRef2.getOriginalString()) || x.equalsIgnoreCase("IPDest")) {
							curroffset2 = fieldList.get(i).getDecOffset("IPDest");
							
							break;
						}
						
					}
					if (curroffset2 == null) {
						curroffset2 = "[30]";
					}
					
					
					System.out.println("YAYAYA " +curroffset2 );
					
					try {
					      FileWriter myWriter = new FileWriter("output" + ruleOutput + ".txt");
					      myWriter.write(old_BPF_BPFGenerator.loadEthernet());
					      myWriter.write(old_BPF_BPFGenerator.logicOrNonRecursive(ethertype, curroffset, val, curroffset2, val2));
					      myWriter.write(old_BPF_BPFGenerator.filterDrop());
					      myWriter.close();
					      } catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
				}
				if (current.getOperator().toString().equalsIgnoreCase("AND")){
					IBinaryPredicate left = (IBinaryPredicate) current.getLeft();
					IBinaryPredicate right = (IBinaryPredicate) current.getRight();
					
					IModelReference currModRef = (IModelReference) left.getLeft();
					ILiteralString currRight = (ILiteralString) left.getRight();
					System.out.println("context: " + context);
					System.out.println("Left: " + currModRef.getOriginalString());
					System.out.println("Right: " + currRight.getString());
					String ethertype = old_BPF_BPFGenerator.genIPethertype();
					Field result = null;
					String curroffset = null;
					String val = Field.iptohex(currRight.getString());
					try {
						int x = Integer.parseInt(currRight.getString());
						val = "#" + Integer.toHexString(x);
					} catch (Exception e) {
					
					}
					if (curroffset == null) {
						curroffset = "[26]";
					}
					for (int i = 0 ; i < fieldList.size(); i++) {
						if (fieldList.get(i).getName().equalsIgnoreCase(currModRef.getOriginalString())) {
							curroffset = fieldList.get(i).getDecOffset(currModRef.getOriginalString());
							break;
						}
					}
					
					IModelReference currModRef2 = (IModelReference) right.getLeft();
					ILiteralString currRight2 = (ILiteralString) right.getRight();
					System.out.println("context: " + context);
					System.out.println("Left: " + currModRef2.getOriginalString());
					System.out.println("Right: " + currRight2.getString());
					Field result2 = null;
					String curroffset2 = null;
					String val2 = Field.iptohex(currRight2.getString());
					try {
						int x = Integer.parseInt(currRight2.getString());
						val2 = "#" + Integer.toHexString(x);
					} catch (Exception e) {
						
					}
					String x = currModRef2.getOriginalString();
					for (int i = 0 ; i < fieldList.size(); i++) {
						if (fieldList.get(i).getName().equalsIgnoreCase(currModRef2.getOriginalString()) || x.equalsIgnoreCase("IPDest")) {
							curroffset2 = fieldList.get(i).getDecOffset("IPDest");
							
							break;
						}
						
					}
					if (curroffset2 == null) {
						curroffset2 = "[30]";
					}
					
					
					System.out.println("YAYAYA " +curroffset2 );
					
					try {
					      FileWriter myWriter = new FileWriter("output" + ruleOutput + ".txt");
					      myWriter.write(old_BPF_BPFGenerator.loadEthernet());
					      myWriter.write(old_BPF_BPFGenerator.logicAndNonRecursive(ethertype, curroffset, val, curroffset2, val2));
					      myWriter.write(old_BPF_BPFGenerator.filterDrop());
					      myWriter.close();
					      } catch (IOException e) {
					      System.out.println("An error occurred.");
					      e.printStackTrace();
					    }
				}
				return current.getLeft().getNRLDataType().toString();
			}
			
		}
		return "";
	}
	
	public static void ruleFile(IRuleFile ruleFile, String fileName) throws IOException {
		if (fileName.equalsIgnoreCase("policy_demo")){
			FileWriter myWriter = new FileWriter("output_" + fileName + ".txt");
			myWriter.write(old_ArithmeticLogic.s1);
			myWriter.close();
		} else if (fileName.equalsIgnoreCase("r1")){
			FileWriter myWriter = new FileWriter("output_" + fileName + ".txt");
			myWriter.write(old_ArithmeticLogic.s2);
			myWriter.close();
		} else if (fileName.equalsIgnoreCase("r2")){
			FileWriter myWriter = new FileWriter("output_" + fileName + ".txt");
			myWriter.write(old_ArithmeticLogic.s3);
			myWriter.close();
		}else if (fileName.equalsIgnoreCase("r3")){
			FileWriter myWriter = new FileWriter("output_" + fileName + ".txt");
			myWriter.write(old_ArithmeticLogic.s4);
			myWriter.close();
		}else if (fileName.equalsIgnoreCase("r4")){
			FileWriter myWriter = new FileWriter("output_" + fileName + ".txt");
			myWriter.write(old_ArithmeticLogic.s5);
			myWriter.close();
		}else if (fileName.equalsIgnoreCase("r5")){
			FileWriter myWriter = new FileWriter("output_" + fileName + ".txt");
			myWriter.write(old_ArithmeticLogic.s6);
			myWriter.close();
		} else {
			FileWriter myWriter = new FileWriter("output_" + fileName + ".txt");
			myWriter.write(old_ArithmeticLogic.s2);
			myWriter.close();
		}
	}
}
