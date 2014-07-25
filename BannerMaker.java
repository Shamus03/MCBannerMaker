import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class BannerMaker extends JFrame
{
    public static BannerMaker window;
    private Banner banner;

    public static void main(String args[])
    {
        window = new BannerMaker();
        window.setVisible(true);
    }

    public BannerMaker()
    {
        super("Banner Maker");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        banner = new Banner(Banner.BannerColor.BLACK.getColor());

        add(new BannerPanel(this), BorderLayout.WEST);
        add(new BannerEditor(this), BorderLayout.EAST);
    }

    public Banner getBanner()
    {
        return banner;
    }
}

class BannerEditor extends JPanel
{
    private BannerMaker parent;
    JComboBox comboBox;
    JComboBox colorBox;
    JComboBox styleBox;
    
    JComboBox[] layerStyles = new JComboBox[Banner.NUM_LAYERS];
    JComboBox[] layerColors = new JComboBox[Banner.NUM_LAYERS];

    public BannerEditor(BannerMaker parent)
    {
        setPreferredSize(new Dimension(550, 500));

        this.parent = parent;
        int i;       
        setLayout(new GridLayout(3 + Banner.NUM_LAYERS, 1));

        add(new JPanel());
        add(new JLabel("Style"));
        add(new JLabel("Color"));

        add(new JLabel("Background:"));
        add(new JLabel("N/A"));

        comboBox = new JComboBox(Banner.BannerColor.values());
        comboBox.addItemListener(new ColorChangedListener(parent.getBanner()));
        comboBox.setSelectedItem(Banner.BannerColor.WHITE);

        JPanel panel = new JPanel();
        panel.add(comboBox);
        add(panel);


        for (i = 0; i < Banner.NUM_LAYERS; i++)
        {
            add(new JLabel("Layer " + (i + 1) + ":"));

            styleBox = new JComboBox(Banner.Style.values());
            layerStyles[i] = styleBox;
            
            JPanel comboPanel = new JPanel();
            comboPanel.add(styleBox);
            add(comboPanel);
            
            colorBox = new JComboBox(Banner.BannerColor.values());

            styleBox.addItemListener(new StyleChangedListener(
                parent.getBanner().getLayer(i), colorBox));

            colorBox.addItemListener(new ColorChangedListener(
                parent.getBanner().getLayer(i)));
            colorBox.setSelectedItem(Banner.BannerColor.RED);
            layerColors[i] = colorBox;
            colorBox.setEnabled(false);

            comboPanel = new JPanel();
            comboPanel.add(colorBox);
            add(comboPanel);
        }
        
        JPanel buttonPanel = new JPanel();
        JButton btnReset = new JButton("Reset Fields");
        buttonPanel.add(btnReset);
        btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBox.setSelectedIndex(Banner.BannerColor.WHITE.ordinal()); 
				for (int i = 0; i < Banner.NUM_LAYERS; i++)		
				{
					layerStyles[i].setSelectedIndex(Banner.Style.BLANK.ordinal()); 
					layerColors[i].setSelectedIndex(Banner.BannerColor.RED.ordinal());			
				}
			}
		});
        add(buttonPanel);
    }
}

class BannerPanel extends JPanel
{
    public static final Color background = new Color(150, 150, 100);

    private BannerMaker parent;

    public BannerPanel(BannerMaker parent)
    {
        setPreferredSize(new Dimension(300, 500));
        setBackground(background);
        this.parent = parent;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        parent.getBanner().draw(g, 50, 35);

        repaint();
    }
}

class Banner implements Colorable
{
    public static final int NUM_LAYERS = 6;

    static enum Style
    {
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
        Style(String string)
        {
            this.string = string;
        }

        public String toString()
        {
            return string;
        }
    }

    static final Color MASK = new Color(0, 0, 0, 20); //adds alpha to banner

    static enum BannerColor
    {
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
        BannerColor(int color, String string)
        {
            this.color = new Color(color);
            this.string = string;
        }

        public Color getColor()
        {
            return color;
        }

        public String toString()
        {
            return string;
        }
    }


    private Color baseColor;
    private Layer[] layers = new Layer[NUM_LAYERS];

    public Banner(Color baseColor)
    {
        this.baseColor = baseColor;

        for (int i = 0; i < NUM_LAYERS; i++)
            layers[i] = new Layer(Style.BLANK, baseColor);
    }

