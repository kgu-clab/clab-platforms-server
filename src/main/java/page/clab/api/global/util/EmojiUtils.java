package page.clab.api.global.util;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;

import java.text.BreakIterator;

public class EmojiUtils {

    public static boolean isEmoji(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(text);
        int start = it.first();
        int end = it.next();

        if (end != BreakIterator.DONE && it.next() == BreakIterator.DONE) {
            String grapheme = text.substring(start, end);

            if (grapheme.length() == 1 && UCharacter.isDigit(grapheme.codePointAt(0)))
                return false;

            for (int i = 0; i < grapheme.length(); ) {
                int codePoint = grapheme.codePointAt(i);

                if (UCharacter.hasBinaryProperty(codePoint, UProperty.EMOJI) ||
                        UCharacter.hasBinaryProperty(codePoint, UProperty.EMOJI_PRESENTATION) ||
                        UCharacter.hasBinaryProperty(codePoint, UProperty.EXTENDED_PICTOGRAPHIC)) {
                    return true;
                }
                i += Character.charCount(codePoint);
            }
        }
        return false;
    }

}
