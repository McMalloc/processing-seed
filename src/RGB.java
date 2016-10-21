public class RGB {

    private int R, G, B;
    private float frequency = 5f / (ornament.numberOfTiles / 2);

    public RGB(int index){
        if (index > ornament.numberOfTiles / 2) {
            this.R = (int) (ornament.sin(frequency*(ornament.numberOfTiles-index) + 0) * 127) + 128;
            this.G = (int) (ornament.sin(frequency*(ornament.numberOfTiles-index) + 2) * 127) + 128;
            this.B = (int) (ornament.sin(frequency*(ornament.numberOfTiles-index) + 4) * 127) + 128;
        } else {
            this.R = (int) (ornament.sin(frequency*index + 0) * 127) + 128;
            this.G = (int) (ornament.sin(frequency*index + 2) * 127) + 128;
            this.B = (int) (ornament.sin(frequency*index + 4) * 127) + 128;
        }
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

}