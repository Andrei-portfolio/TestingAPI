package x_clients.rest_assured;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import x_clients.rest_assured.jpa.entity.CompanyEntity;
import x_clients.rest_assured.jpa.manager.MyPUI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/*Для понимания, мы здесь ничего не тестим, мы просто работаем с хебернет. API мы не используем, а раз не используем
то о каких тестах идёт речь. Не о каких. Этот весь файл мы пишем для удобства пишем аннотацию @Test, запустили
и поехали. Это как вспомогательный инструмент для работы с тестами, но не сами тесты */

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
        ОЧЕНЬ ВАЖНО!!! Пока не пропишем диалект hibernate.connection..... (это фича) в файле env.properties,
        тесты в данном классе не запустятся, будет ошибка. Кроме того, в файле env.properties необходимо
        прописать параметры show_sq и format_sql, от чего при запуске тестов из БД, будет подробная информация
        в консоли. Если будет в show_sq стоять true, то в консоли будет инфо, если поставить false, то тесты
         также будут отрабатывать, а в консоль выводится не будет инфо*/

        PersistenceUnitInfo myPUI = new MyPUI(properties);

        // НАСТРОЙКИ метода entityManager патерн с помощью entityManagerFactory
        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();// Нужен для того, чтобы создать EntityManagerFactory
        EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider.createContainerEntityManagerFactory(myPUI, myPUI.getProperties());// Нужен для того, чтобы создать entityManager
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    @DisplayName("Получение компании из БД по id")
    public void getCompanyFromDB() {
        CompanyEntity company = entityManager.find(CompanyEntity.class, 1033);// Важно поставить id той компани, которая есть в БД
        System.out.println(company);
    }

    @Test
    @DisplayName("Получение СПИСКА всех компании из БД")
    public void getCompanyFromDBByNames() {
        CompanyEntity company = entityManager.find(CompanyEntity.class, 1033);// Важно поставить id той компани, которая есть в БД
        System.out.println(company);
        TypedQuery<CompanyEntity> query = entityManager.createQuery("SELECT ce FROM CompanyEntity ce", CompanyEntity.class);
        // TypedQuery это дженерик, это когда мы создаём класс, который принимает, тип данных который мы ещё
        // не определили. Когда мы пока его не знаем. Здесь пишем не на SQL, а на похожем QL вместо звёздочки "*",
        // используем "ce". Кроме того, пишем не название таблицы, а название сущности CompanyEntity
        List<CompanyEntity> singleResult = query.getResultList();
        singleResult.forEach(System.out::println);
    }

    @Test
    @DisplayName("Получение конкретной компании из БД по НАЗВАНИЮ")
    public void getCompanyFromDBByName() {
        String name = "Кондитерская Профи-тролли";//Важно поставить НАЗВАНИЕ той компани, которая есть в БД.
        // И выбирать название той компании, которая не по повторяется. Т.е. уникальную. Иначе будет ошибка
        TypedQuery<CompanyEntity> query = entityManager
                .createQuery("SELECT ce FROM CompanyEntity ce WHERE ce.name = :name", CompanyEntity.class);
        // TypedQuery это дженерик, это когда мы создаём класс,который принимает, тип данных который мы ещё
        // не определили. Когда мы пока его не знаем. Здесь пишем не на SQL, а на похожем QL вместо звёздочки "*",
        // используем "ce". Кроме того, пишем не название таблицы, а название сущности. После двоеточия пишем
        // любое название
        query.setParameter("name", name);
        CompanyEntity singleResult = query.getSingleResult();
        System.out.println(singleResult);
    }

    @Test
    @DisplayName("Создание компании в БД")
    public void createNewCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setName("ТКБ");
        company.setDescription("Крутой банк");
        company.setActive(true);

        entityManager.getTransaction().begin();// Обязательно должны выполнить Transaction, иначе объект не сохраниться в БД
        entityManager.persist(company);// persist - добавляет обект в таблицу
        entityManager.getTransaction().commit();// Нужно обозначить, что транзакция закончилась

        System.out.println(company);

        // Чтобы в данном тесте можно было неоднакратно создавать компанию, а не один раз с id=0 (по умолчанию),
        // нужно использовать аннотацию @GeneratedValue в классе CompanyEntity. Спомощью которой происходит
        // автогенерация id. Без данной аннотации, будет создана компания с id=0, а при след. запуске теста
        // программа выдаст ошибку. Так как будет пытаться опять создать компанию с id=0
    }

    @Test
    @DisplayName("Удаление компании из БД")
    public void deleteNewCompany() {
        CompanyEntity company = new CompanyEntity();
        company.setName("ТКБ");
        company.setDescription("Крутой банк");
        company.setActive(true);
        entityManager.getTransaction().begin();
        entityManager.persist(company);
        entityManager.getTransaction().commit();

        int id = company.getId();
        assertNotNull(entityManager.find(CompanyEntity.class, id));
//
        entityManager.getTransaction().begin();
        entityManager.remove(company);
        entityManager.getTransaction().commit();
//
        assertNull(entityManager.find(CompanyEntity.class, id));

    }
}

