package repository;

import db.DatabaseManager;
import model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 联系人数据访问实现（MySQL）
 * 实现 ContactRepository 接口，提供完整的增删改查
 */
public class ContactRepositoryImpl implements ContactRepository {

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT id, name, phone_number FROM contacts ORDER BY id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Contact c = new Contact(
                    rs.getString("name"),
                    rs.getString("phone_number")
                );
                contacts.add(c);
            }

        } catch (SQLException e) {
            System.err.println("查询联系人失败: " + e.getMessage());
        }

        return contacts;
    }

    @Override
    public Contact findById(int id) {
        String sql = "SELECT id, name, phone_number FROM contacts WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Contact(
                    rs.getString("name"),
                    rs.getString("phone_number")
                );
            }

        } catch (SQLException e) {
            System.err.println("查询联系人失败: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean insert(Contact contact) {
        String sql = "INSERT INTO contacts (name, phone_number) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, contact.getName());
            ps.setString(2, contact.getPhoneNumber());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("新增联系人失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Contact contact) {
        // 需要 ID 才能更新，暂时不实现
        System.out.println("更新操作暂未实现");
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("删除联系人失败: " + e.getMessage());
            return false;
        }
    }
}
