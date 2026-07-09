package techpriest.Url_Shortener.dto.proxy;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data

public class UrlProxy {
    private String originalUrl;
    private int clickCount;
    private Instant lastClickedAt;
    private String shortCode;
    private UUID userId;

    public UrlProxy(String originalUrl, int clickCount, Instant lastClickedAt, String shortCode,UUID userId) {
        this.originalUrl = originalUrl;
        this.clickCount = clickCount;
        this.lastClickedAt = lastClickedAt;
        this.shortCode = shortCode;
        this.userId = userId;
    }
}
