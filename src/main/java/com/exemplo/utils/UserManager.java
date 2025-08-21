package com.exemplo.utils;

import java.util.*;

public class UserManager {

    private static UserManager instance;
    private Map<String, Usuario> usuarios;
    private Usuario usuarioLogado;

    private UserManager() {
        usuarios = new HashMap<>();
        inicializarUsuariosDefault();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private void inicializarUsuariosDefault() {
        // Criar alguns usuario de exemplo para teste
        usuarios.put("admin@exemplo.com", new Usuario("Administrador", "Sistema", "admin@exemplo.com", "123456", "Treinador"));
        
        //testes
        System.out.println("usuario default criados:");
        System.out.println("- admin@exemplo.com / 123456 (Treinador)");
    }

    public boolean realizarLogin(String email, String senha, String tipoEsperado) {
        Usuario usuario = usuarios.get(email.toLowerCase());

        if (usuario != null && usuario.getSenha().equals(senha) && usuario.getTipo().equals(tipoEsperado)) {
            usuarioLogado = usuario;
            System.out.println("Login realizado com sucesso: " + usuario.getNomeCompleto() + " (" + usuario.getTipo() + ")");
            return true;
        }

        System.out.println("Falha no login: " + email);
        return false;
    }

    public boolean realizarCadastro(String primeiroNome, String sobrenome, String email, String senha, String tipo) {
        email = email.toLowerCase();

        // Verificar se email já existe
        if (usuarios.containsKey(email)) {
            System.out.println("Email já cadastrado: " + email);
            return false;
        }

        // Criar novo usuario
        Usuario novoUsuario = new Usuario(primeiroNome, sobrenome, email, senha, tipo);
        usuarios.put(email, novoUsuario);

        System.out.println("usuario cadastrado com sucesso: " + novoUsuario.getNomeCompleto() + " (" + tipo + ")");
        return true;
    }

    public void realizarLogout() {
        if (usuarioLogado != null) {
            System.out.println("Logout realizado: " + usuarioLogado.getNomeCompleto());
            usuarioLogado = null;
        }
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }

    public String getNomeUsuarioLogado() {
        return usuarioLogado != null ? usuarioLogado.getPrimeiroNome() : "usuario";
    }

    public String getTipoUsuarioLogado() {
        return usuarioLogado != null ? usuarioLogado.getTipo() : "N/A";
    }



    // Classe interna para representar um usuario
    public static class Usuario {
        private String primeiroNome;
        private String sobrenome;
        private String email;
        private String senha;
        private String tipo;
        private Date dataCadastro;

        public Usuario(String primeiroNome, String sobrenome, String email, String senha, String tipo) {
            this.primeiroNome = primeiroNome;
            this.sobrenome = sobrenome;
            this.email = email;
            this.senha = senha;
            this.tipo = tipo;
            this.dataCadastro = new Date();
        }

        // Getters
        public String getPrimeiroNome() { return primeiroNome; }
        public String getSobrenome() { return sobrenome; }
        public String getEmail() { return email; }
        public String getSenha() { return senha; }
        public String getTipo() { return tipo; }
        public Date getDataCadastro() { return dataCadastro; }

        public String getNomeCompleto() {
            return primeiroNome + " " + sobrenome;
        }

        // Setters
        public void setPrimeiroNome(String primeiroNome) { this.primeiroNome = primeiroNome; }
        public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }
        public void setSenha(String senha) { this.senha = senha; }
        public void setTipo(String tipo) { this.tipo = tipo; }

        @Override
        public String toString() {
            return "Usuario{" +
                    "nome='" + getNomeCompleto() + '\'' +
                    ", email='" + email + '\'' +
                    ", tipo='" + tipo + '\'' +
                    ", dataCadastro=" + dataCadastro +
                    '}';
        }
    }
}