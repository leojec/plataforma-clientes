package com.crmshot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "expositores")
public class Expositor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;
    
    @Size(max = 200)
    @Column(name = "nome_fantasia")
    private String nomeFantasia;
    
    @NotBlank
    @Size(max = 18)
    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;
    
    @Email
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    
    @Size(max = 20)
    @Column(name = "telefone")
    private String telefone;
    
    @Size(max = 20)
    @Column(name = "celular")
    private String celular;
    
    @Size(max = 200)
    @Column(name = "endereco")
    private String endereco;
    
    @Size(max = 100)
    @Column(name = "cidade")
    private String cidade;
    
    @Size(max = 2)
    @Column(name = "estado")
    private String estado;
    
    @Size(max = 10)
    @Column(name = "cep")
    private String cep;
    
    @Size(max = 200)
    @Column(name = "site")
    private String site;
    
    @Size(max = 1000)
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusExpositor status = StatusExpositor.ATIVO;
    
    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario vendedor;
    
    @OneToMany(mappedBy = "expositor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("expositor-oportunidades")
    private List<Oportunidade> oportunidades;
    
    @OneToMany(mappedBy = "expositor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("expositor-interacoes")
    private List<Interacao> interacoes;
    
    // Construtores
    public Expositor() {
        this.dataCadastro = LocalDateTime.now();
    }
    
    public Expositor(String razaoSocial, String cnpj, String email) {
        this();
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.email = email;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRazaoSocial() {
        return razaoSocial;
    }
    
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    
    public String getNomeFantasia() {
        return nomeFantasia;
    }
    
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getCelular() {
        return celular;
    }
    
    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCep() {
        return cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }
    
    public String getSite() {
        return site;
    }
    
    public void setSite(String site) {
        this.site = site;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public StatusExpositor getStatus() {
        return status;
    }
    
    public void setStatus(StatusExpositor status) {
        this.status = status;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    public Usuario getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }
    
    public List<Oportunidade> getOportunidades() {
        return oportunidades;
    }
    
    public void setOportunidades(List<Oportunidade> oportunidades) {
        this.oportunidades = oportunidades;
    }
    
    public List<Interacao> getInteracoes() {
        return interacoes;
    }
    
    public void setInteracoes(List<Interacao> interacoes) {
        this.interacoes = interacoes;
    }
    
    public enum StatusExpositor {
        ATIVO,
        INATIVO,
        BLOQUEADO,
        POTENCIAL
    }
}
