package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTest {

    protected static final SessionFactory sessionFactory;
    protected Session session;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            System.out.println("Hibernate SessionFactory создан.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации Hibernate!", e);
        }
    }

    @BeforeEach
    public void setUp() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            throw new IllegalStateException("SessionFactory уже закрыта!");
        }

        session = sessionFactory.openSession();
        session.beginTransaction();
        System.out.println("Сессия Hibernate открыта.");

        try {
            session.createQuery("DELETE FROM Delivery").executeUpdate();
            if (session.getTransaction().isActive()) {
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        } finally {
            session.beginTransaction();
        }
    }


    @AfterEach
    public void tearDown() {
        if (session != null) {
            try {
                if (session.getTransaction().isActive()) {
                    session.getTransaction().commit();
                }
            } catch (Exception e) {
                System.err.println("Ошибка при коммите: " + e.getMessage());
                if (session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
            } finally {
                session.close();
                System.out.println("Сессия Hibernate закрыта.");
            }
        }
    }
}

