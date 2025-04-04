package dk.dtu.compute.course02324.mini_java.semantics;

import dk.dtu.compute.course02324.mini_java.model.*;
import static dk.dtu.compute.course02324.mini_java.model.Operator.*;
import static dk.dtu.compute.course02324.mini_java.utils.Shortcuts.*;

import java.util.*;

import static java.util.Map.entry;

public class ProgramTypeVisitor extends ProgramVisitor {

    /**
     * This is a very simple map of operators to their possible types.
     * Note the typing of an operator is very simplistic for now; the
     * types of all operands and the result of the operation are the same.<p>
     */
    final private Map<Operator,List<Type>> operatorTypes = Map.ofEntries(
            entry(PLUS1, List.of(INT,FLOAT)),
            entry(MINUS1, List.of(INT,FLOAT)),
            entry(PLUS2, List.of(INT, FLOAT)),
            entry(MINUS2, List.of(INT, FLOAT)),
            entry(MULT, List.of(INT, FLOAT)),
            entry(DIV, List.of(INT,FLOAT)),
            entry(MOD, List.of(INT, FLOAT)));

    final public Map<Expression, Type> typeMapping = new HashMap<>();

    final public Set<Var> variables = new HashSet<>();

    final public List<String> problems = new ArrayList<>();

    public void visit(Statement statement) {
        statement.accept(this);
    }

    /**
     * Visits all substatements of a sequence
     * @param sequence sequence to visit
     */
    @Override
    public void visit(Sequence sequence) {
        for (Statement substatement: sequence.statements) {
            substatement.accept(this);
        }
    }

    /**
     * Checks for duplicate variable declaration and handles type-mapping
     * for declarations while checking for type mismatch
     * @param declaration declaration to visit
     */
    @Override
    public void visit(Declaration declaration) {
        if (declaration.expression != null) {
            declaration.expression.accept(this);
        }
        Var variable = declaration.variable;
        if (variables.contains(variable)) {
            problems.add("Variable " + variable.name + " declared more than once.");
        } else {
            variables.add(variable);
            typeMapping.put(variable, declaration.type);
            if (declaration.expression != null) {
                Type expressionType = typeMapping.get(declaration.expression);
                if (!declaration.type.equals(expressionType)) {
                    problems.add("Type mismatch for declaration of " +
                            declaration.type.getName() + " " + declaration.variable.name +
                            ": expression is type " + expressionType.getName() + ".");
                }
            }
        }
    }

    /**
     * Validates the printStatement expression
     * @param printStatement printStatement to visit
     */
    @Override
    public void visit(PrintStatement printStatement) {
        printStatement.expression.accept(this);

        // Here, we do not need to do anything except for recursively
        // making sure that the PrintStatements expression is valid
        // (which the above accept actually does).
    }

    /**
     * Validates the expression and recursively visits the statement
     * @param whileLoop while loop to visit
     */
    public void visit(WhileLoop whileLoop) {
        whileLoop.expression.accept(this);
        if(!((typeMapping.get(whileLoop.expression).equals(INT)))){
            problems.add("Expression is not of type int..." );
        }
        whileLoop.statement.accept(this);
    }

    /**
     * Recursively checks types for expression and
     * checks for type mismatch. Also checks for undefined variable
     * assignment.
     * @param assignment assignment to visit
     */
    @Override
    public void visit(Assignment assignment) {
        assignment.expression.accept(this);
        if (variables.contains(assignment.variable)) {
            Type type = typeMapping.get(assignment.variable);
            if (!type.equals(typeMapping.get(assignment.expression))) {
                problems.add("Type mismatch for assignment to variable " +
                        assignment.variable.name + " of type " + type.getName() + ".");
            } else {
                typeMapping.put(assignment, type);
            }
        } else {
            problems.add("Variable " + assignment.variable.name + " not defined.");
        }
    }

    /**
     * Visit a literal and perform type mapping.
     * @param literal literal to visit
     */
    @Override
    public void visit(Literal literal) {
        if (literal instanceof IntLiteral) {
            typeMapping.put(literal, INT);
        }  else if (literal instanceof FloatLiteral) {
            typeMapping.put(literal, FLOAT);
        }
    }

    /**
     * Visit a variable and checks definition and type.
     * @param var variable to visit
     */
    @Override
    public void visit(Var var) {
        if (!variables.contains(var)) {
            problems.add("Variable not defined " + var);
        } else if (typeMapping.get(var) == null) {
            // this should actually not happen
            problems.add("Variable " + var.name + " does not have a type.");
        }
    }

    /**
     * we didn't write this ┗|｀O′|┛
     * @param operatorExpression operatorExpression to visit
     */
    @Override
    public void visit(OperatorExpression operatorExpression) {
        Type operandType = null;
        for (Expression subexpression: operatorExpression.operands) {
            subexpression.accept(this);
            Type subexpressionType = typeMapping.get(subexpression);
            if (subexpressionType == null) {
                problems.add("A subexpression of " + operatorExpression.operator.getName() + " does not have a type.");
            }
            if (operandType == null) {
                operandType = subexpressionType;
            } else if (!operandType.equals(subexpressionType)) {
                problems.add("Subexpressions of operator do mot match for " + operatorExpression.operator.getName() + ".");
            }
        }
        if (operandType != null) {
            List<Type> opTypes = operatorTypes.get(operatorExpression.operator);
            if (opTypes != null && opTypes.contains(operandType)) {
                typeMapping.put(operatorExpression, operandType);
            } else {
                problems.add("Operator does not support the type of its operands. Operator is " + operatorExpression.operator + " and operand type is " + operandType);
            }
        } else {
            problems.add("Subexpression(s) of operand do not have a type: Operator " + operatorExpression.operator);
        }
    }

    /**
     * Recursively visits the conditional expression and checks
     * in the type map that it is an integer. Also visits the
     * statements in the if and else blocks.
     * @param ifThenElse if-then-else block to visit
     */
    @Override
    public void visit(IfThenElse ifThenElse) {
        ifThenElse.conditionalExpression.accept(this);
        if(!((typeMapping.get(ifThenElse.conditionalExpression).equals(INT)))){
            problems.add("Expression is not of type int..." );
        }
        ifThenElse.falseStatement.accept(this);
        ifThenElse.trueStatement.accept(this);
    }

}
