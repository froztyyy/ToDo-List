package todolist;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import todolist.ToDoListUiController;

public final class DisplayListController implements Initializable {

    @FXML
    private Label lblDescription;
    @FXML
    private Label lblDueDate;
    @FXML
    private TextArea txtDetailsDisplay;
    @FXML
    private Button btnRemove;
    @FXML
    private AnchorPane toDoDisplayCard;
    @FXML
    private Button btnDisplayCard;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Call the method to load data when the controller is initialized

    }

    private toDoListData todoData;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void setData(toDoListData todoData) throws SQLException {
        this.todoData = todoData;

        lblDescription.setText(todoData.getDescription());
        lblDueDate.setText(todoData.getDue_date());
        txtDetailsDisplay.setText(todoData.getDetails());

    }

    @FXML
    private void handleRemoveButton(ActionEvent event) {
        // Show a confirmation alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to remove this item?");
        alert.setContentText("This will move the task to archive.");

        // Wait for user's response
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, proceed with removal
            // Remove the displayListPane
            toDoDisplayCard.getChildren().clear();

            // Save data to the "archive" table in the database
            saveToArchive();
        }
    }

    private void saveToArchive() {
        try (Connection conn = database.getConnection()) {
            if (conn != null) {
                // First, delete the task from the task table
                deleteFromTaskTable(conn);

                // Now, insert the task into the archive table
                insertIntoArchiveTable(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void deleteFromTaskTable(Connection conn) throws SQLException {
        String deleteQuery = "DELETE FROM task WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, todoData.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    private void insertIntoArchiveTable(Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO archive (description, due_date, details) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, todoData.getDescription());
            preparedStatement.setString(2, todoData.getDue_date());
            preparedStatement.setString(3, todoData.getDetails());
            preparedStatement.executeUpdate();
        }
    }

    @FXML
    private void handleDisplayCardButton(ActionEvent event) throws IOException {
         // Load the FXML for the addListWindow
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MaximizeDisplayListCard.fxml"));
        Parent root = loader.load();

        // Create a new stage for the addListWindow
        Stage displayCard = new Stage();
        displayCard.initModality(Modality.WINDOW_MODAL);
        displayCard.initOwner(btnDisplayCard.getScene().getWindow());

        // Set the scene
        Scene scene = new Scene(root);
        displayCard.setScene(scene);

        // Set the stage title
        displayCard.setTitle("Add List");
        displayCard.setResizable(false);

        MaximizeDisplayListCardController maximizeDisplayListCardController = loader.getController();
        maximizeDisplayListCardController.setDisplayListController(this);

        // Show the addListWindow
        displayCard.show();
    }
}
