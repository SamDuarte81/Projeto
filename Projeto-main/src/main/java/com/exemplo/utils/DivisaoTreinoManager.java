package com.exemplo.utils;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;

public class DivisaoTreinoManager {

    private static DivisaoTreinoManager instance;
    private ExercicioStateManager stateManager;

    // DefiniÃ§Ã£o das Divisões de treino
    private final Map<String, DivisaoTreino> divisoesTreino = new LinkedHashMap<String, DivisaoTreino>() {{
        put("ABC", new DivisaoTreino("ABC", "3x por semana", Arrays.asList(
                new DiaTreino("A - Peito/Triceps/Ombro", Arrays.asList(
                        new ExercicioPadrao("Supino Reto", 4, "8-12"),
                        new ExercicioPadrao("Supino Inclinado", 3, "8-12"),
                        new ExercicioPadrao("Crucifixo", 3, "10-15"),
                        new ExercicioPadrao("Desenvolvedor com Halteres", 4, "8-12"),
                        new ExercicioPadrao("Elevação Lateral", 3, "12-15"),
                        new ExercicioPadrao("Triceps Testa", 3, "10-12"),
                        new ExercicioPadrao("Triceps Corda", 3, "12-15")
                )),
                new DiaTreino("B - Costas/Biceps", Arrays.asList(
                        new ExercicioPadrao("Puxada Alta", 4, "8-12"),
                        new ExercicioPadrao("Remada Curvada", 4, "8-12"),
                        new ExercicioPadrao("Remada Cavalinho", 3, "10-12"),
                        new ExercicioPadrao("Pullover", 3, "12-15"),
                        new ExercicioPadrao("Rosca Direta", 3, "10-12"),
                        new ExercicioPadrao("Rosca Martelo", 3, "12-15"),
                        new ExercicioPadrao("Rosca Concentrada", 3, "12-15")
                )),
                new DiaTreino("C - Pernas/Abdomen", Arrays.asList(
                        new ExercicioPadrao("Agachamento", 4, "8-12"),
                        new ExercicioPadrao("Leg Press", 4, "12-15"),
                        new ExercicioPadrao("Cadeira Extensora", 3, "12-15"),
                        new ExercicioPadrao("Mesa Flexora", 3, "12-15"),
                        new ExercicioPadrao("Panturrilha em Pé", 4, "15-20"),
                        new ExercicioPadrao("Abdominal Supra", 3, "15-20"),
                        new ExercicioPadrao("Prancha", 3, "30-60s")
                ))
        )));

        put("Push Pull Legs", new DivisaoTreino("Push Pull Legs", "3x por semana", Arrays.asList(
                new DiaTreino("Push - Empurrar (Peito/Ombro/Triceps)", Arrays.asList(
                        new ExercicioPadrao("Supino Reto", 4, "6-8"),
                        new ExercicioPadrao("Supino Inclinado", 3, "8-10"),
                        new ExercicioPadrao("Supino com Halteres", 3, "10-12"),
                        new ExercicioPadrao("Desenvolvedor Militar", 4, "8-10"),
                        new ExercicioPadrao("Elevação Lateral", 3, "12-15"),
                        new ExercicioPadrao("Elevação Frontal", 3, "12-15"),
                        new ExercicioPadrao("Triceps Frances", 3, "10-12"),
                        new ExercicioPadrao("Mergulho", 3, "8-12")
                )),
                new DiaTreino("Pull - Puxar (Costas/Biceps)", Arrays.asList(
                        new ExercicioPadrao("Barra Fixa", 4, "6-10"),
                        new ExercicioPadrao("Puxada Alta", 3, "8-12"),
                        new ExercicioPadrao("Remada Curvada", 4, "8-10"),
                        new ExercicioPadrao("Remada Unilateral", 3, "10-12"),
                        new ExercicioPadrao("Pullover", 3, "12-15"),
                        new ExercicioPadrao("Rosca Direta", 3, "10-12"),
                        new ExercicioPadrao("Rosca Martelo", 3, "12-15"),
                        new ExercicioPadrao("Rosca Scott", 3, "10-12")
                )),
                new DiaTreino("Legs - Pernas", Arrays.asList(
                        new ExercicioPadrao("Agachamento", 4, "6-8"),
                        new ExercicioPadrao("Levantamento Terra", 4, "6-8"),
                        new ExercicioPadrao("Leg Press", 3, "12-15"),
                        new ExercicioPadrao("Cadeira Extensora", 3, "12-15"),
                        new ExercicioPadrao("Mesa Flexora", 3, "12-15"),
                        new ExercicioPadrao("Hip Thrust", 3, "12-15"),
                        new ExercicioPadrao("Panturrilha em Pé", 4, "15-20"),
                        new ExercicioPadrao("Abdominal Supra", 3, "15-20")
                ))
        )));

        put("Full Body", new DivisaoTreino("Full Body", "3x por semana", Arrays.asList(
                new DiaTreino("Treino A - Corpo Inteiro", Arrays.asList(
                        new ExercicioPadrao("Agachamento", 3, "10-12"),
                        new ExercicioPadrao("Supino Reto", 3, "10-12"),
                        new ExercicioPadrao("Remada Curvada", 3, "10-12"),
                        new ExercicioPadrao("Desenvolvedor com Halteres", 3, "12-15"),
                        new ExercicioPadrao("Rosca Direta", 2, "12-15"),
                        new ExercicioPadrao("Triceps Corda", 2, "12-15"),
                        new ExercicioPadrao("Prancha", 3, "30-45s")
                )),
                new DiaTreino("Treino B - Corpo Inteiro", Arrays.asList(
                        new ExercicioPadrao("Levantamento Terra", 3, "8-10"),
                        new ExercicioPadrao("Supino Inclinado", 3, "10-12"),
                        new ExercicioPadrao("Puxada Alta", 3, "10-12"),
                        new ExercicioPadrao("Elevação Lateral", 3, "12-15"),
                        new ExercicioPadrao("Rosca Martelo", 2, "12-15"),
                        new ExercicioPadrao("Triceps Frances", 2, "12-15"),
                        new ExercicioPadrao("Abdominal Supra", 3, "15-20")
                )),
                new DiaTreino("Treino C - Corpo Inteiro", Arrays.asList(
                        new ExercicioPadrao("Leg Press", 3, "12-15"),
                        new ExercicioPadrao("Supino com Halteres", 3, "10-12"),
                        new ExercicioPadrao("Remada Cavalinho", 3, "10-12"),
                        new ExercicioPadrao("Elevação Frontal", 3, "12-15"),
                        new ExercicioPadrao("Rosca Concentrada", 2, "12-15"),
                        new ExercicioPadrao("Mergulho", 2, "10-12"),
                        new ExercicioPadrao("Russian Twist", 3, "20-30")
                ))
        )));

        put("Upper Lower", new DivisaoTreino("Upper Lower", "4x por semana", Arrays.asList(
                new DiaTreino("Upper A - Membros Superiores", Arrays.asList(
                        new ExercicioPadrao("Supino Reto", 4, "6-8"),
                        new ExercicioPadrao("Remada Curvada", 4, "6-8"),
                        new ExercicioPadrao("Supino Inclinado", 3, "8-12"),
                        new ExercicioPadrao("Puxada Alta", 3, "8-12"),
                        new ExercicioPadrao("Desenvolvedor Militar", 3, "10-12"),
                        new ExercicioPadrao("Rosca Direta", 3, "10-12"),
                        new ExercicioPadrao("Triceps Frances", 3, "10-12")
                )),
                new DiaTreino("Lower A - Membros Inferiores", Arrays.asList(
                        new ExercicioPadrao("Agachamento", 4, "6-8"),
                        new ExercicioPadrao("Levantamento Terra", 4, "6-8"),
                        new ExercicioPadrao("Leg Press", 3, "12-15"),
                        new ExercicioPadrao("Mesa Flexora", 3, "12-15"),
                        new ExercicioPadrao("Hip Thrust", 3, "12-15"),
                        new ExercicioPadrao("Panturrilha em Pé", 4, "15-20"),
                        new ExercicioPadrao("Abdominal Supra", 3, "15-20")
                )),
                new DiaTreino("Upper B - Membros Superiores", Arrays.asList(
                        new ExercicioPadrao("Supino com Halteres", 4, "8-10"),
                        new ExercicioPadrao("Remada Unilateral", 4, "8-10"),
                        new ExercicioPadrao("Supino Declinado", 3, "10-12"),
                        new ExercicioPadrao("Puxada Alta Pegada Aberta", 3, "10-12"),
                        new ExercicioPadrao("Elevação Lateral", 3, "12-15"),
                        new ExercicioPadrao("Rosca Martelo", 3, "12-15"),
                        new ExercicioPadrao("Triceps Corda", 3, "12-15")
                )),
                new DiaTreino("Lower B - Membros Inferiores", Arrays.asList(
                        new ExercicioPadrao("Afundo", 3, "10-12"),
                        new ExercicioPadrao("Stiff", 3, "10-12"),
                        new ExercicioPadrao("Cadeira Extensora", 3, "12-15"),
                        new ExercicioPadrao("Mesa Flexora em Pé", 3, "12-15"),
                        new ExercicioPadrao("Elevação Pélvica", 3, "15-20"),
                        new ExercicioPadrao("Panturrilha Sentado", 4, "15-20"),
                        new ExercicioPadrao("Prancha", 3, "45-60s")
                ))
        )));
    }};

