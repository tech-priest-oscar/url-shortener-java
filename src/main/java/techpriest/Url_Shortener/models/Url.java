package techpriest.Url_Shortener.models;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.HashMap;


@Entity
@Table(name = "url")
public class Url extends Base {
    protected Url() {}


    public Url(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }

    @OneToMany(mappedBy = "url", fetch = FetchType.LAZY)
    private List<ClickLog> clickLogs;

    private String originalUrl;
    private int clickCount = 0;
    private Instant lastClickedAt = Instant.now();
    private String shortCode;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public Instant getLastClickedAt() {
        return lastClickedAt;
    }

    public void setLastClickedAt(Instant lastClickedAt) {
        this.lastClickedAt = lastClickedAt;
    }

    public String getShortCode() {
        return shortCode;
    }

    public List<ClickLog> getClickLogs() {
        return clickLogs;
    }

    public void setClickLogs(List<ClickLog> clickLogs) {
        this.clickLogs = clickLogs;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getId().toString());
        map.put("originalUrl", originalUrl);
        map.put("shortCode", shortCode);
        map.put("lastClickedAt", lastClickedAt.toString());
        map.put("createdAt", getCreatedAt().toString());
        return map;
    }

}