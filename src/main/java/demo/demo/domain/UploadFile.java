package demo.demo.domain;

import lombok.Data;

import static demo.demo.config.FileConst.FILE_DEFAULT_IMAGE_PRODUCT;

@Data
public class UploadFile {
    /*
    storeFileName : 서버 내부에서 관리하는 파일명
     */
    private String storeFileName;
    public static UploadFile DEFAULT_IMAGE_PRODUCT= new UploadFile(FILE_DEFAULT_IMAGE_PRODUCT);

    public UploadFile(String storeFileName) {
        this.storeFileName = storeFileName;
    }
}
