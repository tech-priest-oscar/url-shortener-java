package techpriest.Url_Shortener.dto;

import java.time.Instant;
import java.util.UUID;

import techpriest.Url_Shortener.models.Url;

public record UrlResponseDto(
        UUID id,
        String originalUrl,
        String shortCode,
        int clickCount,
        Instant lastClickedAt,
        Instant createdAt) {

    /** Copies the wanted fields out of a Url entity into this DTO. */
    public static UrlResponseDto from(Url url) {
        return new UrlResponseDto(
                url.getId(),
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getClickCount(),
                url.getLastClickedAt(),
                url.getCreatedAt());
    }
}
