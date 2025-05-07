package kek.math.util;

import kek.math.Numeric;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public final class CongruenceSolver<T extends Numeric<T>> {
    private final SolutionListener<T> listener;

    public CongruenceSolver(SolutionListener<T> listener) {
        this.listener = listener;
    }

    public LinearCongruence<T> solve(List<LinearCongruence<T>> system) {
        Set<LinearCongruence<T>> solutions = new HashSet<>();

        for (LinearCongruence<T> congruence : system) {
            ExtendedGcd.Result<T> coefficientGcd = new ExtendedGcd<T>().compute(congruence.variableCoefficient(), congruence.modulo());

            Numeric.DivisionResult<T> freeCoeffDiv = congruence.freeCoefficient().divide(coefficientGcd.gcd());

            T reducedCoefficient = freeCoeffDiv.quotient();
            T coefficientRemainder = freeCoeffDiv.remainder();

            if (!coefficientRemainder.equals(coefficientRemainder.zero()))
                throw new IrreducibleCoefficientException(congruence);

            T reducedModulo = congruence.modulo().divide(coefficientGcd.gcd()).quotient();

            LinearCongruence<T> solution = new LinearCongruence<>(
                    reducedModulo.one(),
                    reducedCoefficient
                            .multiply(coefficientGcd.x())
                            .divide(reducedModulo)
                            .remainder(),
                    reducedModulo
            );

            listener.onCongruenceSolution(congruence, solution);

            solutions.add(solution);
        }

        LinearCongruence<T> accumulator = null;

        for (LinearCongruence<T> solution : solutions) {
            if (accumulator == null) {
                accumulator = solution;
                continue;
            }

            ExtendedGcd.Result<T> moduloGcd = new ExtendedGcd<T>().compute(accumulator.modulo(), solution.modulo());

            if (!moduloGcd.gcd().equals(moduloGcd.gcd().one()))
                throw new NonCoprimeModuliException(accumulator, solution);

            T n1 = solution.freeCoefficient().multiply(accumulator.modulo()).multiply(moduloGcd.x());
            T n2 = accumulator.freeCoefficient().multiply(solution.modulo()).multiply(moduloGcd.y());

            T mergedModulo = accumulator.modulo().multiply(solution.modulo());

            LinearCongruence<T> result = new LinearCongruence<>(
                    mergedModulo.one(),
                    n1.add(n2).divide(mergedModulo).remainder(),
                    mergedModulo
            );

            listener.onSolutionMerge(accumulator, solution, result);

            accumulator = result;
        }

        if (accumulator == null)
            throw new NoSuchElementException();

        return accumulator;
    }

    public static final class IrreducibleCoefficientException extends RuntimeException {
        private final LinearCongruence<?> congruence;

        public IrreducibleCoefficientException(LinearCongruence<?> congruence) {
            this.congruence = congruence;
        }

        public LinearCongruence<?> getCongruence() {
            return congruence;
        }
    }

    public static final class NonCoprimeModuliException extends RuntimeException {
        private final LinearCongruence<?> first;
        private final LinearCongruence<?> second;

        public NonCoprimeModuliException(LinearCongruence<?> first, LinearCongruence<?> second) {
            this.first = first;
            this.second = second;
        }

        public LinearCongruence<?> getFirstCongruence() {
            return first;
        }

        public LinearCongruence<?> getSecondCongruence() {
            return second;
        }
    }

    public interface SolutionListener<T extends Numeric<T>> {
        void onCongruenceSolution(LinearCongruence<T> congruence, LinearCongruence<T> solution);
        void onSolutionMerge(LinearCongruence<T> first, LinearCongruence<T> second, LinearCongruence<T> result);
    }
}
