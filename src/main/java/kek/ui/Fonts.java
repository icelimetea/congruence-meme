package kek.ui;

import java.awt.*;

public interface Fonts {
    Font TAB_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    static int getMaxDigitWidth(FontMetrics metrics) {
        return "0123456789".codePoints().map(metrics::charWidth).max().orElseThrow();
    }
}
