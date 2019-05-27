package randoop.types;

import java.util.List;
import randoop.types.LazyParameterBound.LazyBoundException;

/**
 * Represents a bound on a type variable where the bound is a {@link ReferenceType} that can be used
 * directly. Contrast with {@link LazyReferenceBound}.
 */
class EagerReferenceBound extends ReferenceBound {

  /** Whether to enable debugging output to standard out. */
  private static boolean debug = true;

  /**
   * Creates a bound for the given reference type.
   *
   * @param boundType the reference boundType
   */
  EagerReferenceBound(ReferenceType boundType) {
    super(boundType);
  }

  @Override
  public EagerReferenceBound substitute(Substitution substitution) {
    ReferenceType referenceType = getBoundType().substitute(substitution);
    if (referenceType.equals(getBoundType())) {
      return this;
    }
    return new EagerReferenceBound(referenceType);
  }

  @Override
  public EagerReferenceBound applyCaptureConversion() {
    ReferenceType referenceType = getBoundType().applyCaptureConversion();
    if (referenceType.equals(getBoundType())) {
      return this;
    }
    return new EagerReferenceBound(referenceType);
  }

  @Override
  public List<TypeVariable> getTypeParameters() {
    return getBoundType().getTypeParameters();
  }

  @Override
  public boolean isLowerBound(Type argType, Substitution subst) {
    if (debug)
      System.out.printf(
          "EagerReferenceBound.isLowerBound(%s [%s],%n %s [%s] [isParameterized()=%s], subst=%s%n",
          this, this.getClass(), argType, argType.getClass(), argType.isParameterized(), subst);
    ReferenceType boundType = this.getBoundType().substitute(subst);
    if (boundType.equals(JavaTypes.NULL_TYPE)) {
      return true;
    }
    if (boundType.isVariable()) {
      if (debug) System.out.printf("Case 1%n");
      return ((TypeVariable) boundType).getLowerTypeBound().isLowerBound(argType, subst);
    }
    if (argType.isParameterized()) {
      if (debug) System.out.printf("Case 2%n");
      if (!(boundType instanceof ClassOrInterfaceType)) {
        if (debug) System.out.printf("Case 4%n");
        return false;
      }
      InstantiatedType argClassType = (InstantiatedType) argType.applyCaptureConversion();
      InstantiatedType boundSuperType =
          ((ClassOrInterfaceType) boundType)
              .getMatchingSupertype(argClassType.getGenericClassType());
      if (boundSuperType == null) {
        if (debug) System.out.printf("Case 5%n");
        return false;
      }
      boundSuperType = boundSuperType.applyCaptureConversion();
      if (debug)
        System.out.printf(
            "EagerReferenceBound.isLowerBound calling isInstantiationOf(%s [%s], %s [%s])%n",
            boundSuperType, boundSuperType.getClass(), argClassType, argClassType.getClass());
      boolean result = boundSuperType.isInstantiationOf(argClassType);
      if (debug)
        System.out.printf(
            "EagerReferenceBound.isLowerBound called isInstantiationOf(%s [%s], %s [%s]) =>%s%n",
            boundSuperType,
            boundSuperType.getClass(),
            argClassType,
            argClassType.getClass(),
            result);
      return result;
    }
    boolean result = boundType.isSubtypeOf(argType);
    if (debug)
      System.out.printf(
          "Case 3: isSubtypeOf(%s [%s] %s [%s]) => %s %n",
          boundType, boundType.getClass(), argType, argType.getClass(), result);
    return result;
  }

  @Override
  boolean isLowerBound(ParameterBound bound, Substitution substitution) {
    assert bound instanceof EagerReferenceBound : "only handling reference bounds";
    return isLowerBound(((EagerReferenceBound) bound).getBoundType(), substitution);
  }

  @Override
  public boolean isSubtypeOf(ParameterBound bound) {
    if (bound instanceof EagerReferenceBound) {
      return this.getBoundType().isSubtypeOf(((EagerReferenceBound) bound).getBoundType());
    }
    assert false : "not handling EagerReferenceBound subtype of other bound type";
    return false;
  }

  @Override
  public boolean isUpperBound(Type argType, Substitution subst) {
    if (debug)
      System.out.printf(
          "EagerReferenceBound.isUpperBound(%s [%s],%n %s [%s] [isParameterized()=%s], subst=%s%n",
          this, this.getClass(), argType, argType.getClass(), argType.isParameterized(), subst);
    ReferenceType boundType = this.getBoundType().substitute(subst);
    if (boundType.equals(JavaTypes.OBJECT_TYPE)) {
      return true;
    }
    if (boundType.isVariable()) {
      if (debug) System.out.printf("isUpperBound Case 1%n");
      return ((TypeVariable) boundType).getUpperTypeBound().isUpperBound(argType, subst);
    }
    if (boundType.isParameterized()) {
      if (!(argType instanceof ClassOrInterfaceType)) {
        if (debug) {
          System.out.printf(
              "EagerReferenceBound.isUpperBound(%n      %s,%n      %s,%n      %s) => false (1)%n",
              this, argType, subst);
        }
        return false;
      }
      InstantiatedType boundClassType;
      try {
        boundClassType = (InstantiatedType) boundType.applyCaptureConversion();
      } catch (LazyBoundException e) {
        // Capture conversion does not (currently?) work for a lazy bound.
        if (debug) {
          System.out.printf(
              "EagerReferenceBound.isUpperBound(%n      %s,%n      %s,%n      %s) => false (2)%n",
              this, argType, subst);
        }
        return false;
      }
      InstantiatedType argSuperType =
          ((ClassOrInterfaceType) argType)
              .getMatchingSupertype(boundClassType.getGenericClassType());
      if (argSuperType == null) {
        if (debug) {
          System.out.printf(
              "EagerReferenceBound.isUpperBound(%n      %s,%n      %s,%n      %s) => false (3)%n",
              this, argType, subst);
          System.out.printf(
              "  because of getMatchingSupertype(%s [%s], %s [%s] => null%n",
              argType,
              argType.getClass(),
              boundClassType.getGenericClassType(),
              boundClassType.getGenericClassType().getClass());
        }
        return false;
      }
      argSuperType = argSuperType.applyCaptureConversion();
      if (debug)
        System.out.printf(
            "EagerReferenceBound.isUpperBound calling isInstantiationOf(%s [%s], %s [%s])%n",
            argSuperType, argSuperType.getClass(), boundClassType, boundClassType.getClass());
      boolean result = argSuperType.isInstantiationOf(boundClassType);
      if (debug)
        System.out.printf(
            "EagerReferenceBound.isUpperBound called isInstantiationOf(%s [%s], %s [%s]) =>%s%n",
            argSuperType,
            argSuperType.getClass(),
            boundClassType,
            boundClassType.getClass(),
            result);
      return result;
    }
    boolean result = argType.isSubtypeOf(boundType);
    if (debug)
      System.out.printf(
          "isUpperBound Case 3: isSubtypeOf(%s [%s] %s [%s]) => %s %n",
          argType, argType.getClass(), boundType, boundType.getClass(), result);
    return result;
  }

  @Override
  boolean isUpperBound(ParameterBound bound, Substitution substitution) {
    return isUpperBound(getBoundType(), substitution);
  }
}
