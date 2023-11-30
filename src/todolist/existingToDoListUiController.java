/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package todolist;

/**
 *
 * @author jcarl
 */
public class existingToDoListUiController {

    private static ToDoListUiController toDoListUiController;

    public static void setToDoListUiController(ToDoListUiController controller) {
        toDoListUiController = controller;
    }

    public static ToDoListUiController getToDoListUiController() {
        return toDoListUiController;
    }

}
