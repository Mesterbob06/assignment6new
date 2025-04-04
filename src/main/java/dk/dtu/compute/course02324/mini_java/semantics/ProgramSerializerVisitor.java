package dk.dtu.compute.course02324.mini_java.semantics;

import dk.dtu.compute.course02324.mini_java.model.*;

public class ProgramSerializerVisitor extends ProgramVisitor  {

    private final StringBuilder result = new StringBuilder();

    private int indentLevel = 0;

    private void addIndentation() {
        String INDENT = "  ";
        result.append(INDENT.repeat(Math.max(0, indentLevel)));
    }


    public void visit(Statement statement) {
        statement.accept(this);
    }

    @Override
    public void visit(Sequence sequence) {
        for (Statement statement: sequence.statements) {
            // Takes care of proper indentation of substatements
            addIndentation();

            // Recursively deals with representation of substatement
            statement.accept(this);

            // The following is just a minor detail:
            // Making sure that while loops do not end with a semicolon
            if (statement instanceof WhileLoop) {
                result.append(System.lineSeparator());
            } else {
                result.append(";").append(System.lineSeparator());
            }
        }
    }

    @Override
    public void visit(Declaration declaration) {
        result.append(declaration.type.getName()).append(" ").append(declaration.variable.name);
        if (declaration.expression != null) {
            result.append(" = ");
            declaration.expression.accept(this);
        }
    }

    /**
     * Serializes a printStatement by recursively serializing the expression.
     * @param printStatement printStatement to visit
     */
    @Override
    public void visit(PrintStatement printStatement) {
        result.append("System.out.println(\"").append(printStatement.prefix).append("\"");
        result.append(" + ");
        printStatement.expression.accept(this);
        result.append(")");
    }

    /**
     * Serializes a while loop, utilizing the indentLevel
     * and addIndentation() utils to follow formatting conventions.
     * @param whileLoop while loop to visit
     */
    @Override
    public void visit(WhileLoop whileLoop) {
        result.append("while ( ");
        whileLoop.expression.accept(this);
        result.append(" >= 0 ) {").append(System.lineSeparator());
        indentLevel++;
        whileLoop.statement.accept(this);
        indentLevel--;
        addIndentation();
        result.append("}");
    }

    @Override
    public void visit(Assignment assignment) {
        result.append(assignment.variable.name).append(" = ");
        assignment.expression.accept(this);
    }

    @Override
    public void visit(Literal literal) {
        if (literal instanceof IntLiteral) {
            result.append(((IntLiteral) literal).literal);
        } else if (literal instanceof FloatLiteral) {
            result.append(((FloatLiteral) literal).literal).append("f");
        } else {
            assert false;
        }
    }

    @Override
    public void visit(Var var) {
        result.append(var.name);
    }

    @Override
    public void visit(OperatorExpression operatorExpression) {
        if (operatorExpression.operands.isEmpty()) {
            result.append(operatorExpression.operator.getName()).append("()");
        } else if (operatorExpression.operands.size() == 1) {
            result.append(operatorExpression.operator.getName()).append(" ");
            operatorExpression.operands.getFirst().accept(this);
        } else if (operatorExpression.operands.size() == 2) {
            operandToString(operatorExpression.operator, operatorExpression.operands.getFirst(),0);
            result.append(" ").append(operatorExpression.operator.getName()).append(" ");
            operandToString(operatorExpression.operator, operatorExpression.operands.getLast(), 1);
        } else {
            result.append(operatorExpression.operator.getName()).append("(");
            boolean first = true;
            for (Expression operand : operatorExpression.operands) {
                if (!first) {
                    result.append(", ");
                } else {
                    first = false;
                }
                operand.accept(this);
            }
            result.append(")");
        }
    }

    /**
     * Serializes a if-then-else, utilizing the indentLevel and
     * addIndentation() utils to follow formatting conventions and
     * recursively serializing the conditional expression and statements.
     * @param ifThenElse if-then-else block to visit
     */
    @Override
    public void visit(IfThenElse ifThenElse) {
        result.append("if ( ");
        ifThenElse.conditionalExpression.accept(this);
        result.append(" >= 0 ) {").append(System.lineSeparator());
        indentLevel++;
        ifThenElse.trueStatement.accept(this);
        indentLevel--;
        addIndentation();
        result.append("} else {").append(System.lineSeparator());
        indentLevel++;
        ifThenElse.falseStatement.accept(this);
        indentLevel--;
        result.append("}");
    }

    private void operandToString(Operator operator, Expression expression, int number) {
        if (expression instanceof OperatorExpression) {
            OperatorExpression operatorExpression = (OperatorExpression) expression;
            if (operatorExpression.operator.precedence > operator.precedence ||
                    (operatorExpression.operator.precedence == operator.precedence &&
                            ((operator.associativity == Associativity.LtR && number == 0) ||
                                    (operator.associativity == Associativity.RtL && number == 1)))) {
                expression.accept(this);
            } else {
                result.append("( ");
                expression.accept(this);
                result.append(" )");
            }
        } else if (expression instanceof Assignment) {
            result.append("( ");
            expression.accept(this);
            result.append(" )");
        } else {
            expression.accept(this);
        }
    }

    public String result() {
        return result.toString();
    }

}
