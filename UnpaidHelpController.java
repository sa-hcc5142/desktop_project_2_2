package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URI;

public class UnpaidHelpController {

    @FXML
    private Label lbl_h1, lbl_h2, lbl_h3, lbl_h4, lbl_h5;

    public void initializeLabels() {
        // Set up click event for each label
        setUpLabelClick(lbl_h1);
        setUpLabelClick(lbl_h2);
        setUpLabelClick(lbl_h3);
        setUpLabelClick(lbl_h4);
        setUpLabelClick(lbl_h5);
    }

    private void setUpLabelClick(Label label) {
        label.setOnMouseClicked(event -> {
            openWhatsApp();
        });
    }

    private void openWhatsApp() {
        String whatsappUrl = "https://web.whatsapp.com";
        try {
            Desktop.getDesktop().browse(new URI(whatsappUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
