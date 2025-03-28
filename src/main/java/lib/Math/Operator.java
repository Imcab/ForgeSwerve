package lib.Math;

/**
 * A class that contains a functional interface for mathematical operations.
 */
public class Operator {

    /**
     * A functional interface for mathematical operations.
     */
    @FunctionalInterface
    public interface Operation{
        double apply(double a, double b);
        
    }

    @FunctionalInterface
    public interface UnaryOperation{
        double apply(double a);
     
    }

    public static final UnaryOperation NEGATE = (a) -> -a;
    public static final UnaryOperation ABSOLUTE = Math::abs;
    public static final UnaryOperation INVERSE = (a) -> 1 / a;
    public static final UnaryOperation SQUARE = (a) -> a * a;
    public static final Operation ADD = (a, b) -> a + b;
    public static final Operation SUBTRACT = (a, b) -> a - b;
    public static final Operation MULTIPLY = (a, b) -> a * b;
    public static final Operation DIVIDE = (a, b) -> a / b;
    public static final Operation MODULO = (a, b) -> a % b;
    public static final Operation POWER = Math::pow;
    public static final Operation DISTANCE = (a, b) -> Math.abs(a - b);
    public static final Operation MEAN = (a, b) -> (a + b) / 2;
    
    
}
