import processing.core.*;

public class processingseed extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] {"processingseed"});
    }

    //	An array of stripes
    Stripe[] stripes = new Stripe[50];

    public void settings() {
        size(1000,1000);
    }

    public void setup() {
        // Initialize all "stripes"
        for (int i = 0; i < stripes.length; i++) {
            stripes[i] = new Stripe(this);
        }
    }

    public void draw() {
        background(100);
        // Move and display all "stripes"
        for (int i = 0; i < stripes.length; i++) {
            stripes[i].move();
            stripes[i].display();
        }
    }
}