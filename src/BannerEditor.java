import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;

public class BannerEditor extends JPanel
{
    private BannerMaker parent;

    public BannerEditor(BannerMaker parent)
    {
        setPreferredSize(new Dimension(550, 500));

        this.parent = parent;
        int i;
        JComboBox comboBox;
        JComboBox colorBox;
        JComboBox styleBox;
        JPanel panel;

        setLayout(new GridLayout(2 + Banner.NUM_LAYERS, 1));

        JComboBox[] layerStyles = new JComboBox[Banner.NUM_LAYERS];
        JComboBox[] layerColors = new JComboBox[Banner.NUM_LAYERS];

        add(new JPanel());
        add(new JLabel("Style"));
        add(new JLabel("Color"));

        add(new JLabel("Background:"));
        add(new JLabel("N/A"));

        comboBox = new JComboBox(Banner.BannerColor.values());
        comboBox.addItemListener(new ColorChangedListener(parent.getBanner()));
        comboBox.setSelectedItem(Banner.BannerColor.WHITE);

        panel = new JPanel();
        panel.add(comboBox);
        add(panel);


        for (i = 0; i < Banner.NUM_LAYERS; i++)
        {
            add(new JLabel("Layer " + (i + 1) + ":"));

            styleBox = new JComboBox(Banner.Style.values());
            layerStyles[i] = styleBox;

            panel = new JPanel();
            panel.add(styleBox);
            add(panel);

            colorBox = new JComboBox(Banner.BannerColor.values());

            styleBox.addItemListener(new StyleChangedListener(
                    parent.getBanner().getLayer(i), colorBox));

            colorBox.addItemListener(new ColorChangedListener(
                    parent.getBanner().getLayer(i)));
            colorBox.setSelectedItem(Banner.BannerColor.RED);
            layerColors[i] = colorBox;
            colorBox.setEnabled(false);

            panel = new JPanel();
            panel.add(colorBox);
            add(panel);
        }
    }

}
