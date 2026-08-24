package crossyLane;

import demoapp.DemoApp;
import processing.core.PApplet;

public class CrossyLaneApp extends PApplet {

    private float x = 100;

    @Override
    public void settings(){
        size(500,500);
    }

    @Override
    public void setup(){
        text("Hello Processing in IntelliJ IDEA", 90, 100);
    }

    public static void main(String[] args) {
        PApplet.main(CrossyLaneApp.class, args);
    }


}
