package page.clab.api.global.common.file.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

public class FileUploadFailException extends FileUploadException {

    private static final String DEFAULT_MESSAGE = "파일 업로드에 실패했습니다.";

    public FileUploadFailException() {
        super(DEFAULT_MESSAGE);
    }

    public FileUploadFailException(String msg) {
        super(msg);
    }

    public FileUploadFailException(Throwable throwable) {
        super(DEFAULT_MESSAGE, throwable);
    }

    public FileUploadFailException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
