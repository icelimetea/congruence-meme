package kek.math.util;

import kek.math.Numeric;

public final class ExtendedGcd<T extends Numeric<T>> {
    private final StepListener<T> listener;

    public ExtendedGcd() {
        this((a, b, result) -> {});
    }

    public ExtendedGcd(StepListener<T> listener) {
        this.listener = listener;
    }

    public Result<T> compute(T a, T b) {
        Result<T> result;

        if (!b.equals(b.zero())) {
            Numeric.DivisionResult<T> q = a.divide(b);

            Result<T> subResult = compute(b, q.remainder());

            result = new Result<>(
                    subResult.gcd(),
                    subResult.y(),
                    subResult.x().subtract(q.quotient().multiply(subResult.y()))
            );
        } else {
            result = new Result<>(a, a.one(), a.zero());
        }

        listener.onIteration(a, b, result);

        return result;
    }

    public interface StepListener<U extends Numeric<U>> {
        void onIteration(U a, U b, Result<U> result);
    }

    public record Result<U extends Numeric<U>>(
            U gcd,
            U x,
            U y
    ) {}
}
