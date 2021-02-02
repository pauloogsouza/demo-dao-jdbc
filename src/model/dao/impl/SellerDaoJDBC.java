package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement ps = null;
        try {
            insertUpdate();

            ps = conn.prepareStatement("INSERT INTO seller(Name, Email, BirthDate, BaseSalary, DepartmentId)" +
                    " VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DBException("Unexpected Error! No rows were affected!");
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("UPDATE seller" +
                    " SET Name= ?, Email= ?, BirthDate= ?, BaseSalary= ?, DepartmentId= ?" +
                    " WHERE seller.Id= ?");

            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());
            ps.setInt(6, obj.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Updated ID: " + obj.getId() + "!");
            } else {
                throw new DBException("Invalid ID! No rows were Affected!");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM seller WHERE seller.Id = ?");

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deleted ID: " + id + "!");
            } else {
                throw new DBException("Invalid ID! No rows were Affected!");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(
                        "select seller.*, department.Name as DepName" +
                            " FROM seller INNER JOIN department" +
                            " ON seller.DepartmentId = department.Id" +
                            " WHERE seller.Id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return instantiateSeller(rs, dep);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(
                    "select seller.*, department.Name as DepName" +
                            " FROM seller INNER JOIN department" +
                            " ON seller.DepartmentId = department.Id" +
                            " ORDER BY seller.Id");

            rs = ps.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
                return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    @Override
    public List<Seller> findByDepartment(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement
                    ("SELECT seller.*, department.Name AS DepName"
                            + " FROM seller INNER JOIN department"
                            + " ON seller.DepartmentId = department.Id"
                            + " WHERE seller.DepartmentId = ?"
                            + " ORDER BY seller.Id");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(ps);
        }
    }

    private Department instantiateDepartment(ResultSet set) throws SQLException {
        Department dep = new Department();
        dep.setId(set.getInt("DepartmentId"));
        dep.setName(set.getString("DepName"));
        return dep;
    }

    private Seller instantiateSeller(ResultSet set, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(set.getInt("Id"));
        seller.setName(set.getString("Name"));
        seller.setEmail(set.getString("Email"));
        seller.setBirthDate(set.getDate("BirthDate"));
        seller.setBaseSalary(set.getDouble("BaseSalary"));
        seller.setDepartment(dep);
        return seller;
    }

    private void insertUpdate() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT MAX(Id) FROM seller");

        ResultSet rs = ps.executeQuery();

        int value = 0;
        if (rs.next()) {
            value = rs.getInt(1);
        }

        ps = conn.prepareStatement("ALTER TABLE seller AUTO_INCREMENT = ?");

        ps.setInt(1, value + 1);

        ps.executeUpdate();
    }
}
