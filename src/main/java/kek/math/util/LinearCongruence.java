package kek.math.util;

import kek.math.Numeric;

public record LinearCongruence<T extends Numeric<T>>(
        T variableCoefficient,
        T freeCoefficient,
        T modulo
) {
    @Override
    public String toString() {
        return "%sx ≡ %s (mod %s)".formatted(
                variableCoefficient.equals(variableCoefficient.one()) ? "" : variableCoefficient,
                freeCoefficient,
                modulo
        );
    }
}
