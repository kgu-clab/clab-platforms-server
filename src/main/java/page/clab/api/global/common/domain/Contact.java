package page.clab.api.global.common.domain;

import lombok.Getter;

@Getter
public class Contact {

    private final String value;

    private Contact(String value) {
        this.value = removeHyphens(value);
    }

    public static Contact of(String value) {
        return new Contact(value);
    }

    private static String removeHyphens(String value) {
        return value.replaceAll("-", "");
    }

}
