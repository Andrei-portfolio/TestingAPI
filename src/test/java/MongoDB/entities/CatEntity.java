package MongoDB.entities;

import java.util.Objects;

public class CatEntity {

    private int age;

    private String name;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatEntity catEntity = (CatEntity) o;
        return age == catEntity.age && Objects.equals(name, catEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name);
    }

    @Override
    public String toString() {
        return "CatEntity{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
