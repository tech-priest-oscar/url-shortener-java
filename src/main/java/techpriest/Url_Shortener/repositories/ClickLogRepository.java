package techpriest.Url_Shortener.repositories;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import techpriest.Url_Shortener.models.ClickLog;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, UUID> {
    public List<ClickLog> findByUrlId(UUID urlId);

    @Override
    @EntityGraph(attributePaths = "url")
    Page<ClickLog> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = "url")
    @Query("SELECT c FROM ClickLog c WHERE c.url.id = :urlId")
    Page<ClickLog> findByUrlIdOrderByCreatedAtDesc(@Param("urlId") UUID urlId, Pageable pageable);

}
