package com.exemplo.controllers;

import com.exemplo.utils.ScreenManager;
import com.exemplo.utils.ExercicioStateManager;
import com.exemplo.utils.PrintManager;
import com.exemplo.utils.DivisaoTreinoManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.*;

public class ExerciciosController implements Initializable {

    @FXML
    private Button voltarButton;

    @FXML
    private Button imprimirButton;

    @FXML
    private Button divisoesTreinoButton; // Novo botÃ£o para divisÃµes

    @FXML
    private Button limparSelecaoButton; // BotÃ£o para limpar seleÃ§Ãµes

    @FXML
    private Label tituloLabel;

    @FXML
    private Label contadorLabel; // Para mostrar total selecionado

    @FXML
    private VBox exerciciosContainer;

    private String tipoExercicio;
    private ExercicioStateManager stateManager;
    private PrintManager printManager;
    private DivisaoTreinoManager divisaoManager;

    // Dados organizados por categorias - Superior
    private final Map<String, List<String>> exerciciosSuperior = new LinkedHashMap<String, List<String>>() {{
        put("Peito", Arrays.asList(
                "Supino Reto", "Supino Inclinado", "Supino Declinado",
                "Supino com Halteres", "Crucifixo", "Crossover", "FlexÃµes"
        ));
        put("Costas", Arrays.asList(
                "Puxada Alta", "Remada Curvada", "Remada Cavalinho",
                "Remada Unilateral", "Pullover", "Barra Fixa"
        ));
        put("Ombros", Arrays.asList(
                "Desenvolvedor com Halteres", "Elevação Lateral",
                "Elevação Frontal", "Desenvolvedor Militar", "Encolhimento"
        ));
        put("Biceps", Arrays.asList(
                "Rosca Direta", "Rosca Alternada", "Rosca Martelo",
                "Rosca Concentrada", "Rosca Scott", "Rosca Cabo"
        ));
        put("Triceps", Arrays.asList(
                "Triceps Testa", "Triceps Corda", "Triceps FrancÃªs",
                "Mergulho", "Triceps Coice", "Triceps Supinado"
        ));
        put("AbdÃ´men", Arrays.asList(
                "Abdominal Supra", "Abdominal Infra", "Prancha",
                "Abdominal Obliquo", "Elevação de Pernas", "Russian Twist"
        ));
    }};

    // Dados organizados por categorias - Inferior
    private final Map<String, List<String>> exerciciosInferior = new LinkedHashMap<String, List<String>>() {{
        put("Quadriceps", Arrays.asList(
                "Agachamento", "Leg Press", "Cadeira Extensora",
                "Afundo", "Agachamento Bulgaro", "Hack Squat"
        ));
        put("Posterior", Arrays.asList(
                "Mesa Flexora", "Stiff", "Levantamento Terra",
                "Mesa Flexora em Pé", "Good Morning", "Cadeira Flexora"
        ));
        put("Glueteos", Arrays.asList(
                "Glueteos no Cabo", "Hip Thrust", "Agachamento Sumo",
                "Elevação Pelvica", "Coice no Cabo", "Passada"
        ));
        put("Panturrilha", Arrays.asList(
                "Panturrilha em Pé", "Panturrilha Sentado",
                "Panturrilha no Leg Press", "Elevação de Panturrilha"
        ));
        put("Adutores/Abdutores", Arrays.asList(
                "Cadeira Adutora", "Cadeira Abdutora",
                "Adução no Cabo", "Abdução no Cabo"
        ));
    }};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateManager = ExercicioStateManager.getInstance();
        printManager = PrintManager.getInstance();
        divisaoManager = DivisaoTreinoManager.getInstance();

