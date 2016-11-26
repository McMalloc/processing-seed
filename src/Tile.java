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
        w = ornament.gridSize*2;
        y = (int)(calculateY(index) * w * (3.0/4.0));
        if (calculateY(idx) % 2 == 0) {
            x = (int)(calculateX(index) * (ornament.sqrt(3)/2) * w + (w*(ornament.sqrt(3)/4)));
        } else {
            x = (int)(calculateX(index) * (ornament.sqrt(3)/2) * w);
        }
    }

    private int calculateX(int index) {
        return index % ornament.nX;
    }

    private int calculateY(int index) {
        return index / ornament.nX;
    }

    // Draw tile
    void display() {
        for (int i = 0; i < 7; i++) {
            applet.fill(ornament.watch[i]);
            Coordinate corner1 = getPoint(x, y, ornament.gridSize, i % 6);
            Coordinate corner2 = getPoint(x, y, ornament.gridSize, i+1 % 6);
            applet.triangle(x, y, corner1.x, corner1.y, corner2.x, corner2.y);
        }
    }

    private Coordinate getPoint(int x, int y, int size, int i) {
        Coordinate point = new Coordinate();
        float angle_deg = 60 * i   + 30;
        float angle_rad = ornament.PI / 180 * angle_deg;
        point.x = (int) (x + size * ornament.cos(angle_rad));
        point.y = (int) (y + size * ornament.sin(angle_rad));
        return point;
    }
}