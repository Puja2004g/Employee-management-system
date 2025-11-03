package com.example.ems.repository;

import com.example.ems.entity.Employee;
import com.example.ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.time.Year;

public class EmployeeRepository {
    public Employee save(Employee employee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
            return employee;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Employee findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }

    public List<Employee> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Employee", Employee.class).getResultList();
        }
    }

    public List<Employee> findByName(String nameLike) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String pattern = "%" + nameLike.toLowerCase() + "%";
            return session.createQuery(
                    "from Employee e where lower(e.firstName) like :pattern or lower(e.lastName) like :pattern",
                    Employee.class)
                .setParameter("pattern", pattern)
                .getResultList();
        }
    }

    public List<Employee> findByFirstAndLast(String firstName, String lastName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Employee e where lower(e.firstName) = :first and lower(e.lastName) = :last",
                    Employee.class)
                .setParameter("first", firstName.toLowerCase())
                .setParameter("last", lastName.toLowerCase())
                .getResultList();
        }
    }

    public Employee update(Employee employee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(employee);
            tx.commit();
            return employee;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Employee managed = session.get(Employee.class, id);
            if (managed != null) {
                // Close any open employment records for this employee by setting endYear to current year
                int currentYear = Year.now().getValue();
                session.createMutationQuery(
                        "update EmploymentRecord r set r.endYear = :year where r.employee = :emp and (r.endYear is null or r.endYear = 'Present')")
                        .setParameter("year", currentYear)
                        .setParameter("emp", managed)
                        .executeUpdate();

                session.remove(managed);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}


