package com.exemplo.utils;

import java.util.*;

public class ExercicioStateManager {

    private static ExercicioStateManager instance;
    private Set<String> exerciciosCompletados;

    private ExercicioStateManager() {
        exerciciosCompletados = new HashSet<>();
    }

    public static ExercicioStateManager getInstance() {
        if (instance == null) {
            instance = new ExercicioStateManager();
        }
        return instance;
    }

    public void marcarExercicioCompleto(String nomeExercicio) {
        exerciciosCompletados.add(nomeExercicio);
        System.out.println("Estado salvo - Exercicios completados: " + exerciciosCompletados.size());
    }

    public void desmarcarExercicio(String nomeExercicio) {
        exerciciosCompletados.remove(nomeExercicio);
        System.out.println("Estado salvo - Exercicios completados: " + exerciciosCompletados.size());
    }

    public boolean isExercicioCompleto(String nomeExercicio) {
        return exerciciosCompletados.contains(nomeExercicio);
    }

    public Set<String> getExerciciosCompletados() {
        return new HashSet<>(exerciciosCompletados);
    }

    public int getTotalExerciciosCompletados() {
        return exerciciosCompletados.size();
    }

    public void limparTodasSelecoes() {
        exerciciosCompletados.clear();
        System.out.println("Todos os Exercicios foram desmarcados");
    }

    public void limparTodos() {
        limparTodasSelecoes();
    }


    public int getExerciciosCompletadosPorCategoria(String categoria, Set<String> exerciciosCategoria) {
        int count = 0;
        for (String exercicio : exerciciosCategoria) {
            if (exerciciosCompletados.contains(exercicio)) {
                count++;
            }
        }
        return count;
    }
}