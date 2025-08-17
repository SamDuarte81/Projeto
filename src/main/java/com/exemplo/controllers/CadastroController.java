package com.exemplo.controllers;

import com.exemplo.services.AuthService;
import com.exemplo.models.TipoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CadastroController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailCadastroField;

    @FXML
    private PasswordField passwordCadastroField;

    @FXML
    private TextField passwordCadastroVisible;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordVisible;

    @FXML
    private Button togglePasswordCadastroBtn;

    @FXML
    private Button toggleConfirmPasswordBtn;

    @FXML
    private RadioButton treinadorCadastroRadio;

    @FXML
    private RadioButton clienteCadastroRadio;

    @FXML
    private ToggleGroup userTypeCadastroGroup;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button cadastrarBtn;

    @FXML
    private Label loginPageLink;

    @FXML
    private Label switchToLoginLink;

    private AuthService authService;

    @FXML
    public void initialize() {
        authService = AuthService.getInstance();
        setupPasswordToggle();

        // Configurar seleção padrão
        if (treinadorCadastroRadio != null) {
            treinadorCadastroRadio.setSelected(true);
        }

        // Configurar eventos dos links
        if (loginPageLink != null) {
            loginPageLink.setOnMouseClicked(this::goToLogin);
        }
        if (switchToLoginLink != null) {
            switchToLoginLink.setOnMouseClicked(this::goToLogin);
        }
    }

    private void setupPasswordToggle() {
        // Sincronizar os campos de senha
        if (passwordCadastroField != null && passwordCadastroVisible != null) {
            passwordCadastroField.textProperty().bindBidirectional(passwordCadastroVisible.textProperty());
            passwordCadastroVisible.setVisible(false);
            passwordCadastroVisible.setManaged(false);
        }

        if (confirmPasswordField != null && confirmPasswordVisible != null) {
            confirmPasswordField.textProperty().bindBidirectional(confirmPasswordVisible.textProperty());
            confirmPasswordVisible.setVisible(false);
            confirmPasswordVisible.setManaged(false);
        }
    }

    @FXML
    private void togglePasswordCadastroVisibility(ActionEvent event) {
        if (passwordCadastroField.isVisible()) {
            passwordCadastroField.setVisible(false);
            passwordCadastroField.setManaged(false);
            passwordCadastroVisible.setVisible(true);
            passwordCadastroVisible.setManaged(true);
            togglePasswordCadastroBtn.setText("");
        } else {
            passwordCadastroField.setVisible(true);
            passwordCadastroField.setManaged(true);
            passwordCadastroVisible.setVisible(false);
            passwordCadastroVisible.setManaged(false);
            togglePasswordCadastroBtn.setText("");
        }
    }

    @FXML
    private void toggleConfirmPasswordVisibility(ActionEvent event) {
        if (confirmPasswordField.isVisible()) {
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);
            confirmPasswordVisible.setVisible(true);
            confirmPasswordVisible.setManaged(true);
            toggleConfirmPasswordBtn.setText("");
        } else {
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
            confirmPasswordVisible.setVisible(false);
            confirmPasswordVisible.setManaged(false);
            toggleConfirmPasswordBtn.setText("");
        }
    }

    @FXML
    private void handleCadastro(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailCadastroField.getText().trim();
        String password = passwordCadastroField.getText();
        String confirmPassword = confirmPasswordField.getText();
        TipoUsuario userType = treinadorCadastroRadio.isSelected() ? TipoUsuario.TREINADOR : TipoUsuario.CLIENTE;

        // ValidaÃ§Ãµes
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos.", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erro", "Por favor, insira um email valido.", Alert.AlertType.ERROR);
            return;
        }

        if (password.length() < 6) {
            showAlert("Erro", "A senha deve ter pelo menos 6 caracteres.", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Erro", "As senhas não coincidem", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidName(firstName) || !isValidName(lastName)) {
            showAlert("Erro", "Nome e sobrenome devem conter apenas letras e espaços.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Tentar realizar o cadastro
            boolean cadastroSucesso = authService.cadastrarUsuario(firstName, lastName, email, password, userType);

            if (cadastroSucesso) {
                showAlert("Sucesso", "Cadastro realizado com sucesso!\nVocê pode fazer login agora.", Alert.AlertType.INFORMATION);

                // Limpar campos
                clearFields();

                // Redirecionar para login usando ActionEvent
                goToLoginFromButton(event);
            }

        } catch (RuntimeException e) {
            showAlert("Erro", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("Erro no cadastro: " + e.getMessage());
            showAlert("Erro", "Erro interno do sistema. Tente novamente.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Limpar todos os campos
        clearFields();

        // Voltar para login
        goToLoginFromButton(event);
    }

    @FXML
    private void goToLogin(MouseEvent event) {
        try {
            loadSceneFromMouseEvent(event, "/fxml/login.fxml", "Sistema de Exercicios - Login");
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de login.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    
    private void goToLoginFromButton(ActionEvent event) {
        try {
            loadSceneFromActionEvent(event, "/fxml/login.fxml", "Sistema de Exercicios - Login");
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de login.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadSceneFromMouseEvent(MouseEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private void loadSceneFromActionEvent(ActionEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    private void clearFields() {
        if (firstNameField != null) firstNameField.clear();
        if (lastNameField != null) lastNameField.clear();
        if (emailCadastroField != null) emailCadastroField.clear();
        if (passwordCadastroField != null) passwordCadastroField.clear();
        if (confirmPasswordField != null) confirmPasswordField.clear();
        if (treinadorCadastroRadio != null) treinadorCadastroRadio.setSelected(true);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    private boolean isValidName(String name) {
        return name.matches("^[A-Za-z0-Ã¿\\s]+$") && name.trim().length() >= 2;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}