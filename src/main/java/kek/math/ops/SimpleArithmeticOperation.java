package kek.math.ops;

import kek.math.LogEntry;
import kek.math.Numeric;
import kek.math.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public final class SimpleArithmeticOperation<T extends Numeric<T>> implements Operation<T> {
    private final Type type;
    private final boolean modular;

    public SimpleArithmeticOperation(Type type, boolean modular) {
        this.type = type;
        this.modular = modular;
    }

    @Override
    public String getName() {
        return modular ? type.getName() + " (по модулю)" : type.getName();
    }

    @Override
    public List<Parameter> getParameters() {
        List<Parameter> parameters = type.getParameters();

        if (modular) {
            parameters = new ArrayList<>(parameters);
            parameters.add(new Parameter("Модуль"));
        }

        return parameters;
    }

    @Override
    public List<LogEntry> apply(List<T> parameters) {
        T a = parameters.get(0);
        T b = parameters.get(1);
        T modulo = null;

        BinaryOperator<T> op = type.getOperator();
        T result = op.apply(a, b);

        if (modular) {
            modulo = parameters.get(2);
            result = result.divide(modulo).remainder();
        }

        return List.of(new ArithmeticOperationStep<>(type, a, b, modulo, result));
    }

    public record ArithmeticOperationStep<U extends Numeric<U>>(
            Type type,
            U a,
            U b,
            U modulo,
            U result
    ) implements LogEntry {
        @Override
        public String toLogString() {
            if (modulo != null) {
                return "= (%s %s %s) mod %s = %s".formatted(a, type.getSymbol(), b, modulo, result);
            } else {
                return "= %s %s %s = %s".formatted(a, type.getSymbol(), b, result);
            }
        }
    }

    public enum Type {
        ADDITION("Сложение", "+", "Слагаемое 1", "Слагаемое 2"),
        SUBTRACTION("Вычитание", "-", "Уменьшаемое", "Вычитаемое"),
        MULTIPLICATION("Умножение", "*", "Множитель 1", "Множитель 2");

        private final String name;
        private final String symbol;

        private final List<Parameter> parameters;

        Type(String name, String symbol, String... parameters) {
            this.name = name;
            this.symbol = symbol;
            this.parameters = Stream.of(parameters).map(Parameter::new).toList();
        }

        public String getName() {
            return name;
        }

        private String getSymbol() {
            return symbol;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        private <U extends Numeric<U>> BinaryOperator<U> getOperator() {
            return switch (this) {
                case ADDITION -> Numeric::add;
                case SUBTRACTION -> Numeric::subtract;
                case MULTIPLICATION -> Numeric::multiply;
            };
        }
    }
}
