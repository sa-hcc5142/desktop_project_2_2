package com.example.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.awt.*;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/login/sample.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        stage.show();

        stage.setX((dimension.width / 2) - (stage.getWidth() / 2));
        stage.setY((dimension.height / 2) - (stage.getHeight() / 2));

    }
    public static void main(String[] args) {
        launch();
    }
}