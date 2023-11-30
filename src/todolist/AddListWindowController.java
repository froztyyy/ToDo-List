package todolist;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import todolist.ToDoListUiController;

/**
 * FXML Controller class
 *
 * @author Gwyneth Uy
 */
public class AddListWindowController implements Initializable {

    @FXML
    private TextField txtDescription;
    @FXML
    private TextArea txtDetails;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Button btnSubmit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private ToDoListUiController toDoListUiController;

    public void setToDoListUiController(ToDoListUiController toDoListUiController) {
        this.toDoListUiController = toDoListUiController;
    }

    @FXML
    private void handleButtonSubmit(ActionEvent event) throws IOException {

        try {
            // Establish a database connection
            connect = database.getConnection();

            // Prepare the SQL statement
            String sql = "INSERT INTO task (description, details, due_date) VALUES (?, ?, ?)";
            prepare = connect.prepareStatement(sql);

            // Set values from the user input
            prepare.setString(1, txtDescription.getText());
            prepare.setString(2, txtDetails.getText());
            prepare.setDate(3, java.sql.Date.valueOf(dueDatePicker.getValue())); // Convert LocalDate to java.sql.Date

            // Execute the SQL statement
            prepare.executeUpdate();

            if (toDoListUiController != null) {
                toDoListUiController.homeDisplayListCard();
            } else {
                // Handle the case where the controller is not set
                System.out.println("Error: Controller not set.");
            }

            // Show a success alert
            showSuccessAlert();

        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
            showErrorAlert("Error", "Failed to insert values into the database.");
        } finally {
            // Close resources
            try {
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Values successfully inserted into the database.");
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
