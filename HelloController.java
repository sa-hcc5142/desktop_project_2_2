package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import javafx.scene.Parent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.Node;

public class HelloController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private TextField tfKuet_email;

    @FXML
    private PasswordField pfPasswordHidden;

    @FXML
    private TextField pfPasswordText;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblForgotPassword;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnSignUp;

    @FXML
    private Label errorMessageLabelLogIn;

    @FXML
    private Label errorMessageLabelSiguUp;

    @FXML
    private Button btnCreateAccount;

    @FXML
    private Button btnUploadPicture;

    @FXML
    private PasswordField pfConfirmPassword;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private CheckBox chkShowPassword;

    @FXML
    private CheckBox chkRememberPass;

    @FXML
    private Button btnSubmitNewPass;

    @FXML
    private Label errorMessageLabelChngPass;

    @FXML
    private Label lblWelcomeMessage;

    @FXML
    private Button btnLoginWhenAlreadyRegistered;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/mentalhealth";
    private static final String DB_USERNAME= "root";  // Replace with your MySQL username
    private static final String DB_PASSWORD = "1234"; // Replace with your


    private String errorMessage = "";
    private String errorPassMessage = "";
    private String errorEmailMessage = "";
    private String errorFirstNameMessage = "";
    private String errorLastNameMessage = "";
    private String errorConfirmPasswordMessage = "";

    public void setAutofillCredentials(String email, String password) {
        tfKuet_email.setText(email);
        pfPasswordHidden.setText(password);
    }


    private int isLoginFieldAlright() {
        int isPassFilled = 1;
        int isEmailFilled = 1;
        int isPassValid = 1;
        int isEmailValid = 1;

        // Check if email is empty
        if (tfKuet_email.getText().isEmpty()) {
            isEmailFilled = 0;
            errorEmailMessage = "kuet email address is empty!";
        }
        // Check if email contains "@stud.kuet.ac.bd"
        else if (!tfKuet_email.getText().contains("@stud.kuet.ac.bd")) {
            isEmailValid = 0;
            errorEmailMessage = "Invalid email address!";
        } else {
            isEmailValid = 1;
            errorEmailMessage = "";
        }

        // Check if password is empty
        if (pfPasswordHidden.getText().isEmpty()) {
            isPassFilled = 0;
            errorPassMessage = "Password is empty!";
        }
        // Check if password length is at least 8 characters
        else if (pfPasswordHidden.getText().length() < 8) {
            isPassValid = 0;
            errorPassMessage = "Password must contain least 8 characters!";
        }
        // Check if password contains "# and atleast of 8 characters"
        else if ((!pfPasswordHidden.getText().contains("#")) && pfPasswordHidden.getText().length() >= 8) {
            isPassValid = 0;
            errorPassMessage = "Password must contain a '#'!";
        } else {
            isPassValid = 1;
            errorPassMessage = "";
        }

        // Set error message
        errorMessage = errorEmailMessage + "\n" + errorPassMessage;

        return isPassFilled * isEmailFilled * isPassValid * isEmailValid;

    }

    private int checkUserExists(String email) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT COUNT(*) FROM signup WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0 ? 1 : 0; // 1 if user exists, otherwise 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @FXML
    public void handleLoginClick(MouseEvent event) {
        String kuetEmail = tfKuet_email.getText();
        String password = pfPasswordHidden.getText();

        // Validate fields first
        if (isLoginFieldAlright() != 1) {
            if (checkUserExists(kuetEmail) == 1) {
                // Validation errors exist, but account is already registered
                errorMessageLabelLogIn.setText(errorMessage);
            } else {
                // Account is not registered
                errorMessageLabelLogIn.setText("Account is not registered. Please Sign-Up!");
            }
            return;
        }

        // Proceed with login if validation passes
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT firstname, password FROM signup WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, kuetEmail);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                if (storedPassword.equals(password)) {
                    // Handle "Remember Password" checkbox
                    if (chkRememberPass != null && chkRememberPass.isSelected()) {
                        // Update the `remember_pass` field to true for this user
                        String updateRememberQuery = "UPDATE signup SET remember_pass = true WHERE email = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateRememberQuery)) {
                            updateStmt.setString(1, kuetEmail);
                            updateStmt.executeUpdate();
                        }
                    } else if (chkRememberPass != null && !chkRememberPass.isSelected()) {
                        // If unchecked, set `remember_pass` to false
                        String clearRememberQuery = "UPDATE signup SET remember_pass = false WHERE email = ?";
                        try (PreparedStatement clearStmt = connection.prepareStatement(clearRememberQuery)) {
                            clearStmt.setString(1, kuetEmail);
                            clearStmt.executeUpdate();
                        }
                    } else {
                        // Debugging output
                        System.out.println("chkRememberPass is null");
                    }

                    // Fetch firstname and open the home window
                    String firstName = resultSet.getString("firstname");
                    SharedModel.getInstance().setCurrentUserEmail(kuetEmail);
                    startHomeWindow(firstName, event);
                    closeCurrentWindow(event);
                } else {
                    // Email/password mismatch
                    errorMessageLabelLogIn.setText("Email or Password doesn't match!");
                }
            } else {
                // No account found
                errorMessageLabelLogIn.setText("Account is not registered. Please Sign-Up!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessageLabelLogIn.setText("Database error occurred!");
        }
    }



    private int isSignUpFieldAlright() {
        int isPassFilled = 1;
        int isEmailFilled = 1;
        int isFnameFilled = 1;
        int isLnameFilled = 1;
        int isConfirmPassFilled = 1;
        int isEmailValid = 1;
        int isPassValid = 1;
        int isConfirmPassValid = 1;

        if (tfFirstName.getText().isEmpty()) {
            isFnameFilled = 0;
            errorFirstNameMessage = "First Name is empty!";
        } else {
            isFnameFilled = 1;
            errorFirstNameMessage = "";
        }
        if (tfLastName.getText().isEmpty()) {
            isLnameFilled = 0;
            errorLastNameMessage = "Last Name is empty!";
        } else {
            isLnameFilled = 1;
            errorLastNameMessage = "";
        }
        if (tfKuet_email.getText().isEmpty()) {
            isEmailFilled = 0;
            errorEmailMessage = "kuet email address is empty!";
        }
        // Check if email contains "@stud.kuet.ac.bd"
        else if (!tfKuet_email.getText().contains("@stud.kuet.ac.bd")) {
            isEmailValid = 0;
            errorEmailMessage = "Invalid email address!";
        } else {
            isEmailValid = 1;
            errorEmailMessage = "";
        }

        if (pfPasswordHidden.getText().isEmpty()) {
            isPassFilled = 0;
            errorPassMessage = "Password is empty!";
        }
        // Check if password length is at least 8 characters
        else if (pfPasswordHidden.getText().length() < 8) {
            isPassValid = 0;
            errorPassMessage = "Password must contain at least 8 characters!";
        }
        // Check if password contains "#" and atleast of 8 characters
        else if ((!pfPasswordHidden.getText().contains("#")) && pfPasswordHidden.getText().length() >= 8) {
            isPassValid = 0;
            errorPassMessage = "Password must contain a '#'!";
        } else {
            isPassValid = 1;
            errorPassMessage = "";
        }


        if (pfConfirmPassword.getText().isEmpty()) {
            isConfirmPassFilled = 0;
            errorConfirmPasswordMessage = "Confirm Password is empty!";
        }
        // Check if passwords match
        else if (!Objects.equals(pfConfirmPassword.getText(), pfPasswordHidden.getText())) {
            isConfirmPassValid = 0;
            errorConfirmPasswordMessage = "Passwords didn't match!";
        } else {
            isConfirmPassValid = 1;
            errorConfirmPasswordMessage = "";
        }

        // Set error message
        errorMessage = errorFirstNameMessage + "\n" + errorLastNameMessage + "\n" + errorEmailMessage + "\n" + errorPassMessage + "\n" + errorConfirmPasswordMessage;
        // errorMessageLabelSiguUp.setText(errorMessage);

        return isFnameFilled * isLnameFilled * isEmailFilled * isPassFilled * isConfirmPassFilled * isConfirmPassValid * isEmailValid * isPassValid;
    }

    private boolean isSignUpWindowOpen = false;

    private void toggleSignUpWindow(MouseEvent event) throws IOException {
        if (isSignUpWindowOpen) {
            // Close signUp.fxml and open sample.fxml
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("/com/example/login/sample.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();
                closeCurrentWindow(event);
                isSignUpWindowOpen = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Close sample.fxml and open signUp.fxml
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("/com/example/login/signUp.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();
                closeCurrentWindow(event);
                isSignUpWindowOpen = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void handleCreateAccountClick(MouseEvent event) {
        String fname = tfFirstName.getText();
        String lname = tfLastName.getText();
        String kuetEmail = tfKuet_email.getText();
        String password = pfPasswordHidden.getText();

        // Validate fields
        if (isSignUpFieldAlright() != 1) {
            if (checkUserExists(kuetEmail) == 1) {
                errorMessageLabelSiguUp.setText("Account is already registered. Log In!");
            } else {
                errorMessageLabelSiguUp.setText(errorMessage);
            }
            return;
        }
        //currentUserEmail=kuetEmail;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Check if email already exists
            if (checkUserExists(kuetEmail) == 1) {
                errorMessageLabelSiguUp.setText("Account is already registered. Log In!");
                return;
            }

            // Insert user details
            String query = "INSERT INTO signup (email,password,firstname,lastname) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, kuetEmail);
            statement.setString(2, password);
            statement.setString(3, fname);
            statement.setString(4, lname);

            statement.executeUpdate();

            // Store the email in the shared model
            SharedModel.getInstance().setCurrentUserEmail(kuetEmail);
            // Proceed to home window
            startHomeWindow(fname, event);
            closeCurrentWindow(event);

        } catch (SQLIntegrityConstraintViolationException e) {
            errorMessageLabelSiguUp.setText("Account is already registered. Log In!");
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessageLabelSiguUp.setText("Database error occurred!");
        }
    }

    private void closeCurrentWindow(@NotNull MouseEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
    }


    // Define the setWelcomeMessage method
    public void setWelcomeMessage(String message) {
        lblWelcomeMessage.setText(message);
    }

    // This method will start the home.fxml window and pass the first name
    private void startHomeWindow(String fname, MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/login/home.fxml"));
            Parent root = fxmlLoader.load();

            // Pass user data to HomeController
            HelloController controller = fxmlLoader.getController();
            controller.setWelcomeMessage(fname + "!");


            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




   private void openGetStarted(MouseEvent event) {
        try {
            // Load the new FXML (BeforeWeGetStarted.fxml)
            FXMLLoader fxmlLoader = new FXMLLoader(BeforeWeGetStarted.class.getResource("/com/example/login/BeforeWeGetStarted.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage for the new window
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void handleForgetPasswordClick(MouseEvent event) {
        try {
            // Load the new password reset FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/login/logInWithNewPassword.fxml"));
            Parent root = loader.load();

            // Create a new stage (window) for the password reset window
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current window (sample.fxml)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSubmitNewPassword(MouseEvent event) {
        String kuetEmail = tfKuet_email.getText();
        String newPassword = pfPasswordHidden.getText();

        // Step 1: Validate login fields
        if (isLoginFieldAlright() != 1) {
            if (checkUserExists(kuetEmail) == 1) {
                // Validation errors exist, but account is already registered
                errorMessageLabelChngPass.setText(errorMessage);
            } else {
                // Account is not registered
                errorMessageLabelChngPass.setText("Account is not registered. Please Sign-Up!");
            }
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Step 2: Check if the email exists
            String checkQuery = "SELECT password FROM signup WHERE email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, kuetEmail);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                String oldPassword = resultSet.getString("password");

                // Step 3: Ensure the new password is different from the old one
                if (newPassword.equals(oldPassword)) {
                    errorMessageLabelChngPass.setText("New password cannot be the same as the old password." +" \n " + "            Please enter a different password!");
                    return;
                }

                // Step 4: Update the password in the database
                String updateQuery = "UPDATE signup SET password = ? WHERE email = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, kuetEmail);
                updateStmt.executeUpdate();

                // Step 5: Handle "Remember Password" checkbox

                if (chkRememberPass != null && chkRememberPass.isSelected()) {
                    String rememberQuery = "UPDATE signup SET remember_pass = true WHERE email = ?";
                    PreparedStatement rememberStmt = connection.prepareStatement(rememberQuery);
                    rememberStmt.setString(1, kuetEmail);
                    rememberStmt.executeUpdate();
                } else if (chkRememberPass == null) {
                    System.out.println("Checkbox value: chkRememberPass is null");

                }
                // Uncheck Remember Password if not selected
                String rememberQuery = "UPDATE signup SET remember_pass = false WHERE email = ?";
                PreparedStatement rememberStmt = connection.prepareStatement(rememberQuery);
                rememberStmt.setString(1, kuetEmail);
                rememberStmt.executeUpdate();
                // Step 6: Display success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password Changed");
                alert.setHeaderText(null);
                alert.setContentText("Password changed successfully! Please log in with your new password.");
                alert.show();

                // Step 7: Create a delay to switch windows
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(1500),
                        ae -> {
                            // Close the alert
                            alert.close();

                            // Close the current window
                            Stage stage = (Stage) btnSubmitNewPass.getScene().getWindow();
                            stage.close();

                            try {
                                // Load and open sample.fxml (login window)
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
                                Parent root = loader.load();
                                Stage newStage = new Stage();
                                newStage.initStyle(StageStyle.UNDECORATED);
                                newStage.setScene(new Scene(root));

                                // Autofill the email and new password in sample.fxml
                                HelloController controller = loader.getController();
                                controller.setAutofillCredentials(kuetEmail, newPassword);

                                newStage.show();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                ));

                // Start the timeline
                timeline.play();

            } else {
                // Step 8: Handle case where email does not exist
                errorMessageLabelChngPass.setText("Email not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessageLabelChngPass.setText("Database error occurred!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (btnClose != null) {
            btnClose.setOnMouseClicked(mouseEvent -> {
                System.exit(0);
            });
        }

        if (btnSignUp != null) {
            btnSignUp.setOnMouseClicked(mouseEvent -> {
                try {
                    toggleSignUpWindow(mouseEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (btnLoginWhenAlreadyRegistered != null) {
            btnLoginWhenAlreadyRegistered.setOnMouseClicked(event -> {

                // Close the current window signUp.fxml and open sample.fxml
                isSignUpWindowOpen = true;
                try {
                    toggleSignUpWindow(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        if (chkShowPassword != null) {
            chkShowPassword.setOnAction(event -> {
                if (chkShowPassword.isSelected()) {
                    // Show password as plain text
                    pfPasswordText.setText(pfPasswordHidden.getText());
                    pfPasswordText.setVisible(true);
                    pfPasswordHidden.setVisible(false);
                } else {
                    // Hide password, show it as masked
                    pfPasswordHidden.setText(pfPasswordText.getText());
                    pfPasswordHidden.setVisible(true);
                    pfPasswordText.setVisible(false);
                }
            });

             //Initially hide the plain text field and show the password field
              pfPasswordHidden.setVisible(true);
              pfPasswordText.setVisible(false);
        }


        if (btnUploadPicture != null) {
            btnUploadPicture.setOnMouseClicked(event -> {
                // Create a FileChooser
                FileChooser fileChooser = new FileChooser();

                // Set a title for the file chooser window
                fileChooser.setTitle("Select an Image");

                // Add a filter to only show image files
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Image Files", ".png", ".jpg", "*.jpeg")
                );

                // Open the file chooser dialog and get the selected file
                File selectedFile = fileChooser.showOpenDialog(btnUploadPicture.getScene().getWindow());

                if (selectedFile != null) {
                    // Create an Image from the selected file
                    Image image = new Image(selectedFile.toURI().toString());

                    // Assuming there's already an ImageView on the button
                    ImageView imageView = new ImageView(image);

                    // Set the ImageView properties (adjust size as needed)
                    imageView.setFitHeight(19);  // Set preferred height
                    imageView.setFitWidth(53);   // Set preferred width
                    imageView.setPreserveRatio(true);

                    // Set the image view on the button
                    btnUploadPicture.setGraphic(imageView);
                }
            });
        }


        if (btnNext != null) {
            btnNext.setOnMouseClicked(event -> {
                openGetStarted(event); // Open BeforeWeGetStarted.fxml window
                closeCurrentWindow(event); // Close the current window (home.fxml)
            });
        }


    }
}