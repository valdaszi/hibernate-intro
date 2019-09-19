package lt.bit.java2;

import lt.bit.java2.entities.Employee;
import lt.bit.java2.utils.HibernateUtil;

import javax.persistence.EntityManager;

public class MainApp {

    public static void main(String[] args) {
        EntityManager em = HibernateUtil.getEntityManager();

        Employee employee = em.find(Employee.class, 10001);

        System.out.println(employee);
    }
}
