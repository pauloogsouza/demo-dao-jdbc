package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

import java.util.List;
import java.util.Locale;

public class Program {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);
        System.out.println();

        System.out.println("=== TEST 2: seller findAll ===");
        List<Seller> list = sellerDao.findAll();
        list.forEach(System.out::println);
        System.out.println();

        System.out.println("=== TEST 3: seller findByDepartment ===");
        List<Seller> list2 = sellerDao.findByDepartment(2);
        list2.forEach(System.out::println);
    }
}
