package com.exemplo.services;

import com.exemplo.models.Usuario;
import com.exemplo.models.TipoUsuario;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static DatabaseService instance;
    private final Gson gson;
    private final String DATA_DIR = "data";
    private final String USUARIOS_FILE = "usuarios.json";
    private final Path dataPath;
    private final Path usuariosFilePath;

    private DatabaseService() {
        // Configurar Gson com suporte para LocalDateTime
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();

        // Configurar caminhos dos arquivos
        dataPath = Paths.get(DATA_DIR);
        usuariosFilePath = dataPath.resolve(USUARIOS_FILE);

        // Criar diretório se não existir
        criarDiretorioSeNaoExistir();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void criarDiretorioSeNaoExistir() {
        try {
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                System.out.println("Diretório 'data' criado com sucesso.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    // Salvar lista de usuários
    public boolean salvarUsuarios(List<Usuario> usuarios) {
        try (FileWriter writer = new FileWriter(usuariosFilePath.toFile(), StandardCharsets.UTF_8)) {
            gson.toJson(usuarios, writer);
            System.out.println("Usuários salvos com sucesso em: " + usuariosFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
            return false;
        }
    }

    // Carregar lista de usuários
    public List<Usuario> carregarUsuarios() {
        if (!Files.exists(usuariosFilePath)) {
            System.out.println("Arquivo de usuários não encontrado. Criando lista vazia.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(usuariosFilePath.toFile(), StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<Usuario>>(){}.getType();
            List<Usuario> usuarios = gson.fromJson(reader, listType);

            if (usuarios == null) {
                usuarios = new ArrayList<>();
            }

            System.out.println("Carregados " + usuarios.size() + " usuários do arquivo.");
            return usuarios;

        } catch (IOException e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro ao processar dados dos usuários: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Salvar um único usuário (adicionar à lista existente)
    public boolean adicionarUsuario(Usuario novoUsuario) {
        List<Usuario> usuarios = carregarUsuarios();

        // Verificar se já existe usuário com mesmo email
        boolean usuarioExiste = usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(novoUsuario.getEmail()));

        if (usuarioExiste) {
            System.err.println("Usuário com email " + novoUsuario.getEmail() + " já existe!");
            return false;
        }

        // Definir próximo ID
        Long proximoId = usuarios.stream()
                .mapToLong(Usuario::getId)
                .max()
                .orElse(0L) + 1;

        novoUsuario.setId(proximoId);
        usuarios.add(novoUsuario);

        return salvarUsuarios(usuarios);
    }

    // Buscar usuário por email
    public Usuario buscarUsuarioPorEmail(String email) {
        List<Usuario> usuarios = carregarUsuarios();
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Atualizar usuário
    public boolean atualizarUsuario(Usuario usuarioAtualizado) {
        List<Usuario> usuarios = carregarUsuarios();

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(usuarioAtualizado.getId())) {
                usuarios.set(i, usuarioAtualizado);
                return salvarUsuarios(usuarios);
            }
        }

        System.err.println("Usuário com ID " + usuarioAtualizado.getId() + " não encontrado!");
        return false;
    }

    // Remover usuário
    public boolean removerUsuario(Long id) {
        List<Usuario> usuarios = carregarUsuarios();

        boolean removido = usuarios.removeIf(u -> u.getId().equals(id));

        if (removido) {
            return salvarUsuarios(usuarios);
        } else {
            System.err.println("Usuário com ID " + id + " não encontrado!");
            return false;
        }
    }

    // Obter estatísticas
    public int getTotalUsuarios() {
        return carregarUsuarios().size();
    }

    public int getTotalUsuariosPorTipo(TipoUsuario tipo) {
        return (int) carregarUsuarios().stream()
                .filter(u -> u.getTipo() == tipo)
                .count();
    }

    // Limpar todos os dados (para testes)
    public boolean limparTodosOsDados() {
        try {
            if (Files.exists(usuariosFilePath)) {
                Files.delete(usuariosFilePath);
                System.out.println("Arquivo de usuários removido com sucesso.");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao remover arquivo de usuários: " + e.getMessage());
            return false;
        }
    }

    // Verificar se arquivo existe
    public boolean arquivoExiste() {
        return Files.exists(usuariosFilePath);
    }

    // Backup dos dados
    public boolean criarBackup() {
        if (!Files.exists(usuariosFilePath)) {
            System.err.println("Nenhum arquivo de dados para fazer backup.");
            return false;
        }

        try {
            String timestamp = LocalDateTime.now().toString().replaceAll("[:.\\-T]", "");
            Path backupPath = dataPath.resolve("backup_usuarios_" + timestamp + ".json");

            Files.copy(usuariosFilePath, backupPath);
            System.out.println("Backup criado: " + backupPath);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao criar backup: " + e.getMessage());
            return false;
        }
    }
}