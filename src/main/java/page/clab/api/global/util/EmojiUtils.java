package page.clab.api.global.util;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;

import java.text.BreakIterator;

/**
 * {@code EmojiUtils}는 주어진 텍스트가 이모지인지 여부를 확인하는 유틸리티 클래스입니다.
 * ICU 라이브러리를 사용하여 텍스트 내 코드 포인트가 이모지 속성에 해당하는지 검증합니다.
 *
 * <p>주요 기능:
 * <ul>
 *     <li>{@link #isEmoji(String)} - 주어진 문자열이 이모지인지 여부를 확인</li>
 * </ul>
 */
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
