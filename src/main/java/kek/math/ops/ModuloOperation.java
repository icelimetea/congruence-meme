package kek.math.ops;

import kek.math.LogEntry;
import kek.math.Numeric;
import kek.math.Operation;

import java.util.List;

public final class ModuloOperation<T extends Numeric<T>> implements Operation<T> {
    @Override
    public String getName() {
        return "Взятие числа по модулю";
    }

    @Override
    public List<Parameter> getParameters() {
        return List.of(new Parameter("Делимое"), new Parameter("Модуль"));
    }

    @Override
    public List<LogEntry> apply(List<T> parameters) {
        T a = parameters.get(0);
        T b = parameters.get(1);

        T result = a.divide(b).remainder();

        return List.of(new ModuloStep<>(a, b, result));
    }

    public record ModuloStep<U extends Numeric<U>>(
            U a,
            U b,
            U result
    ) implements LogEntry {
        @Override
        public String toLogString() {
            return "= %s mod %s = %s".formatted(a, b, result);
        }
    }
}