        configurarBotaoVoltar();
        configurarBotaoImprimir();
        configurarBotaoDivisoes();
        configurarBotaoLimpar();
        atualizarContador();
    }

    public void setTipoExercicio(String tipo) {
        this.tipoExercicio = tipo;
        tituloLabel.setText("Exercicios de " + tipo);
        carregarExercicios();
        atualizarContador();
    }

    private void configurarBotaoVoltar() {
        voltarButton.setOnAction(e -> {
            ScreenManager.mudarTela("/fxml/menu.fxml", voltarButton);
        });

        // Estilo do botÃ£o voltar
        voltarButton.setStyle(
                "-fx-background-color: #6366f1; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 20 10 20;"
        );
    }

    private void configurarBotaoImprimir() {
        if (imprimirButton != null) {
            imprimirButton.setOnAction(e -> {
                if (stateManager.getTotalExerciciosCompletados() == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Nenhum Exercicios Selecionado");
                    alert.setHeaderText("Selecione Exercicioss primeiro!");
                    alert.setContentText("Marque alguns Exercicioss ou escolha uma divisÃ£o de treino antes de imprimir.");
                    alert.showAndWait();
                    return;
                }

                System.out.println("Imprimindo Exercicioss de: " + tipoExercicio);
                printManager.mostrarOpcoesImpressao();
            });

            // Estilo do botÃ£o imprimir
            imprimirButton.setStyle(
                    "-fx-background-color: #10b981; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 10; " +
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
    }

    private void configurarBotaoDivisoes() {
        if (divisoesTreinoButton != null) {
            divisoesTreinoButton.setOnAction(this::mostrarDivisoesTreino);

            // Estilo do botão divisões
            divisoesTreinoButton.setStyle(
                    "-fx-background-color: #f59e0b; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-cursor: hand;"
            );

            // Efeito hover
            divisoesTreinoButton.setOnMouseEntered(e -> {
                divisoesTreinoButton.setStyle(divisoesTreinoButton.getStyle().replace(
                        "-fx-background-color: #f59e0b;",
                        "-fx-background-color: #d97706;"
                ));
            });
            divisoesTreinoButton.setOnMouseExited(e -> {
                divisoesTreinoButton.setStyle(divisoesTreinoButton.getStyle().replace(
                        "-fx-background-color: #d97706;",
                        "-fx-background-color: #f59e0b;"
                ));
            });
        }
    }

    private void configurarBotaoLimpar() {
        if (limparSelecaoButton != null) {
            limparSelecaoButton.setOnAction(this::limparSelecoes);

            // Estilo do botão limpar
            limparSelecaoButton.setStyle(
                    "-fx-background-color: #ef4444; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 10 20 10 20; " +
                            "-fx-cursor: hand;"
            );

            // Efeito hover
            limparSelecaoButton.setOnMouseEntered(e -> {
                limparSelecaoButton.setStyle(limparSelecaoButton.getStyle().replace(
                        "-fx-background-color: #ef4444;",
                        "-fx-background-color: #dc2626;"
                ));
            });
            limparSelecaoButton.setOnMouseExited(e -> {
                limparSelecaoButton.setStyle(limparSelecaoButton.getStyle().replace(
                        "-fx-background-color: #dc2626;",
                        "-fx-background-color: #ef4444;"
                ));
            });
        }
    }

    @FXML
    private void mostrarDivisoesTreino(ActionEvent event) {
        divisaoManager.mostrarSelecaoDivisao();
        // Recarregar a interface apos aplicar uma divisão
        carregarExercicios();
        atualizarContador();
    }

    @FXML
    private void limparSelecoes(ActionEvent event) {
        if (stateManager.getTotalExerciciosCompletados() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nenhuma Seleção");
            alert.setHeaderText("Não há¡ Exercicios selecionados!");
            alert.showAndWait();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Limpar Seleção");
        confirmacao.setHeaderText("Desmarcar todos os Exercicioss?");
        confirmacao.setContentText("Esta ação irá¡ desmarcar todos os " +
                stateManager.getTotalExerciciosCompletados() + " Exercicioss selecionados.");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                stateManager.limparTodasSelecoes();
                carregarExercicios();
                atualizarContador();

                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Seleções Limpas");
                sucesso.setHeaderText("Todos os Exercicioss foram desmarcados!");
                sucesso.showAndWait();
            }
        });
    }

    private void atualizarContador() {
        if (contadorLabel != null) {
            int total = stateManager.getTotalExerciciosCompletados();
            contadorLabel.setText("Exercicioss selecionados: " + total);

            // Mudar cor baseado na quantidade
            String cor = total > 0 ? "#10b981" : "#6b7280";
            contadorLabel.setStyle(
                    "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: " + cor + ";"
            );
        }
    }

    private void carregarExercicios() {
        exerciciosContainer.getChildren().clear();

        Map<String, List<String>> categorias = tipoExercicio.equals("Superior") ?
                exerciciosSuperior : exerciciosInferior;

        for (Map.Entry<String, List<String>> categoria : categorias.entrySet()) {
            // Criar o cabeçalho da categoria
            Label categoriaLabel = criarCategoriaLabel(categoria.getKey(), categoria.getValue());
            exerciciosContainer.getChildren().add(categoriaLabel);

            // Adicionar espaçamento
            VBox.setMargin(categoriaLabel, new Insets(20, 0, 10, 0));

            // Adicionar Exercicioss da categoria
            for (String exercicio : categoria.getValue()) {
                HBox exercicioBox = criarItemExercicio(exercicio);
                exerciciosContainer.getChildren().add(exercicioBox);
                VBox.setMargin(exercicioBox, new Insets(5, 0, 5, 0));
            }

            // Espaçamento entre categorias
            exerciciosContainer.getChildren().add(new Label()); // EspaÃ§o em branco
        }
    }

    private Label criarCategoriaLabel(String nomeCategoria, List<String> exerciciosCategoria) {
        // Calcular progresso da categoria
        int completados = 0;
        for (String exercicio : exerciciosCategoria) {
            if (stateManager.isExercicioCompleto(exercicio)) {
                completados++;
            }
        }
        int total = exerciciosCategoria.size();

        String textoProgresso = String.format("%s (%d/%d)", nomeCategoria.toUpperCase(), completados, total);
        Label categoriaLabel = new Label(textoProgresso);

        // Cor diferente se categoria estiver completa
        String corFundo = completados == total ? "#d4edda" : "#e2e8f0";
        String corTexto = completados == total ? "#155724" : "#2d3748";
        String corBorda = completados == total ? "#c3e6cb" : "#cbd5e0";

        categoriaLabel.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + corTexto + "; " +
                        "-fx-background-color: " + corFundo + "; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 12 20 12 20; " +
                        "-fx-max-width: 400; " +
                        "-fx-alignment: center-left; " +
                        "-fx-border-color: " + corBorda + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8;"
        );

        return categoriaLabel;
    }

    private HBox criarItemExercicio(String nomeExercicio) {
        HBox container = new HBox();
        container.setSpacing(15);
        container.setPadding(new Insets(15));

        // Verificar se o Exercicios já marcado como completo
        boolean isCompleto = stateManager.isExercicioCompleto(nomeExercicio);

        String corFundo = isCompleto ? "#f0f4f8" : "white";
        String corBorda = isCompleto ? "#68d391" : "#e0e0e0";

        container.setStyle(
                "-fx-background-color: " + corFundo + "; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: " + corBorda + "; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 1; " +
                        "-fx-max-width: 400; " +
                        "-fx-min-height: 60;"
        );

        // Adicionar sombra
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(2.0);
        shadow.setOffsetY(2.0);
        shadow.setColor(Color.color(0.4, 0.5, 0.5, 0.3));
        container.setEffect(shadow);

        // CheckBox para marcar como feito
        CheckBox checkBox = new CheckBox();
        checkBox.setStyle("-fx-focus-color: #6366f1;");
        checkBox.setSelected(isCompleto); // Restaurar estado salvo

        // Label com nome do Exercicios
        Label nomeLabel = new Label(nomeExercicio);
        String corTexto = isCompleto ? "#888888" : "#333333";
        String strikethrough = isCompleto ? "-fx-strikethrough: true; " : "";

        nomeLabel.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: " + corTexto + "; " +
                        "-fx-padding: 0 10 0 10; " +
                        strikethrough
        );
        nomeLabel.setWrapText(true);
        nomeLabel.setMaxWidth(250);

        // Configurar para o label crescer
        HBox.setHgrow(nomeLabel, Priority.ALWAYS);

        // Botão de detalhes
        Button detalhesButton = new Button("Detalhes");
        detalhesButton.setStyle(
                "-fx-background-color: #6366f1; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-font-size: 12px; " +
                        "-fx-padding: 8 15 8 15; " +
                        "-fx-cursor: hand;"
        );

        // Ação do checkbox - SALVAR ESTADO
        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()) {
                // Marcar como completo
                stateManager.marcarExercicioCompleto(nomeExercicio);

                nomeLabel.setStyle(
                        nomeLabel.getStyle() +
                                "-fx-strikethrough: true; " +
                                "-fx-text-fill: #888888;"
                );
                container.setStyle(container.getStyle()
                        .replace("-fx-background-color: white;", "-fx-background-color: #f0f4f8;")
                        .replace("-fx-border-color: #e0e0e0;", "-fx-border-color: #68d391;")
                );
                System.out.println("Exercicios marcado como concluÃ­do: " + nomeExercicio);
            } else {
                // Desmarcar
                stateManager.desmarcarExercicio(nomeExercicio);

                nomeLabel.setStyle(
                        nomeLabel.getStyle()
                                .replace("-fx-strikethrough: true;", "")
                                .replace("-fx-text-fill: #888888;", "-fx-text-fill: #333333;")
                );
                container.setStyle(container.getStyle()
                        .replace("-fx-background-color: #f0f4f8;", "-fx-background-color: white;")
                        .replace("-fx-border-color: #68d391;", "-fx-border-color: #e0e0e0;")
                );
                System.out.println("Exercicios desmarcado: " + nomeExercicio);
            }

            // Atualizar contador e recarregar para atualizar contadores das categorias
            atualizarContador();
            carregarExercicios();
        });


        detalhesButton.setOnAction(e -> {
            System.out.println("Mostrando detalhes de: " + nomeExercicio);

        });

        // Efeito hover no container
        container.setOnMouseEntered(e -> {
            if (!checkBox.isSelected()) {
                container.setStyle(container.getStyle()
                        .replace("-fx-background-color: white;", "-fx-background-color: #f8f9ff;"));
            }
        });
        container.setOnMouseExited(e -> {
            if (!checkBox.isSelected()) {
                container.setStyle(container.getStyle()
                        .replace("-fx-background-color: #f8f9ff;", "-fx-background-color: white;"));
            }
        });

        // Efeito hover no botão detalhes
        detalhesButton.setOnMouseEntered(e -> {
            detalhesButton.setStyle(detalhesButton.getStyle().replace(
                    "-fx-background-color: #6366f1;",
                    "-fx-background-color: #4f46e5;"
            ));
        });
        detalhesButton.setOnMouseExited(e -> {
            detalhesButton.setStyle(detalhesButton.getStyle().replace(
                    "-fx-background-color: #4f46e5;",
                    "-fx-background-color: #6366f1;"
            ));
        });

        container.getChildren().addAll(checkBox, nomeLabel, detalhesButton);

        return container;
    }
}