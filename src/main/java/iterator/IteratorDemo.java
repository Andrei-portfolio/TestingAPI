package iterator;

import org.instancio.Instancio;

import java.util.List;

/*
Для того, чтобы создать данный класс, необходимо в pom установить instancio-junit (5.0.1). Далее в данном классе
будем использовать паттерн "Иттератор"
 */

public class IteratorDemo {

    public static void main(String[] args) {
        List<Integer> list = getDataFromDb();
        System.out.println(list);
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }

        MyIterator iterator = new MyIterator(list);
            //for (int i = 0; i < list.size(); i++) {
            while (iterator.hasNext()){// говорим, пока следующее значение есть, выводи его. И не будет исключения
        System.out.println(iterator.next());
    }
}

    private static List<Integer> getDataFromDb() {
        // представляем, что это данные из БД
        return Instancio.createList(Integer.class);
    }
}


