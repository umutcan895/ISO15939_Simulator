import java.util.ArrayList;

public class Scenerio {

    private String name;
    private ArrayList<Dimension> dimensions;


    public Scenerio(String name) {
        this.name = name;
        this.dimensions = new ArrayList<>();
    }




public void addDimension(Dimension d) {
this.dimensions.add(d);

}

public double calculateFinalQualityScore() {
double totalWeightedScore = 0.0;
double totalWeight = 0.0;


    for (Dimension d : dimensions) {

        double dimensionScore = d.calculateDimensionScore();
        totalWeightedScore += dimensionScore * d.getCoefficient();
        totalWeight += d.getCoefficient();
    }

    if (totalWeight == 0.0) {

        return 1.0;

    }

    return totalWeightedScore / totalWeight;
}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Dimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList<Dimension> dimensions) {
        this.dimensions = dimensions;
    }
}
