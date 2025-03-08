package x_clients.rest_assured.jpa.entity;

/*Также в автотестах рассмотрим второй способ работы с БД, но для этого необходимо в pom установить библиотеку хибернет
org.hibernate (6.4.4.Final). Это ORM (object-relational mapper или объектно-реляционное отображение таблички из БД
        ввиде класса объекта модели POJO). Это тот же самый entity. Позволит один раз взять таблицу из БД, написать её поля,
основывающиеся на этой таблице и использовать её в java коде. Чтобы дать понять java, что таблица это сущьность, что
класс создан на основе таблицы из БД, для этого в классе применим аннотацию @Entity из хибернета. Далее, необходимо сказать,
из какой таблицы мы берём значения. Для этого используем аннотацию @Table. Чтобы связать каждое поле нашего класса,
и сказать, что это колонка нашей таблицы, нужно поставить перед каждым полем в данном классе аннотацию @Column
(Название колонок БД и полей должны совпадать, и тогда на самом деле анотацию @Column можно не писать, но для наглядности
напишем). Далее с помощью хебернет написать запрос на получение компании из таблицы. Чтобы нужная нам компания
распарсилась, смапилась. И у нас получился объект данного класса. А это будем делать уже в классе JPADemo
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "company", schema = "public", catalog = "x_clients_db_heq8")// Сказали в какой таблице, в какой схеме и каталоге искать эти данные

public class CompanyEntity {

    @Id // нужно указать данную аннотацию, иначе не будут отрабатывать тесты в классе JPADemo
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)// С помощью данной аннотации происходит автогенерация id,
    // в тесте "Создание компании". Без данной аннотации, будет создана компания с id=0, а при след. запуске теста
    // программа выдаст ошибку
    private int id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "is_active")//
    private boolean isActive;// Мы прекрасно помним, что в java пишем в стиле Camel case. Но что нам делать, если
    // в БД название колонки написано is_active. А для этого, как раз в анотации  @Column в скобках напишем,
    // как на самом деле называется колонка, а ниже напишем как раз как нам нужно в стиле Camel case

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity company = (CompanyEntity) o;
        return id == company.id && isActive == company.isActive && Objects.equals(name, company.name) && Objects.equals(description, company.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isActive);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
