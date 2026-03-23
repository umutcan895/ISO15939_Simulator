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



    double calculateTheScoreForGivenDataSet() {
        double calculatedScore = 0.0;
    if(direction.equalsIgnoreCase("Higher")) {
         calculatedScore = 1.0 + ((rawValue - minRange) / (maxRange - minRange)) * 4.0;
    }else if(direction.equalsIgnoreCase("Lower")) {
         calculatedScore = 5.0- ((rawValue - minRange) / (maxRange - minRange))*4.0;
    }

        if (calculatedScore < 1.0) {
            calculatedScore = 1.0;
        }
        else if (calculatedScore > 5.0) {
            calculatedScore = 5.0;
        }

        calculatedScore = Math.round(calculatedScore * 2.0) /2.0 ;


    return calculatedScore;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getMinRange() {
        return minRange;
    }

    public void setMinRange(double minRange) {
        this.minRange = minRange;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getRawValue() {
        return rawValue;
    }

    public void setRawValue(double rawValue) {
        this.rawValue = rawValue;
    }
}