    public void setColor(Color color)
    {
        this.baseColor = color;
    }
    
    public Color getColor()
    {
        return baseColor;
    }

    public void draw(Graphics g, int x, int y)
    {
        g.setColor(baseColor);
        g.fillRect(x, y, 200, 400); 

        
        for(int i = 0; i < NUM_LAYERS; i++)
            layers[i].draw(g, x, y);
        
        drawBannerMask(g,x,y);
    }
    
    public void drawBannerMask(Graphics g, int x, int y)
    {
        g.setColor(Banner.MASK);
        g.fillRect(x, y, 200, 10);
        g.fillRect(x, y + 390, 200, 10);
        g.fillRect(x, y + 10, 10, 380);
        g.fillRect(x + 190, y + 10, 10, 380);
    }

    public Layer getLayer(int i)
    {
        return layers[i];
    }

    class Layer implements Colorable, Styleable
    {
        private Style style;
        private Color color;

        public Layer(Style style, Color color)
        {
            this.style = style;
            this.color = color; 
        }
        
        public void draw(Graphics g, int x, int y)
        {
            g.setColor(color);

            int i;
            int j;

            switch(style)
            {
                case BLANK:
                    break;
                case UPPER_LEFT_SQUARE:
                    g.fillRect(x, y, 90, 130);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 90, y, 10, 130);
                    g.fillRect(x, y + 130, 100, 10);
                    break;
                case UPPER_RIGHT_SQUARE:
                    g.fillRect(x + 110, y, 90, 130);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 100, y, 10, 130);
                    g.fillRect(x + 100, y + 130, 100, 10);
                    break;
                case LOWER_LEFT_SQUARE:
                    g.fillRect(x, y + 270, 90, 130);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 90, y + 270, 10, 130);
                    g.fillRect(x, y + 260, 100, 10);
                    break;
                case LOWER_RIGHT_SQUARE:
                    g.fillRect(x + 110, y + 270, 90, 130);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 100, y + 270, 10, 130);
                    g.fillRect(x + 100, y + 260, 100, 10);
                    break;
                case BOTTOM_STRIPE:
                    g.fillRect(x, y + 270, 200, 130);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y + 260, 200, 10);
                    break;
                case TOP_STRIPE:
                    g.fillRect(x, y, 200, 130);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y + 130, 200, 10);
                    break;
                case LEFT_STRIPE:
                    g.fillRect(x, y, 60, 400);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 60, y, 10, 400);
                    break;
                case RIGHT_STRIPE:
                    g.fillRect(x + 140, y, 60, 400);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 130, y, 10, 400);
                    break;
                case CENTER_STRIPE:
                    g.fillRect(x + 80, y, 40, 400);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 70, y, 10, 400);
                    g.fillRect(x + 120, y, 10, 400);
                break;
                case MIDDLE_STRIPE:
                    g.fillRect(x, y + 180, 200, 40);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y + 170, 200, 10);
                    g.fillRect(x, y + 220, 200, 10);
                    break;
                case DOWN_RIGHT_DIAGONAL:
                    for (i = 0; i < 18; i++)
                        g.fillRect(x + 10 * i, y + 20 * i, 30, 60);
                    g.setColor(getMaskColor(color));
                    for (i = 0; i < 17; i++)
                    {
                        g.fillRect(x + 10 * i, y + 60 + 20 * i, 10, 20);
                        g.fillRect(x + 30 + 10 * i, y + 20 * i, 10, 20); 
                    }
                    break;
                case DOWN_LEFT_DIAGONAL:
                    for (i = 0; i < 18; i++)
                        g.fillRect(x + 170 - 10 * i, y + 20 * i, 30, 60);
                    g.setColor(getMaskColor(color));
                    for (i = 0; i < 17; i++)
                    {
                        g.fillRect(x + 190 - 10 * i, y + 60 + 20 * i, 10, 20);
                        g.fillRect(x + 160 - 10 * i, y + 20 * i, 10, 20); 
                    }
                    break;
                case SMALL_STRIPES:
                    for (i = 0; i < 4; i++)
                    {
                        g.setColor(color);
                        g.fillRect(x + 20 + 50 * i, y, 10, 400);
                        g.setColor(getMaskColor(color));
                        g.fillRect(x + 10 + 50 * i, y, 10, 400);
                        g.fillRect(x + 30 + 50 * i, y, 10, 400);
                    }
                    break;
                case CROSS:
                    j = 0;
                    for (i = 0; i < 19; i++)
                    {
                        j = 180 - Math.abs(180 - 20 * i);
                        g.fillRect(x + 10 * i, y + j, 20, 40);
                        g.fillRect(x + 10 * i, y + 360 - j, 20, 40);
                    }
                    g.fillRect(x + 80, y + 180, 40, 60);
                    g.setColor(getMaskColor(color));

                    j = 0;
                    for (i = 0; i < 16; i++)
                    {
                        j = 150 - Math.abs(150 - 20 * i);
                        g.fillRect(x + 20 + 10 * i, y + j, 10, 20);
                        g.fillRect(x + 20 + 10 * i, y + 380 - j, 10, 20);
                    }

                    j = 0;
                    for (i = 0; i < 16; i++)
                    {
                        j = 75 - Math.abs(75 - 10 * i);
                        g.fillRect(x + j, y + 40 + 20 * i, 10, 20);
                        g.fillRect(x + 190 - j, y + 40 + 20 * i, 10, 20);
                    }
                    break;
                case STRAIGHT_CROSS:
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 80, y, 40, 400);
                    g.fillRect(x, y + 180, 80, 40);
                    g.fillRect(x + 120, y + 180, 80, 40);

                    g.setColor(color);
                    g.fillRect(x + 90, y, 20, 400);
                    g.fillRect(x, y + 190, 200, 20);
                    break;
                case BORDER:
                    g.fillRect(x, y, 200, 20);
                    g.fillRect(x, y + 380, 200, 20);
                    g.fillRect(x, y + 20, 20, 360);
                    g.fillRect(x + 180, y + 20, 20, 360);

                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 20, y + 20, 160, 10);
                    g.fillRect(x + 20, y + 370, 160, 10);
                    g.fillRect(x + 20, y + 30, 10, 340);
                    g.fillRect(x + 170, y + 30, 10, 340);
                    break;
                case CURLY_BORDER:
                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y, 200, 10);
                    g.fillRect(x, y, 10, 400);
                    g.fillRect(x + 190, y, 10, 400);
                    g.fillRect(x, y + 390, 200, 10);


                    g.fillRect(x, y, 20, 90);
                    g.fillRect(x, y, 40, 70);
                    g.fillRect(x, y, 50, 60);
                    g.fillRect(x + 50, y, 10, 50);
                    g.fillRect(x, y, 70, 40);
                    g.fillRect(x, y, 90, 20);

                    g.setColor(color);
                    g.fillRect(x, y, 10, 90);
                    g.fillRect(x, y, 20, 70);
                    g.fillRect(x, y, 40, 60);
                    g.fillRect(x, y, 60, 40);
                    g.fillRect(x, y, 70, 20);
                    g.fillRect(x, y, 90, 10);


                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 180, y, 20, 90);
                    g.fillRect(x + 160, y, 40, 70);
                    g.fillRect(x + 150, y, 50, 60);
                    g.fillRect(x + 140, y, 10, 50);
                    g.fillRect(x + 130, y, 70, 40);
                    g.fillRect(x + 110, y, 90, 20);

                    g.setColor(color);
                    g.fillRect(x + 190, y, 10, 90);
                    g.fillRect(x + 180, y, 20, 70);
                    g.fillRect(x + 160, y, 40, 60);
                    g.fillRect(x + 140, y, 60, 40);
                    g.fillRect(x + 130, y, 70, 20);
                    g.fillRect(x + 110, y, 90, 10);

                    
                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y + 310, 20, 90);
                    g.fillRect(x, y + 330, 40, 70);
                    g.fillRect(x, y + 340, 50, 60);
                    g.fillRect(x + 50, y + 350, 10, 50);
                    g.fillRect(x, y + 360, 70, 40);
                    g.fillRect(x, y + 380, 90, 20);

                    g.setColor(color);
                    g.fillRect(x, y + 310, 10, 90);
                    g.fillRect(x, y + 330, 20, 70);
                    g.fillRect(x, y + 340, 40, 60);
                    g.fillRect(x, y + 360, 60, 40);
                    g.fillRect(x, y + 380, 70, 20);
                    g.fillRect(x, y + 390, 90, 10);


                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 180, y + 310, 20, 90);
                    g.fillRect(x + 160, y + 330, 40, 70);
                    g.fillRect(x + 150, y + 340, 50, 60);
                    g.fillRect(x + 140, y + 350, 10, 50);
                    g.fillRect(x + 130, y + 360, 70, 40);
                    g.fillRect(x + 110, y + 380, 90, 20);

                    g.setColor(color);
                    g.fillRect(x + 190, y + 310, 10, 90);
                    g.fillRect(x + 180, y + 330, 20, 70);
                    g.fillRect(x + 160, y + 340, 40, 60);
                    g.fillRect(x + 140, y + 360, 60, 40);
                    g.fillRect(x + 130, y + 380, 70, 20);
                    g.fillRect(x + 110, y + 390, 90, 10);


                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y + 110, 20, 80);
                    g.fillRect(x, y + 130, 40, 40);
                    g.fillRect(x, y + 140, 50, 20);

                    g.setColor(color);
                    g.fillRect(x, y + 110, 10, 80);
                    g.fillRect(x, y + 130, 20, 40);
                    g.fillRect(x, y + 140, 40, 20);

                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 180, y + 110, 20, 80);
                    g.fillRect(x + 160, y + 130, 40, 40);
                    g.fillRect(x + 150, y + 140, 50, 20);

                    g.setColor(color);
                    g.fillRect(x + 190, y + 110, 10, 80);
                    g.fillRect(x + 180, y + 130, 20, 40);
                    g.fillRect(x + 160, y + 140, 40, 20);

                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y + 210, 20, 80);
                    g.fillRect(x, y + 230, 40, 40);
                    g.fillRect(x, y + 240, 50, 20);

                    g.setColor(color);
                    g.fillRect(x, y + 210, 10, 80);
                    g.fillRect(x, y + 230, 20, 40);
                    g.fillRect(x, y + 240, 40, 20);

                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 180, y + 210, 20, 80);
                    g.fillRect(x + 160, y + 230, 40, 40);
                    g.fillRect(x + 150, y + 240, 50, 20);

                    g.setColor(color);
                    g.fillRect(x + 190, y + 210, 10, 80);
                    g.fillRect(x + 180, y + 230, 20, 40);
                    g.fillRect(x + 160, y + 240, 40, 20);
                    break;
                case BOTTOM_TRIANGLE:
                    for (i = 0; i < 10; i++)
                    {
                        g.setColor(color);
                        g.fillRect(x + 10 * i, y + 390 - 20 * i,
                            20 * (10 - i), 10 + 20 * i);
                        g.setColor(getMaskColor(color));
                        if (i < 9)
                        {
                            g.fillRect(x + 10 * i, y + 370 - 20 * i, 10, 20);
                            g.fillRect(x + 190 - 10 * i, y + 370 - 20 * i,
                                10, 20);
                        }
                        else
                        {
                            g.fillRect(x + 10 * i, y + 380 - 20 * i, 10, 10);
                            g.fillRect(x + 190 - 10 * i, y + 380 - 20 * i,
                                10, 10);
                        }
                    }
                    break;
                case TOP_TRIANGLE:
                    for (i = 0; i < 10; i++)
                    {
                        g.setColor(color);
                        g.fillRect(x + 10 * i, y,
                            20 * (10 - i), 10 + 20 * i);
                        g.setColor(getMaskColor(color));
                        if (i < 9)
                        {
                            g.fillRect(x + 10 * i, y + 10 + 20 * i, 10, 20);
                            g.fillRect(x + 190 - 10 * i, y + 10 + 20 * i,
                                10, 20);
                        }
                        else
                        {
                            g.fillRect(x + 10 * i, y + 10 + 20 * i, 10, 10);
                            g.fillRect(x + 190 - 10 * i, y + 10 + 20 * i,
                                10, 10);
                        }
                    }
                    break;
                case BOTTOM_SAW:
                    g.fillRect(x, y + 390, 200, 10);
                    for (i = 0; i < 3; i++)
                    {
                        g.setColor(color);
                        g.fillRect(x + 70 * i, y + 380, 60, 20);
                        g.fillRect(x + 10 + 70 * i, y + 360, 40, 20);
                        g.fillRect(x + 20 + 70 * i, y + 340, 20, 20);
                        g.setColor(getMaskColor(color));
                        g.fillRect(x + 70 * i, y + 360, 10, 20);
                        g.fillRect(x + 10 + 70 * i, y + 340, 10, 20);
                        g.fillRect(x + 20 + 70 * i, y + 330, 20, 10);
                        g.fillRect(x + 40 + 70 * i, y + 340, 10, 20);
                        g.fillRect(x + 50 + 70 * i, y + 360, 10, 20);
                    }
                    g.fillRect(x + 60, y + 380, 10, 10);
                    g.fillRect(x + 130, y + 380, 10, 10);
                    break;
                case TOP_SAW:
                    g.fillRect(x, y, 200, 10);
                    for (i = 0; i < 3; i++)
                    {
                        g.setColor(color);
                        g.fillRect(x + 70 * i, y, 60, 20);
                        g.fillRect(x + 10 + 70 * i, y + 20, 40, 20);
                        g.fillRect(x + 20 + 70 * i, y + 40, 20, 20);
                        g.setColor(getMaskColor(color));
                        g.fillRect(x + 70 * i, y + 20, 10, 20);
                        g.fillRect(x + 10 + 70 * i, y + 40, 10, 20);
                        g.fillRect(x + 20 + 70 * i, y + 60, 20, 10);
                        g.fillRect(x + 40 + 70 * i, y + 40, 10, 20);
                        g.fillRect(x + 50 + 70 * i, y + 20, 10, 20);
                    }
                    g.fillRect(x + 60, y + 10, 10, 10);
                    g.fillRect(x + 130, y + 10, 10, 10);
                    break;
                case LEFT_DIAGONAL:
                    for (i = 0; i < 19; i++)
                        g.fillRect(x, y, 10 + i * 10, 380 - i * 20);
                    g.setColor(getMaskColor(color));
                    for (i = 0; i < 20; i++)
                        g.fillRect(x + i * 10, y + 380 - i * 20, 10, 20);
                    break;
                case RIGHT_DIAGONAL:
                    for (i = 0; i < 19; i++)
                        g.fillRect(x + 190 - i * 10, y,
                            10 + i * 10, 380 - i * 20);
                    g.setColor(getMaskColor(color));
                    for (i = 0; i < 20; i++)
                        g.fillRect(x + 190 - i * 10, y + 380 - i * 20, 10, 20);
                    break;
                case CIRCLE:
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 90, y + 140, 20, 120);
                    g.fillRect(x + 70, y + 150, 60, 100);
                    g.fillRect(x + 60, y + 160, 80, 80);
                    g.fillRect(x + 50, y + 170, 100, 60);
                    g.fillRect(x + 40, y + 190, 120, 20);
                    g.setColor(color);
                    g.fillRect(x + 90, y + 150, 20, 100);
                    g.fillRect(x + 70, y + 160, 60, 80);
                    g.fillRect(x + 60, y + 170, 80, 60);
                    g.fillRect(x + 50, y + 190, 100, 20);
                    break;
                case RHOMBUS:
                    for (i = 0; i < 7; i++)
                    {
                        g.setColor(getMaskColor(color));
                        if (i < 6)
                        {
                            g.fillRect(x + 30 + 10 * i, y + 190 - 20 * i,
                                140 - 20 * i, 20 + 40 * i);
                            g.setColor(color);
                            g.fillRect(x + 40 + 10 * i, y + 190 - 20 * i,
                                120 - 20 * i, 20 + 40 * i);
                        }
                        else
                            g.fillRect(x + 90, y + 80, 20, 240);
                    }
                    break;
                case VERTICAL_HALF:
                    g.fillRect(x, y, 100, 400);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 100, y, 10, 400);
                    break;
                case HORIZONTAL_HALF:
                    g.fillRect(x, y, 200, 200);
                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y + 200, 200, 10);
                    break;
                case CREEPER:
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 10, y + 140, 60, 60);
                    g.fillRect(x + 130, y + 140, 60, 60);
                    g.fillRect(x + 40, y + 230, 30, 90);
                    g.fillRect(x + 70, y + 200, 60, 90);
                    g.fillRect(x + 130, y + 230, 30, 90);
                    g.setColor(color);
                    g.fillRect(x + 20, y + 150, 40, 40);
                    g.fillRect(x + 140, y + 150, 40, 40);
                    g.fillRect(x + 50, y + 240, 10, 70);
                    g.fillRect(x + 80, y + 210, 40, 70);
                    g.fillRect(x + 140, y + 240, 10, 70);
                    g.fillRect(x + 50, y + 240, 100, 40);
                    break;
                case BRICKS:
                    int offset;
                    for (i = 0; i < 10; i++)
                    {
                        offset = i % 2 * 30;

                        for (j = 0; j < 4; j++)
                        {
                            g.setColor(getMaskColor(color));
                            g.fillRect(x + 60 * j - offset, y + i * 40, 50, 30);
                            g.setColor(color);
                            g.fillRect(x + 60 * j - offset, y + i * 40, 40, 20);
                            g.setColor(getMaskColor(color));
                            g.fillRect(x + 40 + 60 * j - offset, y + i * 40,
                                10, 10);
                        }
                    }
                    g.setColor(BannerPanel.background);
                    g.fillRect(x - 30, y, 30, 400);
                    g.fillRect(x + 200, y, 30, 400);
                    break;
                case GRADIENT:
                    for (i = 0; i < 40; i++)
                    {
                        g.setColor(new Color(
                            color.getRed(), color.getGreen(), color.getBlue(),
                            (int)((40 - i) / 40.0 * 255)));
                        g.fillRect(x, y + i * 10, 200, 10);
                    }
                    g.setColor(getMaskColor(color));
                    g.fillRect(x, y, 200, 10);
                    break;
                case SKULL:
                    g.setColor(getMaskColor(color));

                    g.fillRect(x + 30, y + 210, 20, 20);
                    g.fillRect(x + 50, y + 220, 20, 20);
                    g.fillRect(x + 70, y + 230, 20, 20);
                    g.fillRect(x + 30, y + 260, 30, 10);

                    g.fillRect(x + 90, y + 240, 20, 10);
                    g.fillRect(x + 60, y + 250, 80, 10);

                    g.fillRect(x + 150, y + 210, 20, 20);
                    g.fillRect(x + 130, y + 220, 20, 20);
                    g.fillRect(x + 110, y + 230, 20, 20);
                    g.fillRect(x + 140, y + 260, 30, 10);

                    g.setColor(color);

                    g.fillRect(x + 60, y + 120, 80, 40);
                    g.fillRect(x + 60, y + 190, 80, 10);
                    g.fillRect(x + 60, y + 120, 10, 80);
                    g.fillRect(x + 130, y + 120, 10, 80);
                    g.fillRect(x + 70, y + 170, 20, 10);
                    g.fillRect(x + 90, y + 160, 20, 10);
                    g.fillRect(x + 110, y + 170, 20, 10);

                    g.fillRect(x + 20, y + 200, 20, 20);
                    g.fillRect(x + 20, y + 200, 10, 30);
                    g.fillRect(x + 40, y + 220, 20, 10);
                    g.fillRect(x + 60, y + 230, 20, 10);
                    g.fillRect(x + 80, y + 240, 10, 20);

                    g.fillRect(x + 90, y + 250, 20, 10);

                    g.fillRect(x + 160, y + 200, 20, 20);
                    g.fillRect(x + 170, y + 200, 10, 30);
                    g.fillRect(x + 140, y + 220, 20, 10);
                    g.fillRect(x + 120, y + 230, 20, 10);
                    g.fillRect(x + 110, y + 240, 10, 20);

                    g.fillRect(x + 20, y + 260, 10, 30);
                    g.fillRect(x + 30, y + 270, 10, 20);
                    g.fillRect(x + 40, y + 270, 20, 10);
                    g.fillRect(x + 60, y + 260, 20, 10);

                    g.fillRect(x + 170, y + 260, 10, 30);
                    g.fillRect(x + 160, y + 270, 10, 20);
                    g.fillRect(x + 140, y + 270, 20, 10);
                    g.fillRect(x + 120, y + 260, 20, 10);

                    g.setColor(new Color(color.getRed(),
                                         color.getGreen(),
                                         color.getBlue(),
                                         90));
                    
                    g.fillRect(x + 70, y + 160, 20, 10);
                    g.fillRect(x + 90, y + 170, 20, 10);
                    g.fillRect(x + 110, y + 160, 20, 10);
                    g.fillRect(x + 70, y + 180, 60, 10);

                    break;
                case FLOWER:
                    g.setColor(getMaskColor(color));

                    g.fillRect(x + 80, y + 170, 40, 60);
                    g.fillRect(x + 70, y + 180, 60, 40);

                    g.fillRect(x + 80, y + 120 , 40, 20);
                    g.fillRect(x + 40, y + 140, 120, 20);
                    g.fillRect(x + 90, y + 110, 20, 10);

                    g.fillRect(x + 30, y + 130, 40, 10);
                    g.fillRect(x + 40, y + 120, 20, 10);
                    g.fillRect(x + 60, y + 160, 10, 10);

                    g.fillRect(x + 130, y + 130, 40, 10);
                    g.fillRect(x + 140, y + 120, 20, 10);
                    g.fillRect(x + 130, y + 160, 10, 10);


                    g.fillRect(x + 80, y + 260 , 40, 20);
                    g.fillRect(x + 40, y + 240, 120, 20);
                    g.fillRect(x + 90, y + 280, 20, 10);

                    g.fillRect(x + 30, y + 260, 40, 10);
                    g.fillRect(x + 40, y + 270, 20, 10);
                    g.fillRect(x + 60, y + 230, 10, 10);

                    g.fillRect(x + 130, y + 260, 40, 10);
                    g.fillRect(x + 140, y + 270, 20, 10);
                    g.fillRect(x + 130, y + 230, 10, 10);


                    g.fillRect(x + 10, y + 160, 10, 20);
                    g.fillRect(x + 20, y + 150, 10, 10);
                    g.fillRect(x + 20, y + 160, 20, 30);

                    g.fillRect(x + 50, y + 170, 10, 60);

                    g.fillRect(x + 10, y + 220, 10, 20);
                    g.fillRect(x + 20, y + 240, 10, 10);
                    g.fillRect(x + 20, y + 210, 20, 30);


                    g.fillRect(x + 180, y + 160, 10, 20);
                    g.fillRect(x + 170, y + 150, 10, 10);
                    g.fillRect(x + 160, y + 160, 20, 30);

                    g.fillRect(x + 140, y + 170, 10, 60);

                    g.fillRect(x + 180, y + 220, 10, 20);
                    g.fillRect(x + 170, y + 240, 10, 10);
                    g.fillRect(x + 160, y + 210, 20, 30);

                    g.setColor(color);

                    g.fillRect(x + 80, y + 180, 40, 40); 

                    g.fillRect(x + 90, y + 120, 20, 20);
                    g.fillRect(x + 50, y + 140, 100, 10);

                    g.fillRect(x + 40, y + 130, 20, 10);
                    g.fillRect(x + 50, y + 150, 20, 10);
                    g.fillRect(x + 40, y + 150, 20, 20);

                    g.fillRect(x + 140, y + 130, 20, 10);
                    g.fillRect(x + 130, y + 150, 20, 10);
                    g.fillRect(x + 140, y + 150, 20, 20);


                    g.fillRect(x + 90, y + 260, 20, 20);
                    g.fillRect(x + 50, y + 250, 100, 10);

                    g.fillRect(x + 40, y + 260, 20, 10);
                    g.fillRect(x + 50, y + 240, 20, 10);
                    g.fillRect(x + 40, y + 230, 20, 20);

                    g.fillRect(x + 140, y + 260, 20, 10);
                    g.fillRect(x + 130, y + 240, 20, 10);
                    g.fillRect(x + 140, y + 230, 20, 20);


                    g.fillRect(x + 20, y + 160, 10, 20);
                    g.fillRect(x + 30, y + 170, 10, 10);
                    
                    g.fillRect(x + 40, y + 170, 10, 60);

                    g.fillRect(x + 20, y + 220, 10, 20);
                    g.fillRect(x + 30, y + 220, 10, 10);


                    g.fillRect(x + 170, y + 160, 10, 20);
                    g.fillRect(x + 160, y + 170, 10, 10);
                    
                    g.fillRect(x + 150, y + 170, 10, 60);

                    g.fillRect(x + 170, y + 220, 10, 20);
                    g.fillRect(x + 160, y + 220, 10, 10);
                    break;
                case MOJANG_LOGO:
                    g.setColor(getMaskColor(color));
                    g.fillRect(x + 20, y + 160, 40, 110);
                    g.fillRect(x + 30, y + 150, 10, 10);
                    g.fillRect(x + 50, y + 140, 40, 10);
                    g.fillRect(x + 90, y + 150, 10, 10);
                    g.fillRect(x + 110, y + 160, 10, 10);
                    g.fillRect(x + 120, y + 150, 10, 10);
                    g.fillRect(x + 130, y + 160, 10, 10);
                    g.fillRect(x + 140, y + 120, 10, 10);
                    g.fillRect(x + 140, y + 150, 20, 10);
                    g.fillRect(x + 70, y + 170, 40, 10);
                    g.fillRect(x + 30, y + 270, 10, 10);
                    g.fillRect(x + 50, y + 280, 110, 10);
                    g.fillRect(x + 160, y + 260, 20, 20);
                    g.fillRect(x + 60, y + 240, 10, 10);
                    g.fillRect(x + 70, y + 250, 10, 10);
                    g.fillRect(x + 80, y + 260, 20, 10);
                    g.fillRect(x + 130, y + 180, 10, 10);
                    g.fillRect(x + 140, y + 190, 10, 10);
                    g.fillRect(x + 150, y + 200, 10, 10);
                    g.fillRect(x + 160, y + 220, 10, 10);
                    g.fillRect(x + 170, y + 200, 10, 20);
                    g.fillRect(x + 160, y + 170, 10, 30);

                    g.setColor(color);
                    g.fillRect(x + 30, y + 160, 20, 110);
                    g.fillRect(x + 40, y + 150, 20, 50);
                    g.fillRect(x + 40, y + 150, 30, 40);
                    g.fillRect(x + 40, y + 150, 50, 10);
                    g.fillRect(x + 40, y + 160, 70, 10);
                    g.fillRect(x + 110, y + 170, 50, 10);
                    g.fillRect(x + 120, y + 160, 10, 10);
                    g.fillRect(x + 140, y + 180, 20, 10);
                    g.fillRect(x + 150, y + 190, 10, 10);
                    g.fillRect(x + 160, y + 200, 10, 20);
                    g.fillRect(x + 140, y + 130, 20, 20);
                    g.fillRect(x + 40, y + 230, 20, 40);
                    g.fillRect(x + 60, y + 250, 10, 20);
                    g.fillRect(x + 40, y + 260, 40, 20);
                    g.fillRect(x + 80, y + 270, 90, 10);

                    break;
            }
        }

        public void setStyle(Style newStyle)
        {
            this.style = newStyle;
        }

        public Style getStyle()
        {
            return style;
        }

        public void setColor(Color newColor)
        {
            this.color = newColor;
        }

        public Color getColor()
        {
            return color;
        }

        private Color getMaskColor(Color color)
        {
            return new Color(color.getRed(),
                             color.getGreen(),
                             color.getBlue(),
                             180);
        }

        public String toString()
        {
            return "Style: " + style + "  Color: " + color;
        }
    }
}

class ColorChangedListener implements ItemListener
{
    Colorable item;

    public ColorChangedListener(Colorable item)
    {
        this.item = item;
    }

    public void itemStateChanged(ItemEvent itemEvent)
    {
        if (itemEvent.getStateChange() == itemEvent.SELECTED)
            item.setColor(((Banner.BannerColor)itemEvent.getItem()).getColor());
    }
}

class StyleChangedListener implements ItemListener
{
    Styleable item;
    JComboBox box;

    public StyleChangedListener(Styleable item, JComboBox box)
    {
        this.item = item;
        this.box = box;
    }

    public void itemStateChanged(ItemEvent itemEvent)
    {
        if (itemEvent.getStateChange() == itemEvent.SELECTED)
        {
            Banner.Style style = (Banner.Style)itemEvent.getItem();
            item.setStyle(style);
            if (style == Banner.Style.BLANK)
                box.setEnabled(false);
            else
                box.setEnabled(true);
        }
    }
}

interface Colorable
{
    public void setColor(Color color);
    public Color getColor();
}

interface Styleable
{
    public void setStyle(Banner.Style style);
    public Banner.Style getStyle();
}
