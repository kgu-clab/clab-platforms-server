package page.clab.api.global.common.file.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class AssignmentFileUploadFailException extends FileUploadException {

    private static final String DEFAULT_MESSAGE = "파일 업로드에 실패했습니다.";

    public AssignmentFileUploadFailException() {
        super(DEFAULT_MESSAGE);
    }

    public AssignmentFileUploadFailException(String msg) {
        super(msg);
    }

    public AssignmentFileUploadFailException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public AssignmentFileUploadFailException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
