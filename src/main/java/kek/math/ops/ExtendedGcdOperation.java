package kek.math.ops;

import kek.math.Numeric;
import kek.math.LogEntry;
import kek.math.Operation;
import kek.math.util.ExtendedGcd;

import java.util.ArrayList;
import java.util.List;

public final class ExtendedGcdOperation<T extends Numeric<T>> implements Operation<T> {
    @Override
    public String getName() {
        return "Расширенный алгоритм Евклида";
    }

    @Override
    public List<Parameter> getParameters() {
        return List.of(new Parameter("Число 1"), new Parameter("Число 2"));
    }

    @Override
    public List<LogEntry> apply(List<T> parameters) {
        T param1 = parameters.get(0);
        T param2 = parameters.get(1);

        List<LogEntry> log = new ArrayList<>();

        new ExtendedGcd<T>((a, b, result) -> log.add(new ExtendedGcdStep<>(a, b, result))).compute(param1, param2);

        return log;
    }

    public record ExtendedGcdStep<U extends Numeric<U>>(U a, U b, ExtendedGcd.Result<U> result) implements LogEntry {
        @Override
        public String toLogString() {
            return "= gcd(%s, %s) = %s * %s + %s * %s = %s".formatted(a, b, a, result.x(), b, result.y(), result.gcd());
        }
    }
}
