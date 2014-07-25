import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ColorChangedListener implements ItemListener {
    Colorable item;

    public ColorChangedListener(Colorable item) {
        this.item = item;
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == itemEvent.SELECTED)
            item.setColor(((Banner.BannerColor) itemEvent.getItem()).getColor());
    }
}
