package kek.math;

import java.util.List;

public interface Operation<T extends Numeric<T>> {
    String getName();

    List<Parameter> getParameters();
    List<LogEntry> apply(List<T> parameters);

    record Parameter(String description) {}
}
