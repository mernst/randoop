package randoop;

import org.plumelib.util.StringsPlume;

/**
 * Means that the statement that this result represents completed normally and evaluated to a value.
 */
public class NormalExecution extends AbstractNormalExecution {

  /** The value that the statement evaluated to. */
  private final Object result;

  /**
   * @param result the return value
   * @param executionTime the execution time, in nanoseconds
   */
  public NormalExecution(Object result, long executionTime) {
    super(executionTime);
    this.result = result;
  }

  @Override
  public Object getRuntimeValue() {
    return this.result;
  }

  /**
   * randoop.test.SequenceTests uses this method.
   *
   * <p>Note that toString() of code under test may have arbitrary behavior.
   */
  @Override
  public String toString() {
    return String.format("[NormalExecution %s]", StringsPlume.toStringAndClass(result));
  }
}
