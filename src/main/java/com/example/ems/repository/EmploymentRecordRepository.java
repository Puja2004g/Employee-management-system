package com.example.ems.repository;

import com.example.ems.entity.EmploymentRecord;
import com.example.ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EmploymentRecordRepository {
    public EmploymentRecord save(EmploymentRecord record) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(record);
            tx.commit();
            return record;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}


