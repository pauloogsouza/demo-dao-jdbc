package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
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
        System.out.println();

        System.out.println("=== TEST 4: seller insert ===");
        Seller seller1 = new Seller(null, "Felipe", "felipe@gmail.com", new Date(), 2000.0, new Department(1, null));
        sellerDao.insert(seller1);
        System.out.println("Inserted! New ID = " + seller1.getId());

        System.out.println("=== TEST 5: seller update ===");
        seller = sellerDao.findById(1);
        seller.setName("Khal Droggo");
        sellerDao.update(seller);

        System.out.println("=== TEST 6: seller delete ===");
        sellerDao.deleteById(14);
    }
}
