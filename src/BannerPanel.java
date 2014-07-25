import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class BannerPanel extends JPanel
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
