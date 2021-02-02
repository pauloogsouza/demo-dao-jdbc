package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class Program2 {

    public static void main(String[] args) {

        System.out.println("=== TEST 1 - findById ===");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department dep = departmentDao.findById(2);
        System.out.println(dep);
        System.out.println();

        System.out.println("=== TEST 2 - findAll ===");
        List<Department> list = departmentDao.findAll();
        list.forEach(System.out::println);
    }
}
