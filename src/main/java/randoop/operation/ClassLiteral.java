package randoop.operation;

import java.util.List;
import java.util.Objects;
import randoop.ExecutionOutcome;
import randoop.NormalExecution;
import randoop.sequence.Variable;
import randoop.types.ReferenceType;
import randoop.types.Type;
import randoop.types.TypeTuple;

/**
 * ClassLiteral is a {@link Operation} that represents a class literal such as {@code
 * java.lang.Object.class}.
 */
public final class ClassLiteral extends CallableOperation {

  /** The class. */
  private final Class<?> theClass;

  /**
   * Creates an object representing a class literal operation such as {@code
   * java.lang.Object.class}.
   *
   * @param theClass the class in the class literal operation. For example, {@code Object}, not
   *     {@code Class<Object>}.
   */
  private ClassLiteral(Class<?> theClass) {
    if (theClass == null) {
      throw new IllegalArgumentException("the Class should not be null.");
    }
    this.theClass = theClass;
  }

  /**
   * Creates an object representing a class literal operation such as {@code
   * java.lang.Object.class}. The argument is a type like {@code Class<Object>}, not just {@code
   * Object}.
   *
   * @param type the type in the class literal operation
   */
  public ClassLiteral(ReferenceType type) {
    this(type.getRuntimeClass());
  }

  /**
   * The class in the class literal operation.
   *
   * @return the class in the class literal operation
   */
  public Class<?> getTheClass() {
    return this.theClass;
  }

  @Override
  public String toString() {
    return theClass.getCanonicalName() + ".class";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ClassLiteral)) {
      return false;
    }
    if (this == o) {
      return true;
    }
    ClassLiteral other = (ClassLiteral) o;
    return this.theClass.equals(other.theClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(theClass);
  }

  @Override
  // The argument array contains a single Integer.
  public ExecutionOutcome execute(Object[] input) {
    assert input.length == 0 : "requires no input";
    return new NormalExecution(theClass, 0);
  }

  @Override
  public void appendCode(
      Type declaringType,
      TypeTuple inputTypes,
      Type outputType,
      List<Variable> inputVars,
      StringBuilder b) {
    b.append(this.toString());
  }

  @Override
  public String getName() {
    return this.toString();
  }

  @Override
  public String toParsableString(Type declaringType, TypeTuple inputTypes, Type outputType) {
    return theClass.getName();
  }

  /**
   * Create a class literal from the given string, which must be of the form "CLASSNAME.class",
   * where CLASSNAME is in the format accepted by {@code Class.forName()}.
   *
   * @param signature a string of the format accepted by {@code Class.forName()}, followed by
   *     ".class"
   * @return a new class literal for the given class
   * @throws OperationParseException if the given string is malformed
   */
  @SuppressWarnings("signature") // parsing
  public static ClassLiteral parse(String signature) throws OperationParseException {
    if (signature == null) {
      throw new IllegalArgumentException("signature may not be null");
    }

    if (!signature.endsWith(".class")) {
      throw new OperationParseException("Does not end with .class: " + signature);
    }

    String className = signature.substring(0, signature.length() - ".class".length());
    try {
      return new ClassLiteral(Class.forName(className));
    } catch (ClassNotFoundException e) {
      throw new OperationParseException("While parsing '" + signature + ": " + e.getMessage());
    }
  }

  // Does not support getReflectionObject because that returns a method/constructor or field.

}
