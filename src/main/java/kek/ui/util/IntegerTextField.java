package kek.ui.util;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public final class IntegerTextField extends JTextField {
    @Override
    protected Document createDefaultModel() {
        return new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                for (char ch : str.toCharArray())
                    if (ch < '0' || ch > '9')
                        return;

                super.insertString(offs, str, a);
            }
        };
    }
}
