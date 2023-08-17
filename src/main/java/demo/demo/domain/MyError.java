package demo.demo.domain;

import lombok.Data;

@Data
public class MyError {
    private String code;
    private String message;

    public MyError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
