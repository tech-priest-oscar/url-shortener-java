package techpriest.Url_Shortener.repositories;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import techpriest.Url_Shortener.models.ClickLog;

public interface ClickLogRepository extends JpaRepository<ClickLog, UUID> {
    public List<ClickLog> findByUrlId(UUID urlId);

    @Override
    @Query(value = "SELECT c FROM ClickLog c JOIN FETCH c.url",
           countQuery = "SELECT count(c) FROM ClickLog c")
    Page<ClickLog> findAll(@NonNull Pageable pageable);

    @Query(value = "SELECT c FROM ClickLog c JOIN FETCH c.url WHERE c.url.id = :urlId ORDER BY c.createdAt DESC",
           countQuery = "SELECT count(c) FROM ClickLog c WHERE c.url.id = :urlId")
    Page<ClickLog> findByUrlIdOrderByCreatedAtDesc(@Param("urlId") UUID urlId, Pageable pageable);

}
