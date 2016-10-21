import processing.core.*;

public class ornament extends PApplet {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    public static int gridSize = 40;
    public static int nX = (ornament.WIDTH / ornament.gridSize) + 1;
    public static int nY = (ornament.HEIGHT / ornament.gridSize) + 1;
    private int loop = 0;

    public static void main(String[] args) {
        PApplet.main(new String[]{"ornament"});
    }

    public static int numberOfTiles = (WIDTH / gridSize +1) * (HEIGHT / gridSize +1);
    private Tile[] squares = new Tile[numberOfTiles];
    private RGB[] colors = new RGB[numberOfTiles];

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        // Initialize all "stripes"
        for (int i = 0; i < squares.length; i++) {
            squares[i] = new Tile(this, i);
            colors[i] = new RGB(i);
        }

        noStroke();
        background(100);
    }

    public void draw() {
        // Move and display all "stripes"
        loop++;
        background(255);
        for(Tile square : squares) {
//            square.rotate();
            square.scale();
            square.display(colors, loop);
        }
    }
}