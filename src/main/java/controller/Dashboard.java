package controller;

import database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    @FXML
    private ListView<Task> listView;

    private Stage newTaskStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setCellFactory(param -> new ListCell<Task>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setText(item.getTaskDesc());
                    checkBox.setSelected(item.isState());
                    checkBox.setLayoutX(15.0);
                    checkBox.setLayoutY(148.0);
                    checkBox.setMnemonicParsing(false);
                    checkBox.setPrefHeight(46.0);
                    checkBox.setPrefWidth(464.0);
                    checkBox.setStyle("-fx-background-color: #7f8fa6;");
                    checkBox.setFont(new Font(18));

                    checkBox.setOnAction(event -> {
                        item.setState(checkBox.isSelected());
                        try {
                            Connection connection = DBConnection.getInstance().getConnection();
                            String SQL = "UPDATE Task SET state = ? WHERE id = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                            preparedStatement.setBoolean(1, item.isState());
                            preparedStatement.setString(2, item.getId());
                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Task " + item.getId() + " state changed to: " + item.isState());
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    setGraphic(checkBox);
                }
            }
        });
        fetchTasksFromDatabase();
    }

    private void fetchTasksFromDatabase() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String SQL = "SELECT * FROM Task";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String taskDesc = resultSet.getString("taskDesc");
                java.sql.Date date = resultSet.getDate("date");
                boolean state = resultSet.getBoolean("state");

                Task task = new Task(id, taskDesc, date.toLocalDate(), state);
                listView.getItems().add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAddOnAction(ActionEvent actionEvent) {
        if (newTaskStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/new_task.fxml"));
                Parent root = loader.load();
                AddNewTask addNewTaskController = loader.getController();
                addNewTaskController.setDashboardController(this);
                newTaskStage = new Stage();
                newTaskStage.setScene(new Scene(root));
                newTaskStage.setOnCloseRequest(e -> newTaskStage = null);
                newTaskStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            newTaskStage.toFront();
        }
    }
    public void addTask(Task task) {
        listView.getItems().add(task);
    }

}
