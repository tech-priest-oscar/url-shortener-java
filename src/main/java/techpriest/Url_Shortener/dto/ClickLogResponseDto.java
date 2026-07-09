package techpriest.Url_Shortener.dto;

import java.time.Instant;
import java.util.Map;

import techpriest.Url_Shortener.models.ClickLog;


public record ClickLogResponseDto(
        String ipAddress,
        String userAgent,
        Instant createdAt,
        Map<String, String> urlMap
    ) {

    /** Copies the wanted fields out of a ClickLog entity into this DTO. */
    public static ClickLogResponseDto from(ClickLog clickLog) {
        return new ClickLogResponseDto(
                clickLog.getIpAddress(),
                clickLog.getUserAgent(),
                clickLog.getCreatedAt(),
                clickLog.getUrl().toMap()
        );
    }
}
