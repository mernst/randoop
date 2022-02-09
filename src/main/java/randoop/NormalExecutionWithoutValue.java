package randoop;

import randoop.main.RandoopBug;

/**
 * Means that the statement that this result represents completed normally, but does not evaluate to
 * a value (e.g., call to a void method, setting a field, etc.).
 */
public class NormalExecutionWithoutValue extends AbstractNormalExecution {

  /** @param executionTime the execution time, in nanoseconds */
  public NormalExecutionWithoutValue(long executionTime) {
    super(executionTime);
  }

  @Override
  public Object getRuntimeValue() {
    throw new RandoopBug("called getRuntimeValue() on NormalExecutionWithoutValue");
  }

  /**
   * randoop.test.SequenceTests uses this method.
   *
   * <p>Note that toString() of code under test may have arbitrary behavior.
   */
  @Override
  public String toString() {
    return "[NormalExecutionWithoutValue]";
  }
}
