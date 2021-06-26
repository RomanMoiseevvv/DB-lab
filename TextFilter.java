import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

class TextFilter extends DocumentFilter {
    private static final String FIELD = "[a-zA-Zа-яА-ЯёЁ0-9]";

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

        if (string.matches(FIELD)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
        if (string.matches(FIELD)) {
            super.replace(fb, offset, length, string, attrs);
        }
    }
}