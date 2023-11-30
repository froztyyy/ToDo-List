/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package todolist;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author jcarl
 */
public class ArchiveDisplayCardController implements Initializable {

    @FXML
    private Button btnRemove;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblDueDate;
    @FXML
    private TextArea txtDetailsDisplay;
    @FXML
    private Button btnRetrieve;
    @FXML
    private AnchorPane archiveDisplayCard;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private archiveToDoListData archiveTodoData;

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        // Show a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("This action cannot be undone.");

        // Wait for user's response
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, proceed with removal
            // Remove the displayListPane
            archiveDisplayCard.getChildren().clear();

            // Save data to the "archive" table in the database
            deleteFromArchive();
        }
    }
    
    @FXML
    private void handleRetrieveButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to retrieve this item?");
        alert.setContentText("This action will retrieve the selected task to your Todo List");

        // Wait for user's response
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, proceed with removal
            // Remove the displayListPane
            archiveDisplayCard.getChildren().clear();

            // Save data to the "archive" table in the database
            deleteFromArchive();
            insertIntoTask();
        }
    }
    
    private void deleteFromArchive() {
        try (Connection conn = database.getConnection()) {
            if (conn != null) {
                // First, delete the task from the task table
                deleteFromArchiveTable(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
    
    private void insertIntoTask() {
        try (Connection conn = database.getConnection()) {
            if (conn != null) {
                // First, delete the task from the task table
                insertIntoTaskTable(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
   
    private void deleteFromArchiveTable(Connection conn) throws SQLException {
        String deleteQuery = "DELETE FROM archive WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, archiveTodoData.getDescription());
            preparedStatement.executeUpdate();
        }
    }
    
    private void insertIntoTaskTable(Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO task (description, due_date, details) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, archiveTodoData.getDescription());
            preparedStatement.setString(2, archiveTodoData.getDue_date());
            preparedStatement.setString(3, archiveTodoData.getDetails());
            preparedStatement.executeUpdate();
        }
    }
    
    public void setArchiveData(archiveToDoListData archiveToDolistData) throws SQLException {
        this.archiveTodoData = archiveToDolistData;

        lblDescription.setText(archiveTodoData.getDescription());
        lblDueDate.setText(archiveTodoData.getDue_date());
        txtDetailsDisplay.setText(archiveTodoData.getDetails());

    }
}
