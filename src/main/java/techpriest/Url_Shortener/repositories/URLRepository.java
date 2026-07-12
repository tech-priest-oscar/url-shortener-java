package techpriest.Url_Shortener.repositories;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import techpriest.Url_Shortener.dto.proxy.UrlProxy;
import techpriest.Url_Shortener.models.Url;


public interface URLRepository extends JpaRepository<Url, UUID> {

    Page<Url> findByOriginalUrlContainingIgnoreCaseOrShortCodeContainingIgnoreCase(
            String originalUrl, String shortCode, Pageable pageable);

    Optional<Url> findByShortCode(String shortCode);

    @Query("SELECT new techpriest.Url_Shortener.dto.proxy.UrlProxy(u.originalUrl," +
            "u.clickCount,u.lastClickedAt," +
            "u.shortCode,us.id) FROM Url u JOIN u.user us WHERE u.shortCode = :shortCode")
    Optional<UrlProxy> findByShortCodeProxy(@Param("shortCode") String shortCode);


}
