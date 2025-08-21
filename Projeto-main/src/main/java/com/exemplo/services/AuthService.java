package com.exemplo.services;

import com.exemplo.models.Usuario;
import com.exemplo.models.TipoUsuario;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class AuthService {
    private static AuthService instance;
    private Map<String, Usuario> usuariosCache; // Cache em memória para performance
    private Usuario usuarioLogado;
    private DatabaseService databaseService;

    private AuthService() {
        databaseService = DatabaseService.getInstance();
        usuariosCache = new HashMap<>();
        carregarUsuarios();

        // Criar usuários padrão apenas se não existirem usuários
        if (usuariosCache.isEmpty()) {
            criarUsuariosPadrao();
        }
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    private void carregarUsuarios() {
        try {
            List<Usuario> usuariosCarregados = databaseService.carregarUsuarios();
            usuariosCache.clear();

            for (Usuario usuario : usuariosCarregados) {
                usuariosCache.put(usuario.getEmail().toLowerCase(), usuario);
            }

            System.out.println("Cache de usuários atualizado: " + usuariosCache.size() + " usuários carregados.");

        } catch (Exception e) {
            System.err.println("Erro ao carregar usuários: " + e.getMessage());
            usuariosCache = new HashMap<>();
        }
    }

    private void salvarUsuarios() {
        try {
            List<Usuario> listaUsuarios = usuariosCache.values().stream().toList();
            boolean sucesso = databaseService.salvarUsuarios(listaUsuarios);

            if (!sucesso) {
                System.err.println("Falha ao salvar usuários no arquivo!");
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    private void criarUsuariosPadrao() {
        System.out.println("Criando usuários padrão...");

        // Treinador padrão
        try {
            Usuario treinador = new Usuario("João", "Silva", "treinador@gym.com",
                    criptografarSenha("123456"), TipoUsuario.TREINADOR);

            if (databaseService.adicionarUsuario(treinador)) {
                usuariosCache.put(treinador.getEmail().toLowerCase(), treinador);
                System.out.println("Treinador padrão criado: " + treinador.getEmail());
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar treinador padrão: " + e.getMessage());
        }

        // Cliente padrão
        try {
            Usuario cliente = new Usuario("Maria", "Santos", "cliente@email.com",
                    criptografarSenha("123456"), TipoUsuario.CLIENTE);

            if (databaseService.adicionarUsuario(cliente)) {
                usuariosCache.put(cliente.getEmail().toLowerCase(), cliente);
                System.out.println("Cliente padrão criado: " + cliente.getEmail());
            }
        } catch (Exception e) {
            System.err.println("Erro ao criar cliente padrão: " + e.getMessage());
        }
    }

    public boolean cadastrarUsuario(String primeiroNome, String sobrenome, String email,
                                    String senha, TipoUsuario tipo) {

        // Validar dados
        if (!validarDadosCadastro(primeiroNome, sobrenome, email, senha)) {
            return false;
        }

        String emailLower = email.toLowerCase().trim();

        // Verificar se email já existe no cache
        if (usuariosCache.containsKey(emailLower)) {
            throw new RuntimeException("Email já cadastrado!");
        }

        // Verificar se email já existe no banco (dupla verificação)
        Usuario usuarioExistente = databaseService.buscarUsuarioPorEmail(emailLower);
        if (usuarioExistente != null) {
            throw new RuntimeException("Email já cadastrado!");
        }

        try {
            // Criar novo usuário
            Usuario novoUsuario = new Usuario(
                    primeiroNome.trim(),
                    sobrenome.trim(),
                    emailLower,
                    criptografarSenha(senha),
                    tipo
            );

            // Tentar salvar no banco
            boolean salvou = databaseService.adicionarUsuario(novoUsuario);

            if (salvou) {
                // Adicionar ao cache se salvo com sucesso
                usuariosCache.put(emailLower, novoUsuario);
                System.out.println("Usuário cadastrado com sucesso: " + novoUsuario);
                return true;
            } else {
                throw new RuntimeException("Erro ao salvar usuário no banco de dados!");
            }

        } catch (Exception e) {
            System.err.println("Erro no cadastro: " + e.getMessage());
            throw new RuntimeException("Erro interno no cadastro: " + e.getMessage());
        }
    }

    public boolean autenticar(String email, String senha) {
        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            return false;
        }

        String emailLower = email.toLowerCase().trim();

        // Primeiro tentar no cache
        Usuario usuario = usuariosCache.get(emailLower);

        // Se não estiver no cache, buscar no banco
        if (usuario == null) {
            usuario = databaseService.buscarUsuarioPorEmail(emailLower);
            if (usuario != null) {
                // Adicionar ao cache para próximas consultas
                usuariosCache.put(emailLower, usuario);
            }
        }

        if (usuario != null && usuario.isAtivo()) {
            String senhaHash = criptografarSenha(senha);
            if (senhaHash.equals(usuario.getSenha())) {
                usuarioLogado = usuario;
                System.out.println("Login realizado: " + usuario.getNomeCompleto() + " (" + usuario.getTipo() + ")");
                return true;
            }
        }

        System.out.println("Falha na autenticação para: " + emailLower);
        return false;
    }

    public void logout() {
        if (usuarioLogado != null) {
            System.out.println("Logout: " + usuarioLogado.getNomeCompleto());
            usuarioLogado = null;
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        String emailLower = email.toLowerCase();

        // Primeiro tentar no cache
        Usuario usuario = usuariosCache.get(emailLower);

        // Se não estiver no cache, buscar no banco
        if (usuario == null) {
            usuario = databaseService.buscarUsuarioPorEmail(emailLower);
            if (usuario != null) {
                usuariosCache.put(emailLower, usuario);
            }
        }

        return Optional.ofNullable(usuario);
    }

    // Método para atualizar dados do usuário
    public boolean atualizarUsuario(Usuario usuarioAtualizado) {
        try {
            boolean sucesso = databaseService.atualizarUsuario(usuarioAtualizado);

            if (sucesso) {
                // Atualizar cache
                usuariosCache.put(usuarioAtualizado.getEmail().toLowerCase(), usuarioAtualizado);

                // Se for o usuário logado, atualizar referência
                if (usuarioLogado != null && usuarioLogado.getId().equals(usuarioAtualizado.getId())) {
                    usuarioLogado = usuarioAtualizado;
                }
            }

            return sucesso;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    // Método para recarregar dados do banco
    public void recarregarDados() {
        carregarUsuarios();
    }

    private boolean validarDadosCadastro(String primeiroNome, String sobrenome, String email, String senha) {
        // Validar nome
        if (primeiroNome == null || primeiroNome.trim().length() < 2) {
            throw new RuntimeException("Primeiro nome deve ter pelo menos 2 caracteres!");
        }

        if (sobrenome == null || sobrenome.trim().length() < 2) {
            throw new RuntimeException("Sobrenome deve ter pelo menos 2 caracteres!");
        }

        // Validar email
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Email inválido!");
        }

        // Validar senha
        if (senha == null || senha.length() < 6) {
            throw new RuntimeException("Senha deve ter pelo menos 6 caracteres!");
        }

        return true;
    }

    private String criptografarSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar senha", e);
        }
    }

    // Métodos para estatísticas
    public int getTotalUsuarios() {
        return databaseService.getTotalUsuarios();
    }

    public int getTotalUsuariosPorTipo(TipoUsuario tipo) {
        return databaseService.getTotalUsuariosPorTipo(tipo);
    }

    // Métodos administrativos
    public boolean criarBackup() {
        return databaseService.criarBackup();
    }

    public boolean limparTodosOsDados() {
        boolean sucesso = databaseService.limparTodosOsDados();
        if (sucesso) {
            usuariosCache.clear();
            usuarioLogado = null;
        }
        return sucesso;
    }


    public void listarUsuarios() {
        System.out.println("=== USUÁRIOS CADASTRADOS ===");
        for (Usuario usuario : usuariosCache.values()) {
            System.out.println(usuario);
        }
        System.out.println("Total: " + usuariosCache.size() + " usuários");
    }
}