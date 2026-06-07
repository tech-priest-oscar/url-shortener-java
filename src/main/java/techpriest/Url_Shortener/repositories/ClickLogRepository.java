package techpriest.Url_Shortener.repositories;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techpriest.Url_Shortener.models.ClickLog;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, UUID> {
    public List<ClickLog> findByUrlId(UUID urlId);

    @Override
    @EntityGraph(attributePaths = "url")
    Page<ClickLog> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "url")
    Page<ClickLog> findByUrlIdOrderByCreatedAtDesc(UUID urlId, Pageable pageable);

}
