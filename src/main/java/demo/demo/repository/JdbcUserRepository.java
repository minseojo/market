package demo.demo.repository;

import demo.demo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository {

    private final DataSource dataSource;

    private User user(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String userId = rs.getString("userId");
        String password = rs.getString("password");
        String nickname = rs.getString("nickname");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String address = rs.getNString("address");
        String phoneNumber = rs.getString("phoneNumber");
        String registrationDate = rs.getString("registrationDate");
        return User.builder()
                .id(id)
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .name(name)
                .email(email)
                .address(address)
                .phoneNumber(phoneNumber)
                .registrationDate(registrationDate)
                .build();
    }

    public User sava(User user) {
        String sql = "insert into User(nickname, userId, password, name, email, phoneNumber, address, registrationDate) values(?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getNickname());
            pstmt.setString(2, user.getUserId());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getName());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhoneNumber());
            pstmt.setString(7, user.getAddress());
            pstmt.setString(8, user.getRegistrationDate());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return user;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public Optional<User> findById(Long id) {
        String sql = "select * from User where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                User user = user(rs);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    public Optional<User> findByUserId(String userId) {
        String sql = "select * from User where userId = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                User user = user(rs);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }



}
