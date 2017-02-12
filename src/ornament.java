import processing.core.*;
import processing.opengl.PShader;
import processing.serial.*;

public class ornament extends PApplet {

    public static final int WIDTH = 1400;
    public static final int HEIGHT = 900;
    public static int gridSize = 60;
    public static int nX = (ornament.WIDTH / ornament.gridSize) + 1;
    public static int nY = (ornament.HEIGHT / ornament.gridSize) + 1;
    public PGraphics canvas, passH, passV;
    public PShader blur;
    public Serial port;
    public float[] lightV = new float[2];

    public static float[][] vectors = new float[6][2];

    public float[] vL = {-0.87f, -0.5f};
    public float[] vR = {0.87f, -0.5f};
    public float[] vU = {0, 1};
    public int valueL = 0;
    public int valueR = 0;
    public int valueU = 0;

    public static int[] palette = new int[] {0, 70, 150, 220, 160, 80, 20};

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
        frameRate(30);

        float arcAngle = TWO_PI/6;

        for (int i = 0; i < vectors.length; i++) {
            vectors[i][0] = cos(arcAngle*i);
            vectors[i][1] = sin(arcAngle*i);
        }

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

        printArray(Serial.list());
        port = new Serial(this, "COM3", 9600);
        port.bufferUntil(10);
    }

    public void draw() {
        // Move and display all "stripes"
        background(0);
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

        int scalar = 200;
        fill(255);
        strokeWeight(1);
        float uScale = valueU/255.0f;
        float rScale = valueR/255.0f;
        float lScale = valueL/255.0f;

        lightV[0] = (vU[0]*uScale + vR[0]*rScale + vL[0]*lScale)/3;
        lightV[1] = (vU[1]*uScale + vR[1]*rScale + vL[1]*lScale)/3;

        stroke(0,0,255);
        line(0, HEIGHT/2, WIDTH, HEIGHT/2);
        line(WIDTH/2, 0, WIDTH/2, HEIGHT);

        stroke(255);
        pushMatrix();
        translate(WIDTH/2, HEIGHT/2);
        drawVector(vU[0], vU[1], scalar);
        drawVector(vL[0], vL[1], scalar);
        drawVector(vR[0], vR[1], scalar);
        stroke(255, 180, 0);
        strokeWeight(2);
        drawVector(vU[0], vU[1], scalar*uScale);
        drawVector(vL[0], vL[1], scalar*lScale);
        drawVector(vR[0], vR[1], scalar*rScale);
        drawVector(lightV[0], lightV[1], scalar);
        popMatrix();

        pushMatrix();
        stroke(0, 180, 100);
        translate(WIDTH*0.25f, HEIGHT*0.75f);
        drawVector(vectors[0][0], vectors[0][1], scalar/2);
        drawVector(vectors[1][0], vectors[1][1], scalar/2);
        drawVector(vectors[2][0], vectors[2][1], scalar/2);
        drawVector(vectors[3][0], vectors[3][1], scalar/2);
        drawVector(vectors[4][0], vectors[4][1], scalar/2);
        drawVector(vectors[5][0], vectors[5][1], scalar/2);
        popMatrix();
        textSize(24);

        calcPalette(lightV);
        noStroke();
        for (int i = 0; i < palette.length; i++) {
            fill(palette[i]);
            rect(50*i+10, 10, 50, 30);
        }


        fill(0, 180);
        text("u:\t " + valueU + "\t> " + uScale, 800+2, HEIGHT*0.7f+2);
        text("r:\t " + valueR + "\t> " + rScale, 800+2, HEIGHT*0.7f+30+2);
        text("l:\t " + valueL + "\t> " + lScale, 800+2, HEIGHT*0.7f+60+2);
        fill(255);
        text("u:\t " + valueU + "\t> " + uScale, 800, HEIGHT*0.7f);
        text("r:\t " + valueR + "\t> " + rScale, 800, HEIGHT*0.7f+30);
        text("l:\t " + valueL + "\t> " + lScale, 800, HEIGHT*0.7f+60);
    }

    public void drawVector(float x, float y, float scalar) {
        line(0,0, x*scalar, y*scalar);
        rect(x*scalar-3, y*scalar-3, 6, 6);
        textSize(10);
        fill(0);
        text(x, x*scalar+1, y*scalar+1);
        text(y, x*scalar+1, y*scalar+12);
        fill(255);
        text(x, x*scalar, y*scalar);
        text(y, x*scalar, y*scalar+11);
    }

    public void serialEvent(Serial p) {
        String inString = p.readString().replace("\n", "");
        String[] values = inString.split(",");
        try {
            valueL = Integer.parseInt(values[0]);
            valueR = Integer.parseInt(values[1]);
            valueU = Integer.parseInt(values[2]);
        } catch (Exception e) {
            System.out.println("uhm ok, let me see ... holy moly, looks like a " + e + "! jeez");
        }
    }

    public void calcPalette(float[] vector) {
        float[] s = new float[7];
        for (int i = 0; i < 6; i++) {
            s[i] = ((vectors[i][0] * vector[0]) + (vectors[i][1] * vector[1]));
        }
        float min = getMin(s);
        float max = getMax(s);
        for (int i = 0; i < 7; i++) {
            palette[i] = (int) map(s[i], min, max, 0, 255) * ((255*3)-(valueU+valueL+valueR))/(255*3);
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