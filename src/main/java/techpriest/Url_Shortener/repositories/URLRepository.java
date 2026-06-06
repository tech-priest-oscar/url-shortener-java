package techpriest.Url_Shortener.repositories;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techpriest.Url_Shortener.models.Url;


@Repository
public interface URLRepository extends JpaRepository<Url, UUID> {

    Page<Url> findByOriginalUrlContainingIgnoreCaseOrShortCodeContainingIgnoreCase(
            String originalUrl, String shortCode, Pageable pageable);
}
