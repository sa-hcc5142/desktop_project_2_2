package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class DoctorProfilesController {

    @FXML
    private VBox vb1;

    @FXML
    private VBox vb2;

    @FXML
    private VBox vb3;

    @FXML
    private VBox vb4;

    @FXML
    private VBox vb5;

    public void setDoctorProfiles(List<DoctorProfile> profiles) {
        VBox[] vboxes = {vb1, vb2, vb3, vb4, vb5};
        for (int i = 0; i < profiles.size(); i++) {
            DoctorProfile profile = profiles.get(i);
            VBox vbox = vboxes[i];

            Label nameLabel = new Label("Name: " + profile.getName());
            Label specialityLabel = new Label("Speciality: " + profile.getSpeciality());
            Label degreesLabel = new Label("Degrees: " + profile.getDegrees());
            Label aboutLabel = new Label("About: " + profile.getAbout());
            Label contactLabel = new Label("Contact: " + profile.getContactNumber());

            vbox.getChildren().addAll(nameLabel, specialityLabel, degreesLabel, aboutLabel, contactLabel);
        }
    }
}
