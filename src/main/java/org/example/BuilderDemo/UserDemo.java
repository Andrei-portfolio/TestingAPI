package org.example.BuilderDemo;

public class UserDemo {

    public static void main(String[] args) {
//        User user = UserBuilder.create().build();
//        System.out.println(user);
        /*получаем в консоль User{firstName='null', LastName='null', age=0, city='null', email='null', phone='null'}
        Пока мы получаем пустого user, и мы ещё не решили нашу задачу, чтобы давать возможность создавать
        пользователя у которого могут быть заданы, как все 6 полей, так и не задано ни одного поля и т.д.
        Задача решается очень просто, мы просто делаем СЕТТЕРЫ в UserBuilder для полей нашего класса User (firstName,
        lastName, age, city, email, phone.

        После чего, можем записать в коде */

        User user = UserBuilder.create().setFirstName("Иван").build();
        System.out.println(user);
/*
Теперь мы можем без проблем использовать и создать, как с Именем, "Иван", так и без имени (код ниже)
 */
        user = UserBuilder.create().build();
        System.out.println(user);

        /*
        Кроме того, можем через точку прописать несколько аргументов у нашего user, например сколько лет, ФИО и т.д.
        Просто надо перечислять через точку setCity("").setEmail("")
        Можем их менять местами и вызывать в любом порядке
         */

        user = UserBuilder.create().setAge(15).setLastName("Иванов").setPhone("9277047244").build();
        System.out.println(user);

/* В терминале получаем User{firstName='null', LastName='Иванов', age=15, city='null', email='null', phone='9277047244'}

 */


    }
}
