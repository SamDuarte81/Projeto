package com.exemplo.utils;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PrintManager {

    private static PrintManager instance;
    private ExercicioStateManager stateManager;
    private DivisaoTreinoManager divisaoManager;

    // Dados dos exercicios (mesmos do controller)
    private final Map<String, List<String>> exerciciosSuperior = new LinkedHashMap<String, List<String>>() {{
        put("Peito", Arrays.asList(
                "Supino Reto", "Supino Inclinado", "Supino Declinado",
                "Supino com Halteres", "Crucifixo", "Crossover", "Flexões"
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
        put("Abdomen", Arrays.asList(
                "Abdominal Supra", "Abdominal Infra", "Prancha",
                "Abdominal Obliquo", "Elevação de Pernas", "Russian Twist"
        ));
    }};

    private final Map<String, List<String>> exerciciosInferior = new LinkedHashMap<String, List<String>>() {{
        put("Quadriceps", Arrays.asList(
                "Agachamento", "Leg Press", "Cadeira Extensora",
                "Afundo", "Agachamento Bulgaro", "Hack Squat"
        ));
        put("Posterior", Arrays.asList(
                "Mesa Flexora", "Stiff", "Levantamento Terra",
                "Mesa Flexora em Pé", "Good Morning", "Cadeira Flexora"
        ));
        put("Gluteos", Arrays.asList(
                "Gluteo no Cabo", "Hip Thrust", "Agachamento Sumo",
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

    // Definição das divisões de treino para detecção automatica
    private final Map<String, List<DiaPersonalizado>> divisoesPadrao = new LinkedHashMap<String, List<DiaPersonalizado>>() {{
        // ABC
        put("ABC", Arrays.asList(
                new DiaPersonalizado("Dia A - Peito/Triceps/Ombros", Arrays.asList("Peito", "Triceps", "Ombros")),
                new DiaPersonalizado("Dia B - Costas/Biceps", Arrays.asList("Costas", "Biceps")),
                new DiaPersonalizado("Dia C - Pernas/Abdomen", Arrays.asList("Quadriceps", "Posterior", "Gluteos", "Panturrilha", "Abdomen"))
        ));

        // Push Pull Legs
        put("Push Pull Legs", Arrays.asList(
                new DiaPersonalizado("Push - Empurrar (Peito/Ombros/Triceps)", Arrays.asList("Peito", "Ombros", "Triceps")),
                new DiaPersonalizado("Pull - Puxar (Costas/Biceps)", Arrays.asList("Costas", "Biceps")),
                new DiaPersonalizado("Legs - Pernas/Abdomen", Arrays.asList("Quadriceps", "Posterior", "Gluteos", "Panturrilha", "Abdomen"))
        ));

        // Upper Lower
        put("Upper Lower", Arrays.asList(
                new DiaPersonalizado("Upper A - Membros Superiores", Arrays.asList("Peito", "Costas", "Ombros", "Biceps", "Triceps")),
                new DiaPersonalizado("Lower A - Membros Inferiores", Arrays.asList("Quadriceps", "Posterior", "Gluteos", "Panturrilha", "Abdomen")),
                new DiaPersonalizado("Upper B - Membros Superiores", Arrays.asList("Peito", "Costas", "Ombros", "Biceps", "Triceps")),
                new DiaPersonalizado("Lower B - Membros Inferiores", Arrays.asList("Quadriceps", "Posterior", "Gluteos", "Panturrilha", "Abdomen"))
        ));

        // Full Body
        put("Full Body", Arrays.asList(
                new DiaPersonalizado("Treino A - Corpo Inteiro", Arrays.asList("Peito", "Costas", "Ombros", "Biceps", "Triceps", "Quadriceps", "Abdomen")),
                new DiaPersonalizado("Treino B - Corpo Inteiro", Arrays.asList("Peito", "Costas", "Ombros", "Biceps", "Triceps", "Quadriceps", "Abdomen")),
                new DiaPersonalizado("Treino C - Corpo Inteiro", Arrays.asList("Peito", "Costas", "Ombros", "Biceps", "Triceps", "Quadriceps", "Abdomen"))
        ));
    }};

    private PrintManager() {
        stateManager = ExercicioStateManager.getInstance();
        divisaoManager = DivisaoTreinoManager.getInstance();
    }

    public static PrintManager getInstance() {
        if (instance == null) {
            instance = new PrintManager();
        }
        return instance;
    }

    public void mostrarOpcoesImpressao() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Imprimir Exercicios");
        alert.setHeaderText("Selecione o que deseja imprimir:");

        // Cria botões personalizados
        ButtonType btnTodos = new ButtonType("Todos os Selecionados");
        ButtonType btnSuperior = new ButtonType("Apenas Superior");
        ButtonType btnInferior = new ButtonType("Apenas Inferior");
        ButtonType btnFichaCompleta = new ButtonType("Ficha com Series");
        ButtonType btnCancelar = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(btnTodos, btnSuperior, btnInferior, btnFichaCompleta, btnCancelar);

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent()) {
            if (resultado.get() == btnTodos) {
                imprimirExerciciosSelecionados("Todos");
            } else if (resultado.get() == btnSuperior) {
                imprimirExerciciosSelecionados("Superior");
            } else if (resultado.get() == btnInferior) {
                imprimirExerciciosSelecionados("Inferior");
            } else if (resultado.get() == btnFichaCompleta) {
                imprimirFichaCompleta();
            }
        }
    }

    public void imprimirExerciciosSelecionados(String tipo) {
        if (stateManager.getTotalExerciciosCompletados() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nenhum Exercicio");
            alert.setHeaderText("Não ha; exercicios selecionados para imprimir!");
            alert.setContentText("Marque alguns Exercicios ou escolha uma divisões de treino antes de tentar imprimir.");
            alert.showAndWait();
            return;
        }

        // Criar o conteudo para impressão
        VBox conteudo = criarConteudoImpressao(tipo);
        executarImpressao(conteudo);
    }

    public void imprimirFichaCompleta() {
        if (stateManager.getTotalExerciciosCompletados() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nenhum Exercicio");
            alert.setHeaderText("Não há exercicios selecionados!");
            alert.setContentText("Selecione exercicios ou uma divisão de treino primeiro.");
            alert.showAndWait();
            return;
        }

        VBox conteudo = criarFichaCompletaComSeries();
        executarImpressao(conteudo);
    }

    private void executarImpressao(VBox conteudo) {
        // Configurar impressÃ£o
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null && printer != null) {
            
            boolean proceed = job.showPrintDialog(null);

            if (proceed) {
                // configura a pagina maximizar
                PageLayout pageLayout = printer.createPageLayout(
                        Paper.A4,
                        PageOrientation.PORTRAIT,
                        Printer.MarginType.HARDWARE_MINIMUM
                );
                job.getJobSettings().setPageLayout(pageLayout);

                // força a escala
                conteudo.autosize();
                conteudo.applyCss();
                conteudo.layout();

                // Configurar escala para caber 
                double scaleX = pageLayout.getPrintableWidth() / conteudo.getBoundsInParent().getWidth();
                double scaleY = pageLayout.getPrintableHeight() / conteudo.getBoundsInParent().getHeight();
                double scale = Math.min(scaleX, scaleY) * 0.9; // 90% para garantir que caiba

                // Aplicar escala se necessario
                if (scale < 1.0) {
                    conteudo.setScaleX(scale);
                    conteudo.setScaleY(scale);
                }

                // Imprimir
                boolean success = job.printPage(conteudo);
                if (success) {
                    job.endJob();
                    mostrarSucessoImpressao();
                } else {
                    mostrarErroImpressao("Falha ao imprimir a pagina");
                }
            }
        } else {
            mostrarErroImpressao("Nenhuma impressora encontrada");
        }
    }

    private VBox criarFichaCompletaComSeries() {
        VBox conteudo = new VBox();
        conteudo.setSpacing(4);
        conteudo.setPadding(new Insets(10));
        conteudo.setStyle("-fx-background-color: white;");

        // cabeçalho
        Label titulo = new Label("FICHA DE TREINO PERSONALIZADA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titulo.setStyle("-fx-text-fill: black; -fx-underline: true;");
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(Double.MAX_VALUE);

        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"));
        Label cabecalho = new Label(String.format("Gerado em: %s | Total: %d exercicios",
                dataHora, stateManager.getTotalExerciciosCompletados()));
        cabecalho.setFont(Font.font("Arial", 9));
        cabecalho.setStyle("-fx-text-fill: #666666;");
        cabecalho.setAlignment(Pos.CENTER);
        cabecalho.setMaxWidth(Double.MAX_VALUE);

        conteudo.getChildren().addAll(titulo, cabecalho);

        // Separador
        Label separador1 = new Label("-".repeat(70));
        separador1.setFont(Font.font("Arial", 7));
        separador1.setStyle("-fx-text-fill: black;");
        separador1.setAlignment(Pos.CENTER);
        separador1.setMaxWidth(Double.MAX_VALUE);
        conteudo.getChildren().add(separador1);

        // Detectar e mostrar tipo de divisão
        String tipoDivisao = detectarTipoDivisao();
        Label infoDivisao = new Label("divisão: " + tipoDivisao + " | Frequencia: " + obterFrequencia(tipoDivisao));
        infoDivisao.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        infoDivisao.setStyle("-fx-text-fill: #2196F3;");
        infoDivisao.setAlignment(Pos.CENTER);
        infoDivisao.setMaxWidth(Double.MAX_VALUE);
        conteudo.getChildren().add(infoDivisao);

        // Organizar exercicios por dias
        List<DiaPersonalizado> diasTreino = organizarExerciciosPorDias();
        adicionarDiasTreinoComSeries(conteudo, diasTreino);

        // Rodape
        Label separador2 = new Label("-".repeat(70));
        separador2.setFont(Font.font("Arial", 7));
        separador2.setStyle("-fx-text-fill: black;");
        separador2.setAlignment(Pos.CENTER);
        separador2.setMaxWidth(Double.MAX_VALUE);
        conteudo.getChildren().add(separador2);

        Label rodape = new Label(" Bom treino! Lembre-se de manter a forma correta e hidratação.");
        rodape.setFont(Font.font("Arial", FontWeight.BOLD, 8));
        rodape.setStyle("-fx-text-fill: #666666;");
        rodape.setAlignment(Pos.CENTER);
        rodape.setMaxWidth(Double.MAX_VALUE);
        conteudo.getChildren().add(rodape);

        return conteudo;
    }

    private String detectarTipoDivisao() {
        Set<String> exerciciosSelecionados = stateManager.getExerciciosCompletados();

        // Verificar cada divisão padrão
        for (Map.Entry<String, List<DiaPersonalizado>> divisao : divisoesPadrao.entrySet()) {
            boolean corresponde = true;
            Set<String> exerciciosDivisao = new HashSet<>();

            // Coletar todos os exercicios esperados da divisão
            for (DiaPersonalizado dia : divisao.getValue()) {
                for (String categoria : dia.getCategorias()) {
                    Map<String, List<String>> todasCategorias = new LinkedHashMap<>();
                    todasCategorias.putAll(exerciciosSuperior);
                    todasCategorias.putAll(exerciciosInferior);

                    if (todasCategorias.containsKey(categoria)) {
                        exerciciosDivisao.addAll(todasCategorias.get(categoria));
                    }
                }
            }

            // Verificar se a seleção atual corresponde a essa divisão (pelo menos 70% dos exercicios)
            int exerciciosComuns = 0;
            for (String exercicio : exerciciosSelecionados) {
                if (exerciciosDivisao.contains(exercicio)) {
                    exerciciosComuns++;
                }
            }

            double percentual = (double) exerciciosComuns / exerciciosSelecionados.size();
            if (percentual >= 0.7) {
                return divisao.getKey();
            }
        }

        return "Personalizada";
    }

    private String obterFrequencia(String tipoDivisao) {
        switch (tipoDivisao) {
            case "ABC": return "3x por semana";
            case "Push Pull Legs": return "3x por semana";
            case "Upper Lower": return "4x por semana";
            case "Full Body": return "3x por semana";
            default: return "Conforme seleção";
        }
    }

    private List<DiaPersonalizado> organizarExerciciosPorDias() {
        String tipoDivisao = detectarTipoDivisao();

        // Se for uma divisão conhecida, usar a estrutura padrão
        if (divisoesPadrao.containsKey(tipoDivisao)) {
            List<DiaPersonalizado> diasPadrao = divisoesPadrao.get(tipoDivisao);
            List<DiaPersonalizado> diasComExercicios = new ArrayList<>();

            for (DiaPersonalizado dia : diasPadrao) {
                DiaPersonalizado diaComExercicios = new DiaPersonalizado(dia.getNome(), dia.getCategorias());

                // Adicionar exercicios selecionados para cada categoria do dia
                for (String categoria : dia.getCategorias()) {
                    Map<String, List<String>> todasCategorias = new LinkedHashMap<>();
                    todasCategorias.putAll(exerciciosSuperior);
                    todasCategorias.putAll(exerciciosInferior);

                    if (todasCategorias.containsKey(categoria)) {
                        for (String exercicio : todasCategorias.get(categoria)) {
                            if (stateManager.isExercicioCompleto(exercicio)) {
                                diaComExercicios.adicionarExercicio(exercicio);
                            }
                        }
                    }
                }

                if (!diaComExercicios.getExercicios().isEmpty()) {
                    diasComExercicios.add(diaComExercicios);
                }
            }

            return diasComExercicios;
        } else {

            return criarDivisaoPersonalizada();
        }
    }

    private List<DiaPersonalizado> criarDivisaoPersonalizada() {
        List<DiaPersonalizado> dias = new ArrayList<>();
        Map<String, List<String>> todasCategorias = new LinkedHashMap<>();
        todasCategorias.putAll(exerciciosSuperior);
        todasCategorias.putAll(exerciciosInferior);

        // Verificar se tem exercicios de superior e inferior
        boolean temSuperior = false;
        boolean temInferior = false;

        for (Map.Entry<String, List<String>> categoria : exerciciosSuperior.entrySet()) {
            for (String exercicio : categoria.getValue()) {
                if (stateManager.isExercicioCompleto(exercicio)) {
                    temSuperior = true;
                    break;
                }
            }
        }

        for (Map.Entry<String, List<String>> categoria : exerciciosInferior.entrySet()) {
            for (String exercicio : categoria.getValue()) {
                if (stateManager.isExercicioCompleto(exercicio)) {
                    temInferior = true;
                    break;
                }
            }
        }

        if (temSuperior && temInferior) {

            DiaPersonalizado diaUpper = new DiaPersonalizado("Dia A - Membros Superiores", new ArrayList<>());
            DiaPersonalizado diaLower = new DiaPersonalizado("Dia B - Membros Inferiores", new ArrayList<>());


            for (Map.Entry<String, List<String>> categoria : exerciciosSuperior.entrySet()) {
                for (String exercicio : categoria.getValue()) {
                    if (stateManager.isExercicioCompleto(exercicio)) {
                        diaUpper.adicionarExercicio(exercicio);
                    }
                }
            }


            for (Map.Entry<String, List<String>> categoria : exerciciosInferior.entrySet()) {
                for (String exercicio : categoria.getValue()) {
                    if (stateManager.isExercicioCompleto(exercicio)) {
                        diaLower.adicionarExercicio(exercicio);
                    }
                }
            }

            if (!diaUpper.getExercicios().isEmpty()) dias.add(diaUpper);
            if (!diaLower.getExercicios().isEmpty()) dias.add(diaLower);
        } else {
            // Criar um dia Unico
            DiaPersonalizado dia = new DiaPersonalizado("Treino Personalizado", new ArrayList<>());

            for (Map.Entry<String, List<String>> categoria : todasCategorias.entrySet()) {
                for (String exercicio : categoria.getValue()) {
                    if (stateManager.isExercicioCompleto(exercicio)) {
                        dia.adicionarExercicio(exercicio);
                    }
                }
            }

            if (!dia.getExercicios().isEmpty()) {
                dias.add(dia);
            }
        }

        return dias;
    }

    private void adicionarDiasTreinoComSeries(VBox conteudo, List<DiaPersonalizado> diasTreino) {
        for (int i = 0; i < diasTreino.size(); i++) {
            DiaPersonalizado dia = diasTreino.get(i);

            // Cabeçalho
            Label nomeDia = new Label(dia.getNome().toUpperCase());
            nomeDia.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            nomeDia.setStyle("-fx-text-fill: #2196F3; -fx-underline: true;");
            nomeDia.setAlignment(Pos.CENTER);
            nomeDia.setMaxWidth(Double.MAX_VALUE);
            conteudo.getChildren().add(nomeDia);

            // exercicios do dia organizados por categoria
            adicionarExerciciosDoDiaComSeries(conteudo, dia.getExercicios());

            // Separador entre dias (exceto no ultimo)
            if (i < diasTreino.size() - 1) {
                Label separadorDia = new Label("â–¬".repeat(50));
                separadorDia.setFont(Font.font("Arial", 7));
                separadorDia.setStyle("-fx-text-fill: #ccc;");
                separadorDia.setAlignment(Pos.CENTER);
                separadorDia.setMaxWidth(Double.MAX_VALUE);
                conteudo.getChildren().add(separadorDia);
            }
        }
    }

    private void adicionarExerciciosDoDiaComSeries(VBox conteudo, List<String> exerciciosDoDia) {
        // Organizar exercicios por categoria
        Map<String, List<String>> exerciciosPorCategoria = new LinkedHashMap<>();
        Map<String, List<String>> todasCategorias = new LinkedHashMap<>();
        todasCategorias.putAll(exerciciosSuperior);
        todasCategorias.putAll(exerciciosInferior);

        for (String exercicio : exerciciosDoDia) {
            for (Map.Entry<String, List<String>> categoria : todasCategorias.entrySet()) {
                if (categoria.getValue().contains(exercicio)) {
                    exerciciosPorCategoria.computeIfAbsent(categoria.getKey(), k -> new ArrayList<>()).add(exercicio);
                    break;
                }
            }
        }

        // Layout em duas colunas para economizar
        HBox layoutDia = new HBox();
        layoutDia.setSpacing(15);
        VBox colunaEsquerda = new VBox(2);
        VBox colunaDireita = new VBox(2);

        int contadorCategorias = 0;
        for (Map.Entry<String, List<String>> entry : exerciciosPorCategoria.entrySet()) {
            VBox categoriaBox = criarCategoriaComSeries(entry.getKey(), entry.getValue());

            // Alternar entre colunas
            if (contadorCategorias % 2 == 0) {
                colunaEsquerda.getChildren().add(categoriaBox);
            } else {
                colunaDireita.getChildren().add(categoriaBox);
            }
            contadorCategorias++;
        }

        layoutDia.getChildren().addAll(colunaEsquerda, colunaDireita);
        conteudo.getChildren().add(layoutDia);

        
        Label espaco = new Label(" ");
        espaco.setFont(Font.font("Arial", 4));
        conteudo.getChildren().add(espaco);
    }

    private VBox criarCategoriaComSeries(String nomeCategoria, List<String> exercicios) {
        VBox categoriaBox = new VBox(2);

        // Nome da categoria
        Label nomeLabel = new Label(nomeCategoria.toUpperCase());
        nomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        nomeLabel.setStyle("-fx-text-fill: black; -fx-underline: true;");
        categoriaBox.getChildren().add(nomeLabel);

        // exercicios com campos para series
        for (String exercicio : exercicios) {
            HBox exercicioLinha = new HBox(8);
            exercicioLinha.setAlignment(Pos.CENTER_LEFT);

            Label nomeExercicio = new Label("â€¢ " + exercicio);
            nomeExercicio.setFont(Font.font("Arial", 7));
            nomeExercicio.setStyle("-fx-text-fill: black;");
            nomeExercicio.setPrefWidth(140);
            nomeExercicio.setWrapText(true);

            Label campoSeries = new Label("S:___");
            campoSeries.setFont(Font.font("Arial", 7));
            campoSeries.setStyle("-fx-text-fill: #666666;");
            campoSeries.setPrefWidth(35);

            Label campoReps = new Label("R:___");
            campoReps.setFont(Font.font("Arial", 7));
            campoReps.setStyle("-fx-text-fill: #666666;");
            campoReps.setPrefWidth(35);

            Label campoPeso = new Label("P:___");
            campoPeso.setFont(Font.font("Arial", 7));
            campoPeso.setStyle("-fx-text-fill: #666666;");
            campoPeso.setPrefWidth(35);

            exercicioLinha.getChildren().addAll(nomeExercicio, campoSeries, campoReps, campoPeso);
            categoriaBox.getChildren().add(exercicioLinha);
        }

        return categoriaBox;
    }

    private VBox criarConteudoImpressao(String tipo) {
        VBox conteudo = new VBox();
        conteudo.setSpacing(4);
        conteudo.setPadding(new Insets(10));
        conteudo.setStyle("-fx-background-color: white;");

        // Cabeçalho compacto
        Label titulo = new Label("LISTA DE EXERCICIOS SELECIONADOS");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titulo.setStyle("-fx-text-fill: black; -fx-underline: true;");
        titulo.setAlignment(Pos.CENTER);
        titulo.setMaxWidth(Double.MAX_VALUE);

        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"));
        String tipoDivisao = detectarTipoDivisao();
        Label cabecalho = new Label(String.format("Gerado em: %s | divisão: %s", dataHora, tipoDivisao));
        cabecalho.setFont(Font.font("Arial", 9));
        cabecalho.setStyle("-fx-text-fill: #666666;");
        cabecalho.setAlignment(Pos.CENTER);
        cabecalho.setMaxWidth(Double.MAX_VALUE);

        conteudo.getChildren().addAll(titulo, cabecalho);

        // Separador
        Label separador1 = new Label("-".repeat(60));
        separador1.setFont(Font.font("Arial", 7));
        separador1.setStyle("-fx-text-fill: black;");
        separador1.setAlignment(Pos.CENTER);
        separador1.setMaxWidth(Double.MAX_VALUE);
        conteudo.getChildren().add(separador1);

        // Organizar por dias
        List<DiaPersonalizado> diasTreino = organizarExerciciosPorDias();
        adicionarDiasTreinoListagem(conteudo, diasTreino);

        // Separador final
        Label separador2 = new Label("-".repeat(60));
        separador2.setFont(Font.font("Arial", 7));
        separador2.setStyle("-fx-text-fill: black;");
        separador2.setAlignment(Pos.CENTER);
        separador2.setMaxWidth(Double.MAX_VALUE);
        conteudo.getChildren().add(separador2);

        // Resumo
        int totalSelecionados = stateManager.getTotalExerciciosCompletados();
        Label resumo = new Label(String.format("TOTAL: %d exercicios | %s", totalSelecionados, tipoDivisao));
        resumo.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        resumo.setStyle("-fx-text-fill: black;");
        resumo.setAlignment(Pos.CENTER);
        resumo.setMaxWidth(Double.MAX_VALUE);
        conteudo.getChildren().add(resumo);

        return conteudo;
    }

    private void adicionarDiasTreinoListagem(VBox conteudo, List<DiaPersonalizado> diasTreino) {
        for (int i = 0; i < diasTreino.size(); i++) {
            DiaPersonalizado dia = diasTreino.get(i);

            // cabeçalho do dia
            Label nomeDia = new Label(dia.getNome().toUpperCase());
            nomeDia.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            nomeDia.setStyle("-fx-text-fill: #2196F3; -fx-underline: true;");
            conteudo.getChildren().add(nomeDia);

            // exercicios do dia organizados por categoria em formato de lista
            adicionarExerciciosDoDiaListagem(conteudo, dia.getExercicios());

            // Separador entre dias (exceto noUltimo)
            if (i < diasTreino.size() - 1) {
                Label separadorDia = new Label("-".repeat(40));
                separadorDia.setFont(Font.font("Arial", 6));
                separadorDia.setStyle("-fx-text-fill: #ccc;");
                separadorDia.setAlignment(Pos.CENTER);
                separadorDia.setMaxWidth(Double.MAX_VALUE);
                conteudo.getChildren().add(separadorDia);
            }
        }
    }

    private void adicionarExerciciosDoDiaListagem(VBox conteudo, List<String> exerciciosDoDia) {
        // Organizar exercicios por categoria
        Map<String, List<String>> exerciciosPorCategoria = new LinkedHashMap<>();
        Map<String, List<String>> todasCategorias = new LinkedHashMap<>();
        todasCategorias.putAll(exerciciosSuperior);
        todasCategorias.putAll(exerciciosInferior);

        for (String exercicio : exerciciosDoDia) {
            for (Map.Entry<String, List<String>> categoria : todasCategorias.entrySet()) {
                if (categoria.getValue().contains(exercicio)) {
                    exerciciosPorCategoria.computeIfAbsent(categoria.getKey(), k -> new ArrayList<>()).add(exercicio);
                    break;
                }
            }
        }

        // Layout em duas colunas para economizar espaço
        HBox layoutDia = new HBox();
        layoutDia.setSpacing(20);
        VBox colunaEsquerda = new VBox(2);
        VBox colunaDireita = new VBox(2);

        int contadorCategorias = 0;
        for (Map.Entry<String, List<String>> entry : exerciciosPorCategoria.entrySet()) {
            VBox categoriaBox = new VBox(1);

            // Nome da categoria
            Label nomeCategoria = new Label(entry.getKey().toUpperCase());
            nomeCategoria.setFont(Font.font("Arial", FontWeight.BOLD, 8));
            nomeCategoria.setStyle("-fx-text-fill: black; -fx-underline: true;");
            categoriaBox.getChildren().add(nomeCategoria);

            // exercicios em formato compacto
            StringBuilder exerciciosTexto = new StringBuilder();
            for (int i = 0; i < entry.getValue().size(); i++) {
                if (i > 0) exerciciosTexto.append(" â€¢ ");
                exerciciosTexto.append(entry.getValue().get(i));
            }

            Label labelExercicios = new Label(exerciciosTexto.toString());
            labelExercicios.setFont(Font.font("Arial", 7));
            labelExercicios.setStyle("-fx-text-fill: black;");
            labelExercicios.setWrapText(true);
            labelExercicios.setMaxWidth(250);
            categoriaBox.getChildren().add(labelExercicios);

            // Alternar entre colunas
            if (contadorCategorias % 2 == 0) {
                colunaEsquerda.getChildren().add(categoriaBox);
            } else {
                colunaDireita.getChildren().add(categoriaBox);
            }
            contadorCategorias++;
        }

        layoutDia.getChildren().addAll(colunaEsquerda, colunaDireita);
        conteudo.getChildren().add(layoutDia);


        Label espaco = new Label(" ");
        espaco.setFont(Font.font("Arial", 3));
        conteudo.getChildren().add(espaco);
    }

    private void mostrarSucessoImpressao() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Impressão Concluida");
        alert.setHeaderText("exercicios impressos com sucesso!");
        alert.setContentText("Sua ficha de treino foi enviada para a impressora.");
        alert.showAndWait();
    }

    private void mostrarErroImpressao(String erro) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro na Impressão");
        alert.setHeaderText("Não foi possivel imprimir");
        alert.setContentText(erro);
        alert.showAndWait();
    }

    // Classe auxiliar para organizar dias de treino
    private static class DiaPersonalizado {
        private String nome;
        private List<String> categorias;
        private List<String> exercicios;

        public DiaPersonalizado(String nome, List<String> categorias) {
            this.nome = nome;
            this.categorias = new ArrayList<>(categorias);
            this.exercicios = new ArrayList<>();
        }

        public String getNome() { return nome; }
        public List<String> getCategorias() { return categorias; }
        public List<String> getExercicios() { return exercicios; }

        public void adicionarExercicio(String exercicio) {
            if (!exercicios.contains(exercicio)) {
                exercicios.add(exercicio);
            }
        }
    }
}