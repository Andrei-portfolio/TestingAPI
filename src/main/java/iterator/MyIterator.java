package iterator;

import java.util.Collection;

public class MyIterator {// У итератора должно быть 2 метода и конструктор

    private Collection<Integer> data;

    private int index;

    public MyIterator(Collection<Integer> data){
        this.data = data;
        this.index = -1;// индекс -1 означает, что мы можем передать сюда даже пустой список
    }

    public Integer next(){//Метод next увеличевает число на еденицу, index становится равен нулю
        index++;
        return (Integer)data.toArray()[index];// преобразуй коллекцию в массив и подставь индекс в нулевое значение,
                                              // т.е. выбери нам нулевой элемент
    }

    public boolean hasNext(){
        try {
            Integer i = (Integer) data.toArray()[index + 1];
            return true;
        }
        catch (IndexOutOfBoundsException exception)
        {
            return false;
        }
    }
}