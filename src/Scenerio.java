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
