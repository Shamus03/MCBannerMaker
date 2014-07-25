import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BannerEditor extends JPanel {
    private BannerMaker parent;
    
    JComboBox comboBox;
    JComboBox colorBox;
    JComboBox styleBox;
    
    JComboBox[] layerStyles = new JComboBox[Banner.NUM_LAYERS];
    JComboBox[] layerColors = new JComboBox[Banner.NUM_LAYERS];

    public BannerEditor(BannerMaker parent) {
        setPreferredSize(new Dimension(550, 500));

        this.parent = parent;
        int i;
      
        JPanel panel;
        setLayout(new GridLayout(3 + Banner.NUM_LAYERS, 1));       

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


        for (i = 0; i < Banner.NUM_LAYERS; i++) {
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
