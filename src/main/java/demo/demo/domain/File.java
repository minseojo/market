package demo.demo.domain;

public class File {
    private Long id;
    private Long productId;
    private String originalName;
    private String saveName;
    private Long fileSize;

    File(Long id, Long productId, String originalName, String saveName) {
        this.id = id;
        this.productId = productId;
        this.originalName = originalName;
        this.saveName = saveName;
    }
}
