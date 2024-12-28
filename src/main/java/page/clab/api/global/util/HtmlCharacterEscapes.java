package page.clab.api.global.util;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.StringEscapeUtils;

/**
 * {@code HtmlCharacterEscapes}는 HTML 특수 문자를 JSON에서 안전하게 사용할 수 있도록 이스케이프 처리를 담당하는 클래스입니다.
 *
 * <p>특히 JSON 내에서 HTML 관련 특수 문자가 안전하게 사용될 수 있도록 다음과 같은 문자들을
 * 커스텀 이스케이프 처리합니다:
 * <ul>
 *     <li>&lt; (less than)</li>
 *     <li>&gt; (greater than)</li>
 *     <li>" (double quote)</li>
 *     <li>( (left parenthesis)</li>
 *     <li>) (right parenthesis)</li>
 *     <li># (hash symbol)</li>
 *     <li>' (single quote)</li>
 * </ul>
 * </p>
 *
 * <p>이 클래스는 또한 이모지나 Zero Width Joiner와 같은 특수 유니코드 문자를
 * UTF-16 형태로 이스케이프 처리하여 JSON 내에서 표현할 수 있도록 합니다.</p>
 */
public class HtmlCharacterEscapes extends CharacterEscapes {

    private static final char ZERO_WIDTH_JOINER = 0x200D;
    private final int[] asciiEscapes;

    public HtmlCharacterEscapes() {
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        char charAt = (char) ch;
        if (Character.isHighSurrogate(charAt) || Character.isLowSurrogate(charAt) || charAt == ZERO_WIDTH_JOINER) {
            String sb = "\\u" +
                String.format("%04x", ch);
            return new SerializedString(sb);
        } else {
            return new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString(charAt)));
        }
    }
}
