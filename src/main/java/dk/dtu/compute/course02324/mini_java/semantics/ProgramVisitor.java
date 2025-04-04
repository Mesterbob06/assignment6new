package dk.dtu.compute.course02324.mini_java.semantics;

import dk.dtu.compute.course02324.mini_java.model.*;

public abstract class ProgramVisitor {

    /**
     * Visit a sequence
     * @param sequence sequence to visit
     */
    abstract public void visit(Sequence sequence);

    /**
     * Visit a declaration
     * @param declaration declaration to visit
     */
    abstract public void visit(Declaration declaration);

    /**
     * Visit a printStatement
     * @param printStatement printStatement to visit
     */
    abstract public void visit(PrintStatement printStatement);

    /**
     * Visit a while loop
     * @param whileLoop while loop to visit
     */
    abstract public void visit(WhileLoop whileLoop);

    /**
     * Visit an assignment
     * @param assignment assignment to visit
     */
    abstract public void visit(Assignment assignment);

    /**
     * Visit a literal
     * @param literal literal to visit
     */
    abstract public void visit(Literal literal);

    /**
     * Visit a variable
     * @param var variable to visit
     */
    abstract public void visit(Var var);

    /**
     * Visit an operatorExpression
     * @param operatorExpression operatorExpression to visit
     */
    abstract public void visit(OperatorExpression operatorExpression);

    /**
     * Visit an if-then-else block
     * @param ifThenElse if-then-else block to visit
     */
    abstract  public void visit(IfThenElse ifThenElse);
}
