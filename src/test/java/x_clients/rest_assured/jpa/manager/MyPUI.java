package x_clients.rest_assured.jpa.manager;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hibernate.jpa.HibernatePersistenceProvider;
import x_clients.rest_assured.jpa.entity.CompanyEntity;

import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class MyPUI implements PersistenceUnitInfo {//мы создали класс, который расширяет данный интерфейс

    private final Properties properties;

    public MyPUI(Properties properties){//Конструктор
        this.properties = properties;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public List<String> getManagedClassNames() {// Этот метод нам пригодится и только он будет отличаться в зависимости от БД, остальное под копирку
        return List.of(
                CompanyEntity.class.getName()// Здесь объясняем java с какими таблицами БД будем работать. Если табл. несколько
                // через запятую таким же способом перечисляем остальные таблицы
        );
    }

    @Override
    public String getPersistenceUnitName() {// Этот метод нам тоже пригодится, пропишем в return
        return "MySuperTest";
    }

    @Override
    public String getPersistenceProviderClassName() {// Этот метод нам тоже пригодится, пропишем в return
        return HibernatePersistenceProvider.class.getName();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return null;
    }

    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return null;
    }

    @Override
    public List<String> getMappingFileNames() {
        return List.of();
    }

    @Override
    public List<URL> getJarFileUrls() {
        return List.of();
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return null;
    }

    @Override
    public ValidationMode getValidationMode() {
        return null;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "";
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void addTransformer(ClassTransformer classTransformer) {

    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}
