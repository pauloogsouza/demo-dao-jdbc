package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.Locale;

public class Program {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Department obj = new Department(1, "Books");
        Seller sl = new Seller(1, "Joao", "joao@gmail.com", new Date(), 2000.0, obj);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println(sl);

    }
}
