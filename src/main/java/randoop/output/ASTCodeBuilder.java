package randoop.output;

import com.github.javaparser.ast.stmt.Statement;
import java.util.List;
import randoop.operation.TypedOperation;
import randoop.sequence.Variable;
import randoop.test.TestChecks;

/** Created by bjkeller on 5/15/17. */
public class ASTCodeBuilder extends CodeBuilder {

  private List<Statement> statements;

  public ASTCodeBuilder(List<Statement> statements) {
    this.statements = statements;
  }

  @Override
  public void addStatement(TypedOperation operation, List<Variable> inputs) {}

  @Override
  public void addStatement(Variable variable, TypedOperation operation, List<Variable> inputs) {}

  @Override
  public void addStatement(
      randoop.sequence.Statement statement,
      Variable variable,
      List<Variable> inputs,
      TestChecks checks) {}
}
