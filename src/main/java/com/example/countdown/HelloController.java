package com.example.countdown;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {
    @FXML
    private TextField inputSzoveg;
    @FXML
    private Button button;
    @FXML
    private Label label;

    private Timer timer;


    @FXML
    public void indit(ActionEvent actionEvent) {
        String szoveg=inputSzoveg.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            LocalDateTime targetDateTime = LocalDateTime.parse(szoveg, formatter);
            LocalDateTime now = LocalDateTime.now();

            if (targetDateTime.isBefore(now)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Nem lehet régebbi mint most");
                return;
            }

            startCountdown(targetDateTime);

        } catch (DateTimeParseException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "yyyy-MM-dd HH:mm:ss");
        }

    }

    private void startCountdown(LocalDateTime targetDateTime) {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(now, targetDateTime);

                if (duration.isNegative() || duration.isZero()) {
                    timer.cancel();
                    showAlert(Alert.AlertType.INFORMATION, "Letelt", "Letelt!");
                } else {
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    long seconds = duration.getSeconds() % 60;

                    label.setText(String.format("Visszamarad:: %02d:%02d:%02d", hours, minutes, seconds));
                }
            }
        }, 0, 1000);
    }

    private void showAlert(Alert.AlertType alertType, String inputError, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("title");
        alert.setHeaderText(null);
        alert.setContentText("message");
        alert.showAndWait();
    }
}