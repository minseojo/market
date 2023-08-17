package demo.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class User {

    private Long id; // 기본키, 디비에 저장 될 유저 고유 id

    private String nickname;  // 사용자가 이용할 닉네임
    private String userId; // Id
    private String password; // 비밀번호

    private String name; // 이름

    private String sex; // 성별

    private String email; // 이메일

    private String phoneNumber; // 휴대폰 번호

    private String address; //주소

    // yyyy-MM-dd HH:mm:ss (mysql DateTime)
    private String registrationDate; // 사용자 회원가입 날짜

    public boolean isSameUser(Long userId) {
        if (this.id != userId) {
            return false;
        }
        return true;
    }
    /*
    나중에 추가 할 내용
    private String lastLoginDate // 사용자가 마지막으로 로그인한 날짜와 시간을 저장합니다
    private Boolean isActive; //사용자의 활성/비활성 상태를 나타냅니다. 계정 활성화 또는 비활성화를 관리하는 데 사용될 수 있습니다.
    */

}
