package kek.math.arithmetic;

import kek.math.Numeric;

import java.math.BigInteger;
import java.util.Objects;

public final class BigIntegerNumeric implements Numeric<BigIntegerNumeric> {
    private final BigInteger value;

    public BigIntegerNumeric(BigInteger value) {
        this.value = value;
    }

    @Override
    public boolean equals(BigIntegerNumeric other) {
        return value.equals(other.value);
    }

    @Override
    public boolean greater(BigIntegerNumeric other) {
        return value.compareTo(other.value) > 0;
    }

    @Override
    public BigIntegerNumeric zero() {
        return new BigIntegerNumeric(BigInteger.ZERO);
    }

    @Override
    public BigIntegerNumeric one() {
        return new BigIntegerNumeric(BigInteger.ONE);
    }

    @Override
    public BigIntegerNumeric add(BigIntegerNumeric addendum) {
        return new BigIntegerNumeric(value.add(addendum.value));
    }

    @Override
    public BigIntegerNumeric subtract(BigIntegerNumeric subtrahend) {
        return new BigIntegerNumeric(value.subtract(subtrahend.value));
    }

    @Override
    public BigIntegerNumeric multiply(BigIntegerNumeric multiplier) {
        return new BigIntegerNumeric(value.multiply(multiplier.value));
    }

    @Override
    public DivisionResult<BigIntegerNumeric> divide(BigIntegerNumeric divisor) {
        BigInteger[] q = value.divideAndRemainder(divisor.value);

        return new DivisionResult<>(
                new BigIntegerNumeric(q[0]),
                new BigIntegerNumeric(q[1].signum() >= 0 ? q[1] : q[1].add(divisor.value))
        );
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BigIntegerNumeric that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
