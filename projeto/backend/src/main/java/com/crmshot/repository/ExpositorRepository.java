package com.crmshot.repository;

import com.crmshot.entity.Expositor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpositorRepository extends JpaRepository<Expositor, Long> {

    Optional<Expositor> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

    List<Expositor> findByStatus(Expositor.StatusExpositor status);

    List<Expositor> findByVendedorId(Long vendedorId);

    Page<Expositor> findByStatus(Expositor.StatusExpositor status, Pageable pageable);

    @Query("SELECT e FROM Expositor e WHERE " +
           "(:nome IS NULL OR LOWER(e.razaoSocial) LIKE LOWER(CONCAT('%', :nome, '%')) OR " +
           "LOWER(e.nomeFantasia) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:vendedorId IS NULL OR e.vendedor.id = :vendedorId)")
    Page<Expositor> findByFiltros(@Param("nome") String nome,
                                  @Param("status") Expositor.StatusExpositor status,
                                  @Param("vendedorId") Long vendedorId,
                                  Pageable pageable);

    @Query("SELECT COUNT(e) FROM Expositor e WHERE e.status = :status")
    Long countByStatus(@Param("status") Expositor.StatusExpositor status);

    @Query("SELECT COUNT(e) FROM Expositor e WHERE e.vendedor.id = :vendedorId")
    Long countByVendedor(@Param("vendedorId") Long vendedorId);

    @Query("SELECT e FROM Expositor e WHERE e.vendedor.id = :vendedorId AND e.status = :status")
    List<Expositor> findByVendedorAndStatus(@Param("vendedorId") Long vendedorId,
                                           @Param("status") Expositor.StatusExpositor status);


    Long countByDataCadastroAfter(LocalDateTime data);
}