    private DivisaoTreinoManager() {
        stateManager = ExercicioStateManager.getInstance();
    }

    public static DivisaoTreinoManager getInstance() {
        if (instance == null) {
            instance = new DivisaoTreinoManager();
        }
        return instance;
    }

    public void mostrarSelecaoDivisao() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Divisões de Treino");
        alert.setHeaderText("Selecione uma divisão de treino padrão:");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        ToggleGroup group = new ToggleGroup();
        Map<RadioButton, String> radioMap = new HashMap<>();

        for (Map.Entry<String, DivisaoTreino> entry : divisoesTreino.entrySet()) {
            VBox divisaoBox = new VBox(5);

            RadioButton radio = new RadioButton(entry.getKey());
            radio.setToggleGroup(group);
            radio.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            radioMap.put(radio, entry.getKey());

            Label frequencia = new Label("Frequencia: " + entry.getValue().getFrequencia());
            frequencia.setFont(Font.font("Arial", 10));
            frequencia.setStyle("-fx-text-fill: #666;");

            Label dias = new Label("Dias: " + entry.getValue().getDias().size() + " treinos");
            dias.setFont(Font.font("Arial", 10));
            dias.setStyle("-fx-text-fill: #666;");

            divisaoBox.getChildren().addAll(radio, frequencia, dias);
            content.getChildren().add(divisaoBox);
        }

