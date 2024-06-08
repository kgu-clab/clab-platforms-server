package page.clab.api.global.common.email.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
@AllArgsConstructor
public class EmailTask {

    private final String to;

    private final String subject;

    private final String content;

    private final List<File> files;

    private final EmailTemplateType templateType;
}
