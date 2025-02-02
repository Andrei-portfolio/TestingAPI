package tests;

import entities.Task;
import helpers.ToDoHelper;
import helpers.ToDoHelperApache;
import helpers.ToDoHelperOkHttp;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

 /* Бизнес тесты - это тесты, где проверяется бизнес логика, например если у нас 3 элемента, то вернуть
 должен 3 элемента*/

public class ToDoBusinessTests {

    private ToDoHelper toDoHelper;//Объявили ToDoHelper, отдельно вынесенный класс в котором у
    // нас создаётся задача

    @BeforeEach
    public void setUp() {
        toDoHelper = new ToDoHelperOkHttp();//ОЧЕНЬ ВАЖНО!!! ЗДЕСЬ МЫ МОЖЕМ СМЕНИТЬ ToDoHelperOkHttp() НА
        // ToDoHelperApache И У НАС ВСЕ ТЕСТЫ ТАКЖЕ ОТРАБОТАЮТ.
    } /* В ЧЕМ ПЛЮСЫ ДАННОГО МЕТОДА И СОЗДАННОГО НАМИ КЛАССА toDoHelperApache? А В ТОМ, ЧТО ЕСЛИ УСЛОВНО ГОВОРЯ
    НАМ ЗАБЛОКИРУЮТ В РОССИИ ИНСТРУМЕНТ Apache, ТО МЫ ЛЕГКО СМОЖЕТ ПЕРЕЙТИ НА ДРУГОЙ ИНСРУМЕНТ (клиент), НАПРИМЕР
    OkHttp. ТО НАМ НУЖНО БУДЕТ ВСЕГО НАВСЕГО В ДАННОЙ СТРОКЕ СМЕНИТЬ ВСЕГО ОДНО СЛОВО ToDoHelperApache НА
    ToDoHelperOkHttp. И ТЕСТЫ ПРОДОЛЖАТ РАБОТАТЬ*/

    @Test
    @DisplayName("Создание объекта")
    @Disabled("Тест падает из-за бага задача jira.com/BUG-12523532")// Если закомитить, то тест можно будет запустить
    public void createTask() throws IOException {
        Task myTask = toDoHelper.createTask();

        List<Task> tasks = toDoHelper.getTasks();
        for (Task task : tasks) {
            if (task.getId() == myTask.getId()) {
                assertNotNull(task);
                assertFalse(task.getCompleted());//Если закомитить данную строку, то тест пройдёт, т.к. мы у boolian
                // ожидаем, задача выполнена (тру) или нет (фалсе), а в to do
                // при создании задачи, она изначально создаётся со статусом null.
                // Это БАГ
            }
        }
    }

    @Test
    @DisplayName("Получение списка задач из 3 элементов")//*ВАЖНО!!!!!!! Перед запуском данного теста, в To-Do
                                                        // лучше удалить все задачи*/
    public void getTasksBodyValidTypes() throws IOException {
        // удалить все элементы

    /* чтобы получить 3 элемента, нам сначала надо всё удалить, ну а потом создать именно 3. Т.к. преподаватель
    не помнит как удалить все задачи в списке to do, и есть ли такой метод не известно. Представим условно,
    что в данном шаге мы удалили все элементы. Следующим шагом, мы создадим их*/

        // создать три элемента
        toDoHelper.createTask();
        toDoHelper.createTask();
        toDoHelper.createTask();

        /* Откуда же нам брать createTask? Данны метод createTask, который создаёт задачи, у нас уже есть,
        но есть в другом классе ToDoContractTests. Так как же быть? Можно его скопировать и перенести сюда.
        Но мы же помним главный принцип, что от одинакового кода нужно избавляться. Поэтому перенесём
        метод createTask в отдельный класс в папке helpers*/

        List<Task> tasks = toDoHelper.getTasks();
        assertEquals(3, tasks.size());// ПРОВЕРКА, ЧТО в СПИСКЕ не менее 3-х задач

        System.out.println(tasks);
    }
    /*ВАЖНО!!!!!!! Перед запуском данного теста, в To-Do лучше удалить все задачи*/

    @Test
    @DisplayName("Удаление задачи")
    public void deleteTask() throws IOException {
        Task myTask = toDoHelper.createTask();// Чтобы удалить задачу, её сначала нужно создать
        toDoHelper.deleteTask(myTask);// По началу deleteTas не опрделялось, т.к. мы забыли его добавить в ToDoHelper.
        // В скобках передаём таску для удаления

        List<Task> tasks = toDoHelper.getTasks();//проверяем, что у нас нет таски с id myTask
        assertThat(tasks).doesNotContain(myTask);// assertThat из библиотеки assertj-core, которую добавили в pom.xml
    }
}
