import javax.swing.SwingUtilities;

public class Main {
public static void main(String[] args) {

SwingUtilities.invokeLater(() ->{
    new MainFrame();
});
    // Made The Calculation Comment Line Beacuse The Calculation Is Made in MainFrame
    //I got help from AI for making this part commment line
  /*  Metric susScore = new Metric("SUS score", "Higher", 50.0, 0.0, 100.0, "Points");
    susScore.setRawValue(85.0);
    double calculatedMetricScore = susScore.calculateTheScoreForGivenDataSet();
    susScore.setScore(calculatedMetricScore);


    Dimension usability =new Dimension("Usability",0.5);
    usability.addMetric(susScore);
    Scenerio educationLMS = new Scenerio("Education Management System");
    educationLMS.addDimension(usability);
    System.out.println("Yazilim Kalite Olcumu");
    System.out.println("Senaryo: " + educationLMS.getName());
    System.out.println("---------------------------");
    System.out.println("Metric Adi: " + susScore.getName());
    System.out.println("Hesaplanan Puan (1-5): " + susScore.getScore() );
    System.out.println("------------------------------");

double finalDimensionScore = usability.calculateDimensionScore();
    System.out.println("Boyut Toplam Puani: " + finalDimensionScore);



   */


}
}