        alert.getDialogPane().setContent(content);

        ButtonType btnAplicar = new ButtonType("Aplicar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnVisualizar = new ButtonType("Visualizar");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnAplicar, btnVisualizar, btnCancelar);

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent()) {
            RadioButton selectedRadio = (RadioButton) group.getSelectedToggle();
            if (selectedRadio != null) {
                String divisaoSelecionada = radioMap.get(selectedRadio);

                if (resultado.get() == btnAplicar) {
                    aplicarDivisao(divisaoSelecionada);
                } else if (resultado.get() == btnVisualizar) {
                    visualizarDivisao(divisaoSelecionada);
                }
            } else if (resultado.get() != btnCancelar) {
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Seleção Necessaria");
                warning.setHeaderText("Selecione uma divisão de treino!");
                warning.showAndWait();
            }
        }
    }

    private void aplicarDivisao(String nomeDivisao) {
        DivisaoTreino divisao = divisoesTreino.get(nomeDivisao);


        stateManager.limparTodasSelecoes();


        for (DiaTreino dia : divisao.getDias()) {
            for (ExercicioPadrao exercicio : dia.getExercicios()) {
                stateManager.marcarExercicioCompleto(exercicio.getNome());
            }
        }

        Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
        sucesso.setTitle("Divisão Aplicada");
        sucesso.setHeaderText("Divisão " + nomeDivisao + " aplicada com sucesso!");
        sucesso.setContentText("Todos os exercicios da divisão foram selecionados. " +
                "Você pode visualizar ou imprimir sua ficha de treino.");
        sucesso.showAndWait();
    }

    private void visualizarDivisao(String nomeDivisao) {
        DivisaoTreino divisao = divisoesTreino.get(nomeDivisao);

        Alert visualizacao = new Alert(Alert.AlertType.INFORMATION);
        visualizacao.setTitle("Visualizar - " + nomeDivisao);
        visualizacao.setHeaderText(nomeDivisao + " (" + divisao.getFrequencia() + ")");

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(15);
        content.setPadding(new Insets(10));

        for (DiaTreino dia : divisao.getDias()) {
            VBox diaBox = new VBox(8);

            Label nomeDia = new Label(dia.getNome());
            nomeDia.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            nomeDia.setStyle("-fx-text-fill: #2196F3;");

            diaBox.getChildren().add(nomeDia);

            for (ExercicioPadrao exercicio : dia.getExercicios()) {
                HBox exercicioBox = new HBox(10);
                exercicioBox.setAlignment(Pos.CENTER_LEFT);

                Label nomeExercicio = new Label(", " + exercicio.getNome());
                nomeExercicio.setFont(Font.font("Arial", 11));
                nomeExercicio.setPrefWidth(200);

                Label series = new Label(exercicio.getSeries() + " series");
                series.setFont(Font.font("Arial", 10));
                series.setStyle("-fx-text-fill: #666;");
                series.setPrefWidth(70);

                Label reps = new Label(exercicio.getRepeticoes() + " reps");
                reps.setFont(Font.font("Arial", 10));
                reps.setStyle("-fx-text-fill: #666;");

                exercicioBox.getChildren().addAll(nomeExercicio, series, reps);
                diaBox.getChildren().add(exercicioBox);
            }

            content.getChildren().add(diaBox);

            // Separador entre dias
            if (!dia.equals(divisao.getDias().get(divisao.getDias().size() - 1))) {
                Label separador = new Label(", ".repeat(50));
                separador.setStyle("-fx-text-fill: #ccc;");
                content.getChildren().add(separador);
            }
        }

        scrollPane.setContent(content);
        scrollPane.setPrefSize(500, 400);
        scrollPane.setFitToWidth(true);

        visualizacao.getDialogPane().setContent(scrollPane);
        visualizacao.getDialogPane().setPrefSize(520, 450);

        visualizacao.showAndWait();
    }

    // Classes auxiliares
    public static class DivisaoTreino {
        private String nome;
        private String frequencia;
        private List<DiaTreino> dias;

        public DivisaoTreino(String nome, String frequencia, List<DiaTreino> dias) {
            this.nome = nome;
            this.frequencia = frequencia;
            this.dias = dias;
        }

        public String getNome() { return nome; }
        public String getFrequencia() { return frequencia; }
        public List<DiaTreino> getDias() { return dias; }
    }

    public static class DiaTreino {
        private String nome;
        private List<ExercicioPadrao> exercicios;

        public DiaTreino(String nome, List<ExercicioPadrao> exercicios) {
            this.nome = nome;
            this.exercicios = exercicios;
        }

        public String getNome() { return nome; }
        public List<ExercicioPadrao> getExercicios() { return exercicios; }
    }

    public static class ExercicioPadrao {
        private String nome;
        private int series;
        private String repeticoes;

        public ExercicioPadrao(String nome, int series, String repeticoes) {
            this.nome = nome;
            this.series = series;
            this.repeticoes = repeticoes;
        }

        public String getNome() { return nome; }
        public int getSeries() { return series; }
        public String getRepeticoes() { return repeticoes; }
    }
}