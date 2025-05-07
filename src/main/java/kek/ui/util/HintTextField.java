package kek.ui.util;

import javax.swing.*;
import java.awt.*;

public final class HintTextField extends JTextField {
    private final String hint;

    public HintTextField(String hint) {
        this.hint = hint;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g.create();

        if (getText().isEmpty()) {
            FontMetrics metrics = getFontMetrics(getFont());
            Insets insets = getInsets();

            graphics.setFont(getFont());
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int yoff = insets.top + (getHeight() - (insets.top + insets.bottom) - metrics.getHeight()) / 2;
            graphics.drawString(hint, insets.left + 5, yoff + metrics.getAscent());
        }

        graphics.dispose();
    }
}
