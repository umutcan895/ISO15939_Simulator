    import java.util.ArrayList;

    public class Dimension {
        private String name;
        private double coefficient;
        private ArrayList<Metric> metrics;

        public Dimension(String name, double coefficient) {
            this.name = name;
            this.coefficient = coefficient;
            this.metrics = new ArrayList<>();
        }

        public void addMetric(Metric m) {
            this.metrics.add(m);
        }
       public double  calculateDimensionScore() {
            double weightedSum = 0.0;
            double totalWeight = 0.0;

            for (Metric m : metrics) {
            double currentMetricScore = m.calculateTheScoreForGivenDataSet();
            weightedSum += currentMetricScore * m.getCoefficient();
          totalWeight += m.getCoefficient();
            }

    if(totalWeight == 0.0) {
        return 0.0;
    }
    return weightedSum / totalWeight;


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

        public ArrayList<Metric> getMetrics() {
            return metrics;
        }

        public void setMetrics(ArrayList<Metric> metrics) {
            this.metrics = metrics;
        }
    }
