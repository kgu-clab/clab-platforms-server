package page.clab.api.global.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RandomNicknameUtil {

    private static final int ADJECTIVE_SIZE = 15;

    private static final int COLOR_SIZE = 15;

    private static final int NOUN_SIZE = 25;

    public static String makeRandomNickname() {
        List<String> adjectiveArray = Arrays.asList(
                "행복한", "기쁜", "배고픈", "졸린", "신난",
                "잠자는", "코딩하는", "밥먹는", "책읽는", "알바하는",
                "데이트하는", "유튜브찍는", "공부하는", "멋진", "깔끔한"
        );

        List<String> colorArray = Arrays.asList(
                "빨간색", "주황색", "노란색", "초록색", "연두색",
                "파란색", "하늘색", "보라색", "핑크색", "검은색",
                "하얀색", "갈색", "회색", "카키색", "청록색"
        );

        List<String> nounArray = Arrays.asList(
                "책상", "키보드", "의자", "연필", "가방",
                "손수건", "물병", "머리끈", "옷장", "액자",
                "시계", "휴대폰", "마우스", "콘센트", "JAVA",
                "C언어", "파이썬", "코틀린", "HTML", "CSS",
                "어셈블리어", "SQL", "사탕", "떡볶이", "치킨"
        );

        String adjective = adjectiveArray.get((int)(Math.random() * ADJECTIVE_SIZE));
        String color = colorArray.get((int)(Math.random() * COLOR_SIZE));
        String noun = nounArray.get((int)(Math.random() * NOUN_SIZE));

        return (adjective + color + noun);
    }

}
