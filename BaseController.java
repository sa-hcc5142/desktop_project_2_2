package com.example.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.awt.*;
import java.io.IOException;

public abstract class BaseController {
        protected FeelingModel model;
    protected String currentUserEmail;

    public BaseController() {
        this.model = FeelingModel.getInstance();
        this.currentUserEmail = SharedModel.getInstance().getCurrentUserEmail();
        System.out.println("Email accessed in BaseController: " + this.currentUserEmail);
    }

    public void redirectTo(String fxmlFileName, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();


            Object controller = loader.getController();

            // Conditional logic for controller-specific initialization
            if (controller instanceof BaseController) {
                ((BaseController) controller).setup();
            } else if (controller instanceof WhatKindOfHelpDoYouNeed) {
                ((WhatKindOfHelpDoYouNeed) controller).initializeCustomLogic();
            }
            else if (controller instanceof ChatPage) {
                ((ChatPage) controller).setCurrentUserEmail(currentUserEmail);
            }

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            stage.show();

            stage.setX((dimension.width / 2) - (stage.getWidth() / 2));
            stage.setY((dimension.height / 2) - (stage.getHeight() / 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Setup method for BaseController-specific behavior
    public void setup() {
        // Shared setup logic for all BaseControllers (optional)
    }



}
