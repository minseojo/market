package demo.demo.repository;

import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcProductRepository implements ProductRepository {
    private final DataSource dataSource;

    // 빌더로 상품 생성해서 리턴 (중복 코드 매소드)
    private Product product(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Integer price = rs.getInt("price");
        String category = rs.getString("category");
        String images = rs.getString("images");
        String createDate = rs.getString("createDate");

        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .category(category)
                .stringImageFiles(images)
                .createDate(createDate)
                .build();
    }
    private String connectImageFiles(Product product) {
        StringBuilder imagesFileNames = new StringBuilder();
        if (product.getImageFiles() != null) {
            for (UploadFile imageFile : product.getImageFiles()) {
                imagesFileNames.append(imageFile.getStoreFileName()).append(",");
            }
        }
        return String.valueOf(imagesFileNames);
    }

    public Product sava(Product product) {
        String sql = "insert into Product(name, price, category, images, createDate) values(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 이미지 파일들 (',') 구분자로 구분해서 연결
        String imageFileNames = connectImageFiles(product);
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setString(4, String.valueOf(imageFileNames));
            pstmt.setString(5, String.valueOf(String.valueOf(product.getCreateDate())));
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                long generatedId = rs.getLong(1);
                product.setId(generatedId); // 프라이머리 키(id) 값을 Product 객체에 설정
            }
            return product;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    public Product update(Product product) {
        String sql = "update Product SET name = ?, price = ?, category = ? where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 이미지 파일들 (',') 구분자로 구분해서 연결
        //String imageFileNames = connectImageFiles(product);

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setLong(4, product.getId());
            //pstmt.setString(4, String.valueOf(imageFileNames));

            pstmt.executeUpdate();
            return product;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Transactional
    public Optional<Product> findById(Long id) {
        String sql = "select * from Product where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Product product = product(rs);
                return Optional.of(product);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Product> findByName(String name) {
        return Optional.empty();
    }

    public List<Product> findLimitTwenty() {
        String sql = "select * from Product"
                + " order by id desc"
                + " limit 8";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()) {
                Product product = product(rs);
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public List<Product> findAllByName(String name) {
        String sql = "select * from Product where name like '%name'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()) {
                Product product = product(rs);
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Product> findByFilter(String name) {
        String sql = "select * from Product where name like ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%"+name+"%");
            rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()) {
                Product product = product(rs);
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public List<Product> findByCategory(String category) {
        String sql = "select * from Product where category = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product product = product(rs);
                products.add(product);
            }
            if (products.isEmpty()) {
                return null;
            }
            return products;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public List<Product> findAll() {
        String sql = "select * from Product";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()) {
                Product product = product(rs);
                products.add(product);
            }
            return products;
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