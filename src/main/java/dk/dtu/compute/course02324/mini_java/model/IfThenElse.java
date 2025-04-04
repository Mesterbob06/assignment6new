package dk.dtu.compute.course02324.mini_java.model;

import dk.dtu.compute.course02324.mini_java.semantics.ProgramVisitor;

public class IfThenElse implements Statement {

    final public Expression conditionalExpression;
    final public Statement trueStatement;
    final public Statement falseStatement;


    public IfThenElse(Expression conditionalExpression, Statement trueStatement, Statement falseStatement) {
        this.conditionalExpression = conditionalExpression;
        this.trueStatement = trueStatement;
        this.falseStatement = falseStatement;
    }

    @Override
    public void accept(ProgramVisitor visitor) {
        visitor.visit(this);
    }
}
