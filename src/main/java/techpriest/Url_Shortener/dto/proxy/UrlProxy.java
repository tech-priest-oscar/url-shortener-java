package techpriest.Url_Shortener.dto.proxy;

import lombok.Data;

import java.util.UUID;

@Data

public class UrlProxy {
    private String originalUrl;
    private int clickCount;
    private String shortCode;
    private UUID userId;

    public UrlProxy(String originalUrl, int clickCount, String shortCode,UUID userId) {
        this.originalUrl = originalUrl;
        this.clickCount = clickCount;
        this.shortCode = shortCode;
        this.userId = userId;
    }
}
