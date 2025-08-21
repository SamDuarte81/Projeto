package com.exemplo.controllers;

import com.exemplo.utils.ScreenManager;
import com.exemplo.utils.ExercicioStateManager;
import com.exemplo.utils.PrintManager;
import com.exemplo.services.AuthService;
import com.exemplo.models.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;
import javafx.scene.control.ButtonType;

public class MenuController implements Initializable {

    @FXML
    private Button homeButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button superiorButton;

    @FXML
    private Button inferiorButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Button imprimirButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logoutButton;

    private ExercicioStateManager stateManager;
    private PrintManager printManager;
    private AuthService authService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateManager = ExercicioStateManager.getInstance();
        printManager = PrintManager.getInstance();
        authService = AuthService.getInstance();

        // Verificar se usuario está logado
        if (!authService.isUsuarioLogado()) {
            // Se não estiver logado, voltar para tela de login
            ScreenManager.mudarTela("/fxml/login.fxml", homeButton);
            return;
        }

        // Adicionar efeitos visuais
        adicionarEfeitosBotoes();


        configurarAcoesBotoes();


        atualizarEstatisticas();


        atualizarInformacoesUsuario();
    }

    private void atualizarInformacoesUsuario() {
        if (authService.isUsuarioLogado()) {
            Usuario usuario = authService.getUsuarioLogado();
            String nomeUsuario = usuario.getPrimeiroNome();
            String tipoUsuario = usuario.getTipo().getDescricao();

            if (welcomeLabel != null) {
                welcomeLabel.setText("Bem-vindo(a), " + nomeUsuario + " (" + tipoUsuario + ")");
                welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #6366f1;");
            }
        }
    }

    private void adicionarEfeitosBotoes() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0.4, 0.5, 0.5));

        if (superiorButton != null) superiorButton.setEffect(shadow);
        if (inferiorButton != null) inferiorButton.setEffect(shadow);

        // Efeitos hover
        if (superiorButton != null) {
            superiorButton.setOnMouseEntered(e -> {
                superiorButton.setStyle(superiorButton.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
                atualizarEstatisticas();
            });
            superiorButton.setOnMouseExited(e -> superiorButton.setStyle(
                    superiorButton.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", "")
            ));
        }

        if (inferiorButton != null) {
            inferiorButton.setOnMouseEntered(e -> {
                inferiorButton.setStyle(inferiorButton.getStyle() + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
                atualizarEstatisticas();
            });
            inferiorButton.setOnMouseExited(e -> inferiorButton.setStyle(
                    inferiorButton.getStyle().replace("-fx-scale-x: 1.05; -fx-scale-y: 1.05;", "")
            ));
        }
    }

    private void configurarAcoesBotoes() {
        if (superiorButton != null) {
            superiorButton.setOnAction(e -> abrirExercicios("Superior"));
        }
        if (inferiorButton != null) {
            inferiorButton.setOnAction(e -> abrirExercicios("Inferior"));
        }



        if (helpButton != null) {
            helpButton.setOnAction(e -> mostrarAjuda());
        }

        // Configurar impressao
        if (imprimirButton != null) {
            imprimirButton.setOnAction(e -> {
                System.out.println("Iniciando impressão dos exercicios selecionados");
                printManager.mostrarOpcoesImpressao();
            });

            // Estilo do botão imprimir
            imprimirButton.setStyle(
                    "-fx-background-color: #10b981; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 8; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-cursor: hand;"
            );

            // Efeito hover
            imprimirButton.setOnMouseEntered(e -> {
                imprimirButton.setStyle(imprimirButton.getStyle().replace(
                        "-fx-background-color: #10b981;",
                        "-fx-background-color: #059669;"
                ));
            });
            imprimirButton.setOnMouseExited(e -> {
                imprimirButton.setStyle(imprimirButton.getStyle().replace(
                        "-fx-background-color: #059669;",
                        "-fx-background-color: #10b981;"
                ));
            });
        }

        // Configurar botão de logout
        if (logoutButton != null) {
            logoutButton.setOnAction(e -> realizarLogout());

            // Estilo do botão logout
            logoutButton.setStyle(
                    "-fx-background-color: #ef4444; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 8; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-cursor: hand;"
            );

            // Efeito hover
            logoutButton.setOnMouseEntered(e -> {
                logoutButton.setStyle(logoutButton.getStyle().replace(
                        "-fx-background-color: #ef4444;",
                        "-fx-background-color: #dc2626;"
                ));
            });
            logoutButton.setOnMouseExited(e -> {
                logoutButton.setStyle(logoutButton.getStyle().replace(
                        "-fx-background-color: #dc2626;",
                        "-fx-background-color: #ef4444;"
                ));
            });
        }
    }

    private void mostrarAjuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajuda - Sistema de Exercicios");
        alert.setHeaderText("Informações do Sistema");

        StringBuilder conteudo = new StringBuilder();

        if (authService.isUsuarioLogado()) {
            Usuario usuario = authService.getUsuarioLogado();
            conteudo.append(" Usuario: ").append(usuario.getNomeCompleto()).append("\n");
            conteudo.append(" Tipo: ").append(usuario.getTipo().getDescricao()).append("\n");
        }

        if (stateManager != null) {
            conteudo.append("ðŸ“Š exercicios selecionados: ").append(stateManager.getTotalExerciciosCompletados()).append("\n\n");
        }

        conteudo.append("Funcionalidades:\n");
        conteudo.append("Selecionar exercicios por categoria\n");
        conteudo.append("Usar divisões de treino pre-definidas\n");
        conteudo.append("Imprimir fichas personalizadas\n");
        conteudo.append("Salvar progresso automaticamente\n\n");
        conteudo.append("Dicas:\n");
        conteudo.append("Use 'divisões de Treino' para seleção rapida\n");
        conteudo.append("Imprima diferentes formatos de ficha\n");
        conteudo.append("Seus exercicios ficam salvos entre sessÃµes");

        alert.setContentText(conteudo.toString());
        alert.showAndWait();
    }

    private void realizarLogout() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Logout");
        confirmacao.setHeaderText("Sair do Sistema");


        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Realizar logout
            authService.logout();

            // Voltar para tela de login
            ScreenManager.mudarTela("/fxml/login.fxml", logoutButton);
        }
    }

    private void abrirExercicios(String tipo) {
        System.out.println("Abrindo exercicios de: " + tipo);
        ScreenManager.mudarTelaComDados("/fxml/exercicios.fxml", superiorButton, tipo);
    }

    private void atualizarEstatisticas() {
        if (stateManager != null && statusLabel != null) {
            int totalCompletados = stateManager.getTotalExerciciosCompletados();

            if (totalCompletados == 0) {
                statusLabel.setText("Nenhum exercicios completado ainda");
                statusLabel.setStyle("-fx-text-fill: #718096; -fx-font-size: 14px;");
            } else {
                statusLabel.setText("¯ " + totalCompletados + " exercicios(s) completado(s)!");
                statusLabel.setStyle("-fx-text-fill: #38a169; -fx-font-size: 14px; -fx-font-weight: bold;");
            }


            atualizarBotoesComProgresso();
        }
    }

    private void atualizarBotoesComProgresso() {
        if (stateManager == null) return;

        int totalCompletados = stateManager.getTotalExerciciosCompletados();

        if (totalCompletados > 0) {
            // Adicionar um pequeno indicador
            String indicadorStyle = "-fx-border-color: #38a169; -fx-border-width: 2;";

            if (superiorButton != null && !superiorButton.getStyle().contains("border-color")) {
                superiorButton.setStyle(superiorButton.getStyle() + indicadorStyle);
            }
            if (inferiorButton != null && !inferiorButton.getStyle().contains("border-color")) {
                inferiorButton.setStyle(inferiorButton.getStyle() + indicadorStyle);
            }
        }
    }
}