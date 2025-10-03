package com.crmshot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidades")
public class Oportunidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "titulo", nullable = false)
    private String titulo;
    
    @Size(max = 1000)
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expositor_id", nullable = false)
    @JsonBackReference("expositor-oportunidades")
    private Expositor expositor;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOportunidade status = StatusOportunidade.PROSPECCAO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "fonte", nullable = false)
    private FonteOportunidade fonte;
    
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "valor_estimado", precision = 15, scale = 2)
    private BigDecimal valorEstimado;
    
    @Column(name = "probabilidade_fechamento")
    private Integer probabilidadeFechamento = 0;
    
    @Column(name = "data_prevista_fechamento")
    private LocalDateTime dataPrevistaFechamento;
    
    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Size(max = 1000)
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    // Construtores
    public Oportunidade() {
        this.dataCriacao = LocalDateTime.now();
    }
    
    public Oportunidade(String titulo, Expositor expositor, Usuario vendedor, FonteOportunidade fonte) {
        this();
        this.titulo = titulo;
        this.expositor = expositor;
        this.vendedor = vendedor;
        this.fonte = fonte;
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
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Expositor getExpositor() {
        return expositor;
    }
    
    public void setExpositor(Expositor expositor) {
        this.expositor = expositor;
    }
    
    public Usuario getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }
    
    public StatusOportunidade getStatus() {
        return status;
    }
    
    public void setStatus(StatusOportunidade status) {
        this.status = status;
    }
    
    public FonteOportunidade getFonte() {
        return fonte;
    }
    
    public void setFonte(FonteOportunidade fonte) {
        this.fonte = fonte;
    }
    
    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }
    
    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }
    
    public Integer getProbabilidadeFechamento() {
        return probabilidadeFechamento;
    }
    
    public void setProbabilidadeFechamento(Integer probabilidadeFechamento) {
        this.probabilidadeFechamento = probabilidadeFechamento;
    }
    
    public LocalDateTime getDataPrevistaFechamento() {
        return dataPrevistaFechamento;
    }
    
    public void setDataPrevistaFechamento(LocalDateTime dataPrevistaFechamento) {
        this.dataPrevistaFechamento = dataPrevistaFechamento;
    }
    
    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }
    
    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public enum StatusOportunidade {
        PROSPECCAO,
        QUALIFICACAO,
        PROPOSTA,
        NEGOCIACAO,
        FECHADA_GANHA,
        FECHADA_PERDIDA,
        CANCELADA
    }
    
    public enum FonteOportunidade {
        INDICACAO,
        SITE,
        REDE_SOCIAL,
        TELEFONE,
        EMAIL,
        EVENTO,
        OUTROS
    }
}
