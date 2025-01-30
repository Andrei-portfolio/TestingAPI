package tests;

import entities.Task;
import helpers.ToDoHelperApache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 /* Бизнес тесты - это тесты, где проверяется бизнес логика, например если у нас 3 элемента, то вернуть
 должен 3 элемента*/

public class ToDoBusinessTests {
    private ToDoHelperApache toDoHelperApache;//Объявили ToDoHelper, отдельно вынесенный класс в котором у нас создаётся задача

    @BeforeEach
    public void setUp(){
        toDoHelperApache = new ToDoHelperApache();
    }

    @Test
    @DisplayName("Создание объекта")
    @Disabled("Тест падает из-за бага задача jira.com/BUG-12523532")// Если закомитить, то тест можно будет запустить
    public void createTask() throws IOException {
        Task myTask = toDoHelperApache.createTask();

            List<Task> tasks = toDoHelperApache.getTasks();
        for (Task task : tasks) {
            if (task.getId() == myTask.getId())
            {
                assertNotNull(task);
                assertFalse(task.getCompleted());//Если закомитить данную строку, то тест пройдёт, т.к. мы у boolian
                                                // ожидаем, задача выполнена (тру) или нет (фалсе), а в to do
                                                // при создании задачи, она изначально создаётся со статусом null.
                                                // Это БАГ
            }
        }
    }

    @Test
    @DisplayName("Получение списка задач из 3 элементов")
    public void getTasksBodyValidTypes() throws IOException {
        // удалить все элементы

    /* чтобы получить 3 элемента, нам сначала надо всё удалить, ну а потом создать именно 3. Т.к. преподаватель
    не помнит как удалить все задачи в списке to do, и есть ли такой метод не известно. Представим условно,
    что в данном шаге мы удалили все элементы. Следующим шагом, мы создадим их*/

        // создать три элемента
        toDoHelperApache.createTask();
        toDoHelperApache.createTask();
        toDoHelperApache.createTask();

        /* Откуда же нам брать createTask? Данны метод createTask, который создаёт задачи, у нас уже есть,
        но есть в другом классе ToDoContractTests. Так как же быть? Можно его скопировать и перенести сюда.
        Но мы же помним главный принцип, что от одинакового кода нужно избавляться. Поэтому перенесём
        метод createTask в отдельный класс в папке helpers*/

        List<Task> tasks = toDoHelperApache.getTasks();
        assertEquals(3, tasks.size());// ПРОВЕРКА, ЧТО в СПИСКЕ не менее 3-х задач

        System.out.println(tasks);
    }
        /*ВАЖНО!!!!!!! Перед запуском данного теста, в To-Do лучше удалить все задачи*/

/*
    @Test
    @DisplayName("Удаление задачи")
    public void deleteTask() throws IOException {
        Task myTask = toDoHelper.createTask();
        toDoHelper.deleteTask(myTask);

        List<Task> tasks = toDoHelper.getTasks();
        assertThat(tasks).doesNotContain(myTask);*/

}
