package x_clients.rest_assured;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import x_clients.rest_assured.jpa.entity.CompanyEntity;
import x_clients.rest_assured.jpa.manager.MyPUI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
Для понимания, мы здесь ничего не тестим, мы просто работаем с хебернет. API мы не используем, а раз не используем
то о каких тестах идёт речь. Не о каких. Этот весь файл мы пишем для удобства пишем аннотацию @Test, запустили
и поехали
 */

public class JPADemo {

    private static EntityManager entityManager;// Создадим энтити мэнеджер для работы с энтити. Чтобы не реализовывать
    // огромное количество методов данного энтити менеджера, можно попробывать использовать БИЛДЕР (или патерн
    // entityManagerFactory фабрика, который предназначен для создания объектов - тоже самое что билдер). На каком то из
    // уроков, мы уже использовали БИЛДЕР. Как помним, мы настраиваем этот билдер один раз, а потом можем
    // его использовать неограниченное количество раз в том числе в новом проекте. Просто нужно будет скопировать.
    // Но чтобы создать entityManagerFactory, нужно сделать определённые настройки

    @BeforeAll
        public static void setUp() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "env.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
        /*Четыре строки кода выше, нужны чтобы подключиться к БД, пароль и логины которой приведены в файле env.properties
        ОЧЕНЬ ВАЖНО!!! Пока не пропишем диалект (это фича) в файле env.properties, кейсы в данном классе не запустятся*/

        PersistenceUnitInfo myPUI = new MyPUI(properties);

        // НАСТРОЙКИ.
        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();// Нужен для того, чтобы создать EntityManagerFactory
        EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider.createContainerEntityManagerFactory(myPUI, myPUI.getProperties());// Нужен для того, чтобы создать entityManager
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    public void getCompanyFromDBByName() {
        CompanyEntity company = entityManager.find(CompanyEntity.class, 999);// Важно поставить id той компани, которая есть в БД
        System.out.println(company);
    }

}

    /*@Test
    public void getCompanyFromDBByName() {
        String name = "bars";
        TypedQuery<CompanyEntity> query = entityManager
                .createQuery("SELECT ce FROM CompanyEntity ce WHERE ce.name = :name", CompanyEntity.class);
        query.setParameter("name", name);
        CompanyEntity singleResult = query.getSingleResult();
        System.out.println(singleResult);
    }

    @Test
    public void createNewCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setName("Объект");
        company.setDescription("Описание объекта");
        company.setActive(true);

        entityManager.getTransaction().begin();
        entityManager.persist(company);
        entityManager.getTransaction().commit();

        System.out.println(company);
    }

    @Test
    public void deleteNewCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setName("Объект");
        company.setDescription("Описание объекта");
        company.setActive(true);
        entityManager.getTransaction().begin();
        entityManager.persist(company);
        entityManager.getTransaction().commit();

        int id = company.getId();
        assertNotNull(entityManager.find(CompanyEntity.class, id));

        entityManager.getTransaction().begin();
        entityManager.remove(company);
        entityManager.getTransaction().commit();

        assertNull(entityManager.find(CompanyEntity.class, id));
    }*/

