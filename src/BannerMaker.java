import javax.swing.JFrame;
import java.awt.BorderLayout;

public class BannerMaker extends JFrame
{
    public static BannerMaker window;
    private Banner banner;

    public static void main(String[] args)
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

