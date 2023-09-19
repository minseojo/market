package demo.demo.repository;

import demo.demo.domain.Product;
import demo.demo.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcProductRepository implements ProductRepository {
    private final DataSource dataSource;
    private final JdbcTemplate template;
    // 빌더로 상품 생성해서 리턴 (중복 코드 매소드)
    private Product createProduct(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Integer price = rs.getInt("price");
        String category = rs.getString("category");
        String images = rs.getString("images");
        String createDate = rs.getString("createDate");
        Long owner_id = rs.getLong("owner_id");
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .category(category)
                .stringImageFiles(images)
                .createDate(createDate)
                .ownerId(owner_id)
                .build();
    }

    @Override
    public Product save(Product product, String imageFileNames) {
        String sql = "insert into Product(name, price, category, images, createDate, owner_id) values(?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setString(3, product.getCategory());
            pstmt.setString(4, String.valueOf(imageFileNames));
            pstmt.setString(5, String.valueOf(String.valueOf(product.getCreateDate())));
            pstmt.setLong(6, product.getOwnerId());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                product.setId(rs.getLong(1));
            }
            return product;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "update Product SET name = ?, price = ?, category = ? where id = ?";
        template.update(sql, product.getName(), product.getPrice(), product.getCategory(), product.getId());
        return true;
    }
    @Override
    public boolean deleteById(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        template.update(sql, productId);
        return true;
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
                Product product = createProduct(rs);
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

    public List<Product> findLimitEight() {
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
                Product product = createProduct(rs);
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
                Product product = createProduct(rs);
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
    public List<Product> findByFilter(String productName) {
        String sql = "select * from Product where name like ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%"+productName+"%");
            rs = pstmt.executeQuery();
            List<Product> products = new ArrayList<>();
            while(rs.next()) {
                Product product = createProduct(rs);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                Product product = createProduct(rs);
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
            while (rs.next()) {
                Product product = createProduct(rs);
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
    private void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeConnection(con);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeResultSet(rs);
    }

}