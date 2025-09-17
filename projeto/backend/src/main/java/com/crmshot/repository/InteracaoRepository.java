package com.crmshot.repository;

import com.crmshot.entity.Interacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InteracaoRepository extends JpaRepository<Interacao, Long> {
    
    List<Interacao> findByExpositorId(Long expositorId);
    
    List<Interacao> findByOportunidadeId(Long oportunidadeId);
    
    List<Interacao> findByUsuarioId(Long usuarioId);
    
    List<Interacao> findByTipo(Interacao.TipoInteracao tipo);
    
    Page<Interacao> findByExpositorId(Long expositorId, Pageable pageable);
    
    @Query("SELECT i FROM Interacao i WHERE " +
           "(:expositorId IS NULL OR i.expositor.id = :expositorId) AND " +
           "(:oportunidadeId IS NULL OR i.oportunidade.id = :oportunidadeId) AND " +
           "(:usuarioId IS NULL OR i.usuario.id = :usuarioId) AND " +
           "(:tipo IS NULL OR i.tipo = :tipo) AND " +
           "(:dataInicio IS NULL OR i.dataInteracao >= :dataInicio) AND " +
           "(:dataFim IS NULL OR i.dataInteracao <= :dataFim)")
    Page<Interacao> findByFiltros(@Param("expositorId") Long expositorId,
                                 @Param("oportunidadeId") Long oportunidadeId,
                                 @Param("usuarioId") Long usuarioId,
                                 @Param("tipo") Interacao.TipoInteracao tipo,
                                 @Param("dataInicio") LocalDateTime dataInicio,
                                 @Param("dataFim") LocalDateTime dataFim,
                                 Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM Interacao i WHERE i.expositor.id = :expositorId")
    Long countByExpositor(@Param("expositorId") Long expositorId);
    
    @Query("SELECT COUNT(i) FROM Interacao i WHERE i.usuario.id = :usuarioId")
    Long countByUsuario(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT i FROM Interacao i WHERE i.dataProximaAcao IS NOT NULL AND i.dataProximaAcao <= :dataLimite AND i.concluida = false")
    List<Interacao> findProximasAcoes(@Param("dataLimite") LocalDateTime dataLimite);
    
    @Query("SELECT i FROM Interacao i WHERE i.usuario.id = :usuarioId AND i.dataProximaAcao IS NOT NULL AND i.dataProximaAcao <= :dataLimite AND i.concluida = false")
    List<Interacao> findProximasAcoesByUsuario(@Param("usuarioId") Long usuarioId, @Param("dataLimite") LocalDateTime dataLimite);
    
    // Método para contar interações criadas após uma data
    Long countByDataCriacaoAfter(LocalDateTime data);
    
    // Método para buscar atividades agrupadas por usuário e data para o gráfico
    @Query("SELECT DATE(i.dataCriacao) as data, u.nome as usuario, COUNT(i) as quantidade " +
           "FROM Interacao i JOIN i.usuario u " +
           "WHERE i.dataCriacao >= :dataInicio AND i.dataCriacao <= :dataFim " +
           "GROUP BY DATE(i.dataCriacao), u.id, u.nome " +
           "ORDER BY DATE(i.dataCriacao)")
    List<Object[]> findAtividadesPorUsuarioEData(@Param("dataInicio") LocalDateTime dataInicio, 
                                                 @Param("dataFim") LocalDateTime dataFim);
}
