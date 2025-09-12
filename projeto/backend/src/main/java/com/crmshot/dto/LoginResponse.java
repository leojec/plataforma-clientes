package com.crmshot.dto;

import com.crmshot.entity.Usuario;

public class LoginResponse {
    
    private String token;
    private String tipo = "Bearer";
    private UsuarioInfo usuario;
    
    // Construtores
    public LoginResponse() {}
    
    public LoginResponse(String token, Usuario usuario) {
        this.token = token;
        this.usuario = new UsuarioInfo(usuario);
    }
    
    // Getters e Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public UsuarioInfo getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioInfo usuario) {
        this.usuario = usuario;
    }
    
    // Classe interna para informações do usuário
    public static class UsuarioInfo {
        private Long id;
        private String nome;
        private String email;
        private String perfil;
        private Boolean ativo;
        
        public UsuarioInfo() {}
        
        public UsuarioInfo(Usuario usuario) {
            this.id = usuario.getId();
            this.nome = usuario.getNome();
            this.email = usuario.getEmail();
            this.perfil = usuario.getPerfil().name();
            this.ativo = usuario.getAtivo();
        }
        
        // Getters e Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getNome() {
            return nome;
        }
        
        public void setNome(String nome) {
            this.nome = nome;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPerfil() {
            return perfil;
        }
        
        public void setPerfil(String perfil) {
            this.perfil = perfil;
        }
        
        public Boolean getAtivo() {
            return ativo;
        }
        
        public void setAtivo(Boolean ativo) {
            this.ativo = ativo;
        }
    }
}
