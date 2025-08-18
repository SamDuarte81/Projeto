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

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisible;

    @FXML
    private Button togglePasswordBtn;

    @FXML
    private RadioButton treinadorRadio;

    @FXML
    private RadioButton clienteRadio;

    @FXML
    private ToggleGroup userTypeGroup;

    @FXML
    private Button loginBtn;

    @FXML
    private Label registerPageLink;

    @FXML
    private Label switchToCadastroLink;

    private AuthService authService;

    @FXML
    public void initialize() {
        authService = AuthService.getInstance();
        setupPasswordToggle();

        // Configurar seleção padrão
        if (treinadorRadio != null) {
            treinadorRadio.setSelected(true);
        }

        // Configurar eventos dos links
        if (registerPageLink != null) {
            registerPageLink.setOnMouseClicked(this::goToCadastro);
        }
        if (switchToCadastroLink != null) {
            switchToCadastroLink.setOnMouseClicked(this::goToCadastro);
        }

       //listar ja cadastrados
        System.out.println("Usuarios disponiveis para login: ===");
        authService.listarUsuarios();
    }

    private void setupPasswordToggle() {
        if (passwordField != null && passwordVisible != null) {
            // Sincronizar os campos de senha
            passwordField.textProperty().bindBidirectional(passwordVisible.textProperty());

            // Configurar visibilidade inicial
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
        }
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            togglePasswordBtn.setText("");
        } else {
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
            togglePasswordBtn.setText("");
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        System.out.println("=== INICIANDO PROCESSO DE LOGIN ===");

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        System.out.println("Email digitado: " + email);
        System.out.println("Senha digitada: [OCULTA]");
        System.out.println("Tipo selecionado: " + (treinadorRadio.isSelected() ? "TREINADOR" : "CLIENTE"));

        // Validações Basicas
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos.", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erro", "Por favor, insira um email valido.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Tentar fazer login
            System.out.println("Tentando autenticar usuario...");
            boolean loginSucesso = authService.autenticar(email, password);

            if (loginSucesso) {
                System.out.println("Autenticação bem-sucedida!");

                // Verificar se o tipo de usuario logado corresponde ao selecionado
                TipoUsuario tipoSelecionado = treinadorRadio.isSelected() ? TipoUsuario.TREINADOR : TipoUsuario.CLIENTE;
                TipoUsuario tipoUsuario = authService.getUsuarioLogado().getTipo();

                System.out.println("Tipo esperado: " + tipoSelecionado);
                System.out.println("Tipo do usuario: " + tipoUsuario);

                if (!tipoUsuario.equals(tipoSelecionado)) {
                    // Fazer logout e mostrar erro
                    authService.logout();
                    showAlert("Erro", "Tipo de usuario não corresponde. Você esta cadastrado como " +
                            tipoUsuario.getDescricao(), Alert.AlertType.ERROR);
                    return;
                }

                // Login bem-sucedido, mostrar mensagem de sucesso
                System.out.println("Login concluido com sucesso! Redirecionando para menu...");

                showAlert("Sucesso", "Login realizado com sucesso!\nBem-vindo(a), " +
                        authService.getUsuarioLogado().getNomeCompleto(), Alert.AlertType.INFORMATION);

                // Redirecionar para tela principal
                loadMainScreen(event);

            } else {
                System.out.println("Falha na autenticação!");
                showAlert("Erro", "Email ou senha incorretos.\n\nDica: Verifique se o tipo de usuario correto.", Alert.AlertType.ERROR);

                // Limpar campos de senha
                passwordField.clear();
                passwordVisible.clear();
            }

        } catch (Exception e) {
            System.err.println("Erro no login: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erro", "Erro interno do sistema. Tente novamente.\nDetalhes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goToCadastro(MouseEvent event) {
        try {
            System.out.println("Navegando para tela de cadastro...");
            loadSceneFromMouseEvent(event, "/fxml/cadastro.fxml", "Sistema de exercicios - Cadastro");
        } catch (IOException e) {
            showAlert("Erro", "Erro ao carregar a tela de cadastro.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadSceneFromMouseEvent(MouseEvent event, String fxmlPath, String title) throws IOException {
        System.out.println("Carregando tela: " + fxmlPath);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();

        System.out.println("Tela carregada com sucesso!");
    }

    private void loadMainScreen(ActionEvent event) {
        try {
            System.out.println("Carregando tela principal (menu)...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sistema de Exercicios - Menu Principal");
            stage.show();

            System.out.println("Menu principal carregado com sucesso!");

        } catch (IOException e) {
            System.err.println("ERRO Critico: Não foi possivel carregar a tela do menu!");
            e.printStackTrace();

            showAlert("Erro critico",
                    "Não foi possivel carregar a tela principal.\n" +
                            "Erro: " + e.getMessage() + "\n\n" +
                            "Verifique se o arquivo /fxml/menu.fxml existe.",
                    Alert.AlertType.ERROR);
        } catch (Exception e) {
            System.err.println("ERRO INESPERADO: " + e.getMessage());
            e.printStackTrace();

            showAlert("Erro",
                    "Erro inesperado ao carregar tela principal.\n" +
                            "Detalhes: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}