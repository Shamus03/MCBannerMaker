import javax.swing.JComboBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class StyleChangedListener implements ItemListener {
    Styleable item;
    JComboBox box;

    public StyleChangedListener(Styleable item, JComboBox box) {
        this.item = item;
        this.box = box;
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == itemEvent.SELECTED) {
            Banner.Style style = (Banner.Style) itemEvent.getItem();
            item.setStyle(style);
            if (style == Banner.Style.BLANK)
                box.setEnabled(false);
            else
                box.setEnabled(true);
        }
    }
}
