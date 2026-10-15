public class Complex {
    private double x, y;

    public Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { 
        return x;
    }
    public double getY() {
        return y; 
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public Complex add(Complex other) {
        return new Complex(x + other.x, y + other.y);
    }

    public Complex sub(Complex other) {
        return new Complex(x - other.x, y - other.y);
    }

    public Complex mul(Complex other) {
        return new Complex(x * other.x - y * other.y, x * other.y + y * other.x);
    }

    public Complex div(Complex other) {
        double k = other.x * other.x + other.y * other.y;
        if (k == 0) {
            throw new ArithmeticException("деление на ноль");
        }
        return new Complex((x * other.x + y * other.y) / k, (y * other.x - x * other.y) / k);
    }

    @Override
    public String toString() {
        if (y == 0) return "" + x;
        if (x == 0) return y + "i";
        return x + (y > 0 ? "+" : "") + y + "i";
    }
}