package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Task;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddNewTask {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private DatePicker date;

    @FXML
    private JFXTextField lblId;

    @FXML
    private JFXTextField lblTaskDesc;
    private Dashboard dashboardController;

    public void setDashboardController(Dashboard dashboardController) {
        this.dashboardController = dashboardController;
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Task task = new Task(lblId.getText(),lblTaskDesc.getText(),date.getValue(),false);
        try {
            String SQL ="INSERT INTO Task VALUES(?,?,?,?)";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQL);
            psTm.setObject(1,task.getId());
            psTm.setObject(2,task.getTaskDesc());
            psTm.setDate  (3, Date.valueOf(task.getDate()));
            psTm.setObject(4,task.isState());

            boolean b = psTm.executeUpdate()>0;
            System.out.println(b);

            if(b){
                new Alert(Alert.AlertType.INFORMATION, "Task Added!").show();
                dashboardController.addTask(task);

                Stage stage = (Stage) btnAdd.getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(task);
        lblId.setText("");
        lblTaskDesc.setText("");
    }
}
