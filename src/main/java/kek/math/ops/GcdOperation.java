package kek.math.ops;

import kek.math.Numeric;
import kek.math.LogEntry;
import kek.math.Operation;

import java.util.ArrayList;
import java.util.List;

public final class GcdOperation<T extends Numeric<T>> implements Operation<T> {
    @Override
    public String getName() {
        return "Алгоритм Евклида";
    }

    @Override
    public List<Parameter> getParameters() {
        return List.of(new Parameter("Число 1"), new Parameter("Число 2"));
    }

    @Override
    public List<LogEntry> apply(List<T> parameters) {
        List<LogEntry> log = new ArrayList<>();

        T a = parameters.get(0);
        T b = parameters.get(1);

        log.add(new GcdIntermediateStep<>(a, b));

        while (!b.equals(b.zero())) {
            T tmp = a.divide(b).remainder();
            a = b;
            b = tmp;
            log.add(new GcdIntermediateStep<>(a, b));
        }

        log.add(new GcdFinalResult<>(a));

        return log;
    }

    public record GcdIntermediateStep<U extends Numeric<U>>(
            U a,
            U b
    ) implements LogEntry {
        @Override
        public String toLogString() {
            return "= gcd(%s, %s)".formatted(a, b);
        }
    }

    public record GcdFinalResult<U extends Numeric<U>>(
            U result
    ) implements LogEntry {
        @Override
        public String toLogString() {
            return "= %s".formatted(result);
        }
    }
}
