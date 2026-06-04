package techpriest.Url_Shortener.repositories;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techpriest.Url_Shortener.models.Url;


@Repository
public interface URLRepository extends JpaRepository<Url, UUID> {
    
}
