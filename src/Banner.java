import java.awt.Color;
import java.awt.Graphics;

public class Banner implements Colorable {

    public static final int NUM_LAYERS = 6;

    public static enum Style {
        BLANK("Blank"),
        UPPER_LEFT_SQUARE("Upper Left Square"),
        UPPER_RIGHT_SQUARE("Upper Right Square"),
        LOWER_LEFT_SQUARE("Lower Left Square"),
        LOWER_RIGHT_SQUARE("Lower Right Square"),
        BOTTOM_STRIPE("Bottom Stripe"),
        TOP_STRIPE("Top Stripe"),
        LEFT_STRIPE("Left Stripe"),
        RIGHT_STRIPE("Right Stripe"),
        CENTER_STRIPE("Center Stripe"),
        MIDDLE_STRIPE("Middle Stripe"),
        DOWN_RIGHT_DIAGONAL("Down Right Diagonal"),
        DOWN_LEFT_DIAGONAL("Down Left Diagonal"),
        SMALL_STRIPES("Small Stripes"),
        CROSS("Cross"),
        STRAIGHT_CROSS("Straight Cross"),
        BORDER("Border"),
        CURLY_BORDER("Curly Border"),
        BOTTOM_TRIANGLE("Bottom Triangle"),
        TOP_TRIANGLE("Top Triangle"),
        BOTTOM_SAW("Bottom Saw"),
        TOP_SAW("Top Saw"),
        LEFT_DIAGONAL("Left Diagonal"),
        RIGHT_DIAGONAL("Right Diagonal"),
        CIRCLE("Circle"),
        RHOMBUS("Rhombus"),
        VERTICAL_HALF("Vertical Half"),
        HORIZONTAL_HALF("Horizontal Half"),
        CREEPER("Creeper"),
        BRICKS("Bricks"),
        GRADIENT("Gradient"),
        SKULL("Skull"),
        FLOWER("Flower"),
        MOJANG_LOGO("Mojang Logo");

        private String string;

        Style(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    static final Color MASK = new Color(0, 0, 0, 20);

    static enum BannerColor {
        BLACK(0x191919, "Black"),
        RED(0x993333, "Red"),
        GREEN(0x667F33, "Green"),
        BROWN(0x664C33, "Brown"),
        BLUE(0x334CB2, "Blue"),
        PURPLE(0x7F3FB2, "Purple"),
        CYAN(0x4C7F99, "Cyan"),
        LIGHT_GRAY(0x999999, "Light Gray"),
        GRAY(0x4C4C4C, "Gray"),
        PINK(0xF27FA5, "Pink"),
        LIME(0x7FCC19, "Lime"),
        YELLOW(0xE5E533, "Yellow"),
        LIGHT_BLUE(0x6699D8, "Light Blue"),
        MAGENTA(0xB24CD8, "Magenta"),
        ORANGE(0xD87F33, "Orange"),
        WHITE(0xFFFFFF, "White");

        private Color color;
        private String string;

        BannerColor(int color, String string) {
            this.color = new Color(color);
            this.string = string;
        }

        public Color getColor() {


            return color;
        }

        public String toString() {
            return string;
        }
    }


    private Color baseColor;
    private Layer[] layers = new Layer[NUM_LAYERS];

    public Banner(Color baseColor) {
        this.baseColor = baseColor;

        for (int i = 0; i < NUM_LAYERS; i++)
            layers[i] = new Layer(Style.BLANK, baseColor);
    }

    public void setColor(Color color) {
        this.baseColor = color;
    }

    public Color getColor() {
        return baseColor;
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(baseColor);
        g.fillRect(x, y, 200, 400);
        g.setColor(Banner.MASK);
        g.fillRect(x, y, 200, 10);
        g.fillRect(x, y + 390, 200, 10);
        g.fillRect(x, y + 10, 10, 380);
        g.fillRect(x + 190, y + 10, 10, 380);

        for (int i = 0; i < NUM_LAYERS; i++)
            layers[i].draw(g, x, y);
    }

    public Layer getLayer(int i) {
        return layers[i];
    }

}
