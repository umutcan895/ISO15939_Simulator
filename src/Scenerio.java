import java.util.ArrayList;

public class Scenerio {
    private String name;
    private ArrayList<Dimension> dimensions = new ArrayList<>();

    public Scenerio(String name) { this.name = name; }

    public void addDimension(Dimension d) { dimensions.add(d); }

    public double calculateFinalScore() {
        double weightedSum = 0, totalWeight = 0;
        for (Dimension d : dimensions) {
            weightedSum += d.calculateDimensionScore() * d.getCoefficient();
            totalWeight += d.getCoefficient();
        }
        return totalWeight == 0 ? 1.0 : weightedSum / totalWeight;
    }

    public String getName() { return name; }
    public ArrayList<Dimension> getDimensions() { return dimensions; }
}