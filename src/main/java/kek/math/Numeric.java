package kek.math;

public interface Numeric<T extends Numeric<T>> {
    boolean equals(T other);
    boolean greater(T other);

    T zero();
    T one();

    T add(T addendum);
    T subtract(T subtrahend);
    T multiply(T multiplier);
    DivisionResult<T> divide(T divisor);

    record DivisionResult<T>(T quotient, T remainder) {}
}
