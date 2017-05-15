package randoop.output;

import java.util.List;
import randoop.operation.TypedOperation;
import randoop.sequence.Statement;
import randoop.sequence.Variable;
import randoop.test.TestChecks;

/** Created by bjkeller on 5/15/17. */
public abstract class CodeBuilder {

  public void addStatement(Statement statement, Variable variable, List<Variable> inputs) {
    statement.apply(this, variable, inputs);
  }

  public abstract void addStatement(TypedOperation operation, List<Variable> inputs);

  public abstract void addStatement(
      Variable variable, TypedOperation operation, List<Variable> inputs);

  public abstract void addStatement(
      Statement statement, Variable variable, List<Variable> inputs, TestChecks checks);
}
