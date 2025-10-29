package com.example.ems.repository;

import com.example.ems.entity.Employee;
import com.example.ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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
                session.remove(managed);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}


