package lib.Forge.Equals;

import java.util.List;
import java.util.function.Predicate;

public class Conditional {
    
    @FunctionalInterface
    private interface Chooser<T>{
        T from(T a, T b, boolean condition);        
    }

    public static<T> T chooseBetween(T a, T b, boolean condition) {
        Chooser<T> c = ((x, y, cond) -> cond ? x : y);
        return c.from(a, b, condition);
    }

    public static int chooseBetween(int a, int b, boolean condition) {
        return condition ? a : b;
    }
    
    public static String chooseBetween(String a, String b, boolean condition) {
        return condition ? a : b;
    }

    public static Double chooseBetween(Double a, Double b, boolean condition) {
        return condition ? a : b;
    }

    public static<T> T chooseBetween(T a, T b, Predicate<T> condition) {
        return condition.test(a) ? a : b;
    }

    public static <T> T chooseAmong(int index, List<T> options) {
        if (index >= 0 && index < options.size()) {
            return options.get(index);
        }
        return options.get(options.size() - 1);
    }

    public static <T> T chooseAmong(List<T> options, Predicate<T> condition, T defaultValue) {
        return options.stream().filter(condition).findFirst().orElse(defaultValue);
    }

    public static <T> T chooseAmong(boolean[] conditions, List<T> options, T defaultValue) {
        for (int i = 0; i < Math.min(conditions.length, options.size()); i++) {
            if (conditions[i]) {
                return options.get(i);
            }
        }
        return defaultValue;
    }
}
