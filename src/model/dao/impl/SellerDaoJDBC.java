package model.dao.impl;

import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

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
                return instantiateSeller(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    private Seller instantiateSeller(ResultSet set) throws SQLException {
        Seller seller = new Seller();
        seller.setId(set.getInt("Id"));
        seller.setName(set.getString("Name"));
        seller.setEmail(set.getString("Email"));
        seller.setBirthDate(set.getDate("BirthDate"));
        seller.setBaseSalary(set.getDouble("BaseSalary"));

        Department dep = new Department();
        dep.setId(set.getInt("DepartmentId"));
        dep.setName(set.getString("DepName"));

        seller.setDepartment(dep);
        return seller;
    }
}
