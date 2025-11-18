package com.crmshot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Entity
@Table(name = "interacoes")
public class Interacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expositor_id", nullable = false)
    @JsonBackReference("expositor-interacoes")
    private Expositor expositor;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oportunidade_id")
    private Oportunidade oportunidade;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoInteracao tipo;
    
    @NotBlank
    @Size(max = 200)
    @Column(name = "assunto", nullable = false)
    private String assunto;
    
    @Size(max = 2000)
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "data_interacao", nullable = false)
    private LocalDateTime dataInteracao;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @Column(name = "proxima_acao")
    private String proximaAcao;
    
    @Column(name = "data_proxima_acao")
    private LocalDateTime dataProximaAcao;
    
    @Column(name = "concluida")
    private Boolean concluida = false;
    
    // Campos específicos para PROPOSTA
    @Column(name = "valor_proposta")
    private Double valorProposta;
    
    @Column(name = "metros_quadrados")
    private Double metrosQuadrados;
    
    // Construtores
    public Interacao() {
        this.dataCriacao = LocalDateTime.now();
        this.dataInteracao = LocalDateTime.now();
    }
    
    public Interacao(Expositor expositor, Usuario usuario, TipoInteracao tipo, String assunto, String descricao) {
        this();
        this.expositor = expositor;
        this.usuario = usuario;
        this.tipo = tipo;
        this.assunto = assunto;
        this.descricao = descricao;
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
    
    public Expositor getExpositor() {
        return expositor;
    }
    
    public void setExpositor(Expositor expositor) {
        this.expositor = expositor;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Oportunidade getOportunidade() {
        return oportunidade;
    }
    
    public void setOportunidade(Oportunidade oportunidade) {
        this.oportunidade = oportunidade;
    }
    
    public TipoInteracao getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoInteracao tipo) {
        this.tipo = tipo;
    }
    
    public String getAssunto() {
        return assunto;
    }
    
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public LocalDateTime getDataInteracao() {
        return dataInteracao;
    }
    
    public void setDataInteracao(LocalDateTime dataInteracao) {
        this.dataInteracao = dataInteracao;
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
    
    public String getProximaAcao() {
        return proximaAcao;
    }
    
    public void setProximaAcao(String proximaAcao) {
        this.proximaAcao = proximaAcao;
    }
    
    public LocalDateTime getDataProximaAcao() {
        return dataProximaAcao;
    }
    
    public void setDataProximaAcao(LocalDateTime dataProximaAcao) {
        this.dataProximaAcao = dataProximaAcao;
    }
    
    public Boolean getConcluida() {
        return concluida;
    }
    
    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }
    
    public Double getValorProposta() {
        return valorProposta;
    }
    
    public void setValorProposta(Double valorProposta) {
        this.valorProposta = valorProposta;
    }
    
    public Double getMetrosQuadrados() {
        return metrosQuadrados;
    }
    
    public void setMetrosQuadrados(Double metrosQuadrados) {
        this.metrosQuadrados = metrosQuadrados;
    }
    
    public enum TipoInteracao {
        LIGACAO,
        EMAIL,
        REUNIAO,
        VISITA,
        WHATSAPP,
        PROPOSTA,
        FECHADO,
        OUTROS
    }
}
