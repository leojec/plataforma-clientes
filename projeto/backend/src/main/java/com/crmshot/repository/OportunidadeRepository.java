package com.crmshot.repository;

import com.crmshot.entity.Oportunidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    
    List<Oportunidade> findByExpositorId(Long expositorId);
    
    List<Oportunidade> findByVendedorId(Long vendedorId);
    
    List<Oportunidade> findByStatus(Oportunidade.StatusOportunidade status);
    
    Page<Oportunidade> findByStatus(Oportunidade.StatusOportunidade status, Pageable pageable);
    
    @Query("SELECT o FROM Oportunidade o WHERE " +
           "(:titulo IS NULL OR LOWER(o.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:vendedorId IS NULL OR o.vendedor.id = :vendedorId) AND " +
           "(:expositorId IS NULL OR o.expositor.id = :expositorId)")
    Page<Oportunidade> findByFiltros(@Param("titulo") String titulo,
                                    @Param("status") Oportunidade.StatusOportunidade status,
                                    @Param("vendedorId") Long vendedorId,
                                    @Param("expositorId") Long expositorId,
                                    Pageable pageable);
    
    @Query("SELECT COUNT(o) FROM Oportunidade o WHERE o.status = :status")
    Long countByStatus(@Param("status") Oportunidade.StatusOportunidade status);
    
    @Query("SELECT COUNT(o) FROM Oportunidade o WHERE o.vendedor.id = :vendedorId")
    Long countByVendedor(@Param("vendedorId") Long vendedorId);
    
    @Query("SELECT SUM(o.valorEstimado) FROM Oportunidade o WHERE o.status = :status")
    BigDecimal sumValorByStatus(@Param("status") Oportunidade.StatusOportunidade status);
    
    @Query("SELECT SUM(o.valorEstimado) FROM Oportunidade o WHERE o.vendedor.id = :vendedorId AND o.status = :status")
    BigDecimal sumValorByVendedorAndStatus(@Param("vendedorId") Long vendedorId, 
                                          @Param("status") Oportunidade.StatusOportunidade status);
    
    @Query("SELECT o FROM Oportunidade o WHERE o.dataPrevistaFechamento BETWEEN :dataInicio AND :dataFim")
    List<Oportunidade> findByDataPrevistaFechamentoBetween(@Param("dataInicio") LocalDateTime dataInicio,
                                                          @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT o FROM Oportunidade o WHERE o.vendedor.id = :vendedorId AND o.dataPrevistaFechamento BETWEEN :dataInicio AND :dataFim")
    List<Oportunidade> findByVendedorAndDataPrevistaFechamentoBetween(@Param("vendedorId") Long vendedorId,
                                                                     @Param("dataInicio") LocalDateTime dataInicio,
                                                                     @Param("dataFim") LocalDateTime dataFim);
    
    // Métodos para dashboard
    Long countByStatus(String status);
    
    @Query("SELECT SUM(o.valorEstimado) FROM Oportunidade o WHERE o.status = :status")
    BigDecimal sumValorEstimadoByStatus(@Param("status") String status);
    
    @Query("SELECT SUM(o.valorEstimado) FROM Oportunidade o WHERE o.status IN :statusList")
    BigDecimal sumValorEstimadoByStatusIn(@Param("statusList") List<String> statusList);
}
