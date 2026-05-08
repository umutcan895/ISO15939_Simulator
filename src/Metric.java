public class Metric {
    private String name;
    private double coefficient;
    private String direction;
    private double minRange;
    private double maxRange;
    private String unit;
    private double rawValue;
    private double score;

    public Metric(String name, String direction, double coefficient, double minRange, double maxRange, String unit) {
        this.name = name;
        this.direction = direction;
        this.coefficient = coefficient;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.unit = unit;
    }

    public double calculateScore() {
        double range = maxRange - minRange;
        if (range == 0) return 1.0;

        double calc;
        if (direction.startsWith("High") || direction.contains("↑")) {
            calc = 1.0 + ((rawValue - minRange) / range) * 4.0;
        } else {
            calc = 5.0 - ((rawValue - minRange) / range) * 4.0;
        }

        calc = Math.max(1.0, Math.min(5.0, calc));
        this.score = Math.round(calc * 2.0) / 2.0;
        return this.score;
    }

    public String getName() { return name; }
    public double getCoefficient() { return coefficient; }
    public String getDirection() { return direction; }
    public double getMinRange() { return minRange; }
    public double getMaxRange() { return maxRange; }
    public String getUnit() { return unit; }
    public void setRawValue(double v) { this.rawValue = v; }
}