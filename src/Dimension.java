import java.util.ArrayList;

public class Dimension {
    private String name;
    private double coefficient;
    private ArrayList<Metric> metrics = new ArrayList<>();

    public Dimension(String name, double coefficient) {
        this.name = name;
        this.coefficient = coefficient;
    }

    public void addMetric(Metric m) { metrics.add(m); }

    public double calculateDimensionScore() {
        double weightedSum = 0, totalWeight = 0;
        for (Metric m : metrics) {
            weightedSum += m.calculateScore() * m.getCoefficient();
            totalWeight += m.getCoefficient();
        }
        return totalWeight == 0 ? 0 : weightedSum / totalWeight;
    }

    public String getName() { return name; }
    public double getCoefficient() { return coefficient; }
    public ArrayList<Metric> getMetrics() { return metrics; }
}