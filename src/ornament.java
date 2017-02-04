import processing.core.*;
import processing.opengl.PShader;

public class ornament extends PApplet {

    public static final int WIDTH = 1400;
    public static final int HEIGHT = 900;
    public static int gridSize = 60;
    public static int nX = (ornament.WIDTH / ornament.gridSize) + 1;
    public static int nY = (ornament.HEIGHT / ornament.gridSize) + 1;
    public PGraphics canvas, passH, passV;
    public PShader blur;

    public static int[][] vectors = new int[][] {
            {-1,2},
            {-2,0},
            {-1,2},
            {1,2},
            {2,0},
            {1,-2}
    };
    public static int[] watch = new int[] {0, 70, 150, 220, 160, 80, 20};

    public static void main(String[] args) {
        PApplet.main(new String[]{"ornament"});
    }

    public static int numberOfTiles = nX *  nY;
    private Tile[] tiles = new Tile[numberOfTiles];
    private RGB[] colors = new RGB[numberOfTiles];

    public void settings() {
        size(WIDTH, HEIGHT, P3D);
//        fullScreen();
    }

    public void setup() {
        blur = loadShader("blur.glsl");
        blur.set("blurSize", 15);
        blur.set("sigma", 15.0f);
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(this, i);
            colors[i] = new RGB(i);
        }
        canvas = createGraphics(WIDTH, HEIGHT, P2D);
        passH = createGraphics(width, height, P2D);
        passH.noSmooth();

        passV = createGraphics(width, height, P2D);
        passV.noSmooth();
        background(0);
    }

    public void draw() {
        // Move and display all "stripes"
        canvas.beginDraw();
        canvas.noStroke();
        for(Tile tile : tiles) {
            tile.display(this);
        }
        canvas.endDraw();

        blur.set("horizontalPass", 1);
        passH.beginDraw();
        passH.background(0);
        passH.shader(blur);
        passH.image(canvas, 0, 0);
        passH.endDraw();

        // Applying the blur shader along the horizontal direction
        blur.set("horizontalPass", 0);
        passV.beginDraw();
        passV.background(0);
        passV.shader(blur);
        passV.image(passH, 0, 0);
        passV.endDraw();

        tint(180, 180, 255);
        image(canvas, 0, 0);

        tint(255, 128);
        image(passV, 0, 0);



//        tint(255, 20);
//        image(canvas, 0, 0);
    }

    public float[] getDirection(int mX, int mY) {
        int centerX = WIDTH/2;
        int centerY = HEIGHT/2;
//        return ((atan2((float)(mY - centerY), (float)(mX - centerX))) * (180/PI)) + 180;
        float[] vector = new float[2];
        vector[0] = mouseX-centerX;
        vector[1] = mouseY-centerY;
        return vector;
    }

    public void mouseMoved() {
        float[] v = getDirection(mouseX, mouseY);
        float[] s = new float[7];
        for (int i = 0; i < 6; i++) {
            s[i] = ((vectors[i][0] * v[0]) + (vectors[i][1] * v[1]));
        }
        float min = getMin(s);
        float max = getMax(s);
        for (int i = 0; i < 7; i++) {
            watch[i] = (int)map(s[i], min, max, 0, 255);
        }
    }

    public float getMin(float[] array) {
        float min = Integer.MAX_VALUE;
        for (float v : array) {
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public float getMax(float[] array) {
        float max = Integer.MIN_VALUE;
        for (float v : array) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }
}