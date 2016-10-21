import processing.core.PApplet;

public class Tile {
    int x;       // horizontal location of stripe
    int y;       // horizontal location of stripe
    int w;       // width of stripe
    int idx;
    float angle;
    boolean mouse; // state of stripe (mouse is over or not?)
    PApplet applet; // The parent PApplet that we will render ourselves onto

    Tile(PApplet p, int index) {
        applet = p;
        idx = index;
        w = ornament.gridSize;
        x = calculateX(index) * w;
        y = calculateY(index) * w;
        angle = 0;
//        angle = index / 100f;
    }

    private int calculateX(int index) {
        return index % ornament.nX;
    }

    private int calculateY(int index) {
        return index / ornament.nX;
    }

    // Draw tile
    void display(RGB[] colors, int loop) {
        RGB color = colors[(idx + loop) % ornament.numberOfTiles];
        applet.fill(color.getR(), color.getG(), color.getB());
        applet.pushMatrix();
        applet.translate(x+w/2,y+w/2);
        applet.rotate(angle);
        applet.rect(0-w/2,0-w/2,w,w);
        applet.popMatrix();
    }

    // Move stripe
    void rotate() {
//        angle+=0.08;
        angle = angle + applet.random(-0.5f,0.5f);
    }
    // Scale stripe
    void scale() {
        w = w + (int) applet.random(-2f,2f);
    }
}