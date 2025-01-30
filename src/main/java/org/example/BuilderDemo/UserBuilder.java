package org.example.BuilderDemo;

public class UserBuilder {
private  static User user;

public static UserBuilder create() {// назовём метод create
    user = new User(); //создали какого то пустого User (с пустым конструктором)
    return new UserBuilder();// возвращаем экземпляр вот этого класса UserBuilder. Это строитель. Нам сейчас
    // нужен не сам User, а строитель который этого User будет строить
    }

    public User build(){// Если выше был пустой User, то просто его вернём себе. Условно говоря, говорим завершай стройку
        return  user;
    }

/*
Далее проверим, и создадим новый класс UserDemo. После чего, ниже, установим поля нашего класса User (firstName,
lastName, age, city, email, phone). Т.е. достаточно сделать 6 методов, вместо того, чтобы создать конструктор
и получать ошибку из-за количества аргументов. Или сделать один метод, где зададим всё. Или с помощью сеттера,
можно добавить метод, например ФИО. Билдер позволяет творчески подойти к процессу

 */

    public UserBuilder setFirstName (String firstName) {//установи нам FirstName
        user.firstName = firstName;
        return this;// верни текущий объект, т.е. UserBuilder
    }

    public UserBuilder setLastName (String lastName) {//По аналогии делаем и lastName, говорим установи нам lastName
        user.lastName = lastName;
        return this;// верни текущий объект, т.е. UserBuilder
    }

    public UserBuilder setAge (int age) {//По аналогии делаем и age, говорим установи нам age
        user.age = age;
        return this;// верни текущий объект, т.е. UserBuilder
    }

    public UserBuilder setCity (String city) {//По аналогии делаем и city, говорим установи нам city
        user.city = city;
        return this;// верни текущий объект, т.е. UserBuilder
    }

    public UserBuilder setEmail (String email) {//По аналогии делаем и email, говорим установи нам email
        user.email = email;
        return this;// верни текущий объект, т.е. UserBuilder
    }

    public UserBuilder setPhone (String phone) {//По аналогии делаем и email, говорим установи нам email
        user.phone = phone;
        return this;// верни текущий объект, т.е. UserBuilder
    }
}
