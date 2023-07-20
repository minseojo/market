package demo.demo.domain;

import lombok.Data;

@Data
public class UploadFile {
    /*
    storeFileName : 서버 내부에서 관리하는 파일명
     */
    private String storeFileName;

    public UploadFile(String storeFileName) {
        this.storeFileName = storeFileName;
    }
}
