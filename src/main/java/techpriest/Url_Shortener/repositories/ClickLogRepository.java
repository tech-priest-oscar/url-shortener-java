package techpriest.Url_Shortener.repositories;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techpriest.Url_Shortener.models.ClickLog;

@Repository
public interface ClickLogRepository extends JpaRepository<ClickLog, UUID> {

    public List<ClickLog> findByUrlId(UUID urlId);
    
}
