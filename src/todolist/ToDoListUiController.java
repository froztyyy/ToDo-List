/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package todolist;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gwyneth Uy
 */
public class ToDoListUiController implements Initializable {

    private static ToDoListUiController toDoListUiController;

    @FXML
    private Button btnAddList;
    @FXML
    private GridPane listHandler;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnArchive;
    @FXML
    private Pane homePane;
    @FXML
    private GridPane archiveListHandler;
    @FXML
    private VBox sideNavigation;
    @FXML
    private Pane archivePane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        homeDisplayListCard();
        archiveDisplayListCard();
        sideNavigation.setVisible(true);
        homePane.setVisible(true);
        archivePane.setVisible(false);
    }

    private void setButtonColor(Button button, boolean isSelected) {
        if (isSelected) {
            button.getStyleClass().add("selected-button");
        } else {
            button.getStyleClass().remove("selected-button");
        }
    }

    private Button lastClickedButton = null;

    @FXML
    public void SwitchForm(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton == lastClickedButton) {
            // Ignore the click if the same button was clicked twice in a row
            return;
        }

        // Reset the color of the last clicked button
        if (lastClickedButton != null) {
            setButtonColor(lastClickedButton, false);
        }

        // Update the last clicked button
        lastClickedButton = clickedButton;

        if (clickedButton == btnHome) {
            setButtonColor(btnHome, true);
            setButtonColor(btnArchive, false);

            sideNavigation.setVisible(true);
            homePane.setVisible(true);
            archivePane.setVisible(false);

        } else if (clickedButton == btnArchive) {
            setButtonColor(btnHome, false);
            setButtonColor(btnArchive, true);

            sideNavigation.setVisible(true);
            homePane.setVisible(false);
            archivePane.setVisible(true);

        }
    }

    @FXML
    private void handleButtonAddList(ActionEvent event) throws IOException {
        // Load the FXML for the addListWindow
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addListWindow.fxml"));
        Parent root = loader.load();

        // Create a new stage for the addListWindow
        Stage addListStage = new Stage();
        addListStage.initModality(Modality.WINDOW_MODAL);
        addListStage.initOwner(btnAddList.getScene().getWindow());

        // Set the scene
        Scene scene = new Scene(root);
        addListStage.setScene(scene);

        // Set the stage title
        addListStage.setTitle("Add List");
        addListStage.setResizable(false);

        AddListWindowController addListWindowController = loader.getController();
        addListWindowController.setToDoListUiController(this);

        // Show the addListWindow
        addListStage.show();
    }

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private int currentDisplayIndex = 0;

    private ObservableList<toDoListData> toDoList = FXCollections.observableArrayList();

    public ObservableList<toDoListData> getToDoListData() throws SQLException {

        String sql = "Select description, details, due_date FROM task";
        ObservableList<toDoListData> toDoList = FXCollections.observableArrayList();
        connect = database.getConnection();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String description = result.getString("description");
                String details = result.getString("details");
                String due_date = result.getString("due_date");

                toDoListData todoListData = new toDoListData(description, details, due_date);

                toDoList.add(todoListData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (result, prepare, connect) if needed
        }

        return toDoList;
    }

    public void homeDisplayListCard() {
        try {
            toDoList.clear();
            toDoList.addAll(getToDoListData());

            int maxColumns = 3;
            int row = 0;
            int column = 0;

            listHandler.getChildren().clear();
            listHandler.getRowConstraints().clear();
            listHandler.getColumnConstraints().clear();

            for (int q = 0; q < toDoList.size(); q++) {
                try {
                    if (column >= maxColumns) {
                        // Move to the next row when the maximum number of columns is reached
                        column = 0;
                        row++;
                    }

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("displayList.fxml"));
                    AnchorPane pane = loader.load();
                    DisplayListController cardController = loader.getController();
                    cardController.setData(toDoList.get(q));

                    listHandler.add(pane, column++, row);

                    GridPane.setMargin(pane, new Insets(5));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            currentDisplayIndex = 0;
            if (!toDoList.isEmpty()) {
                toDoListData firstToDo = toDoList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<archiveToDoListData> archiveToDoList = FXCollections.observableArrayList();

    public ObservableList<archiveToDoListData> getArchiveToDoListData() throws SQLException {

        String sql = "Select description, details, due_date FROM archive";
        ObservableList<archiveToDoListData> archiveToDoList = FXCollections.observableArrayList();
        connect = database.getConnection();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String description = result.getString("description");
                String details = result.getString("details");
                String due_date = result.getString("due_date");

                archiveToDoListData archiveToDoListData = new archiveToDoListData(description, details, due_date);

                archiveToDoList.add(archiveToDoListData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources (result, prepare, connect) if needed
        }

        return archiveToDoList;
    }

    public void archiveDisplayListCard() {
        try {
            archiveToDoList.clear();
            archiveToDoList.addAll(getArchiveToDoListData());

            int maxColumns = 3;
            int row = 0;
            int column = 0;

            archiveListHandler.getChildren().clear();
            archiveListHandler.getRowConstraints().clear();
            archiveListHandler.getColumnConstraints().clear();

            for (int q = 0; q < archiveToDoList.size(); q++) {
                try {
                    if (column >= maxColumns) {
                        // Move to the next row when the maximum number of columns is reached
                        column = 0;
                        row++;
                    }

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("archiveDisplayCard.fxml"));
                    AnchorPane pane = loader.load();
                    ArchiveDisplayCardController archiveCardController= loader.getController();
                    archiveCardController.setArchiveData(archiveToDoList.get(q));

                    archiveListHandler.add(pane, column++, row);

                    GridPane.setMargin(pane, new Insets(5));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            currentDisplayIndex = 0;
            if (!archiveToDoList.isEmpty()) {
                archiveToDoListData firstToDo = archiveToDoList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
