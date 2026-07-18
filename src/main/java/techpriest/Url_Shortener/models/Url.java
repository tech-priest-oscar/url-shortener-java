package techpriest.Url_Shortener.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "url")
public class Url extends Base {
    protected Url() {}


    public Url(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }

    @OneToMany(mappedBy = "url", fetch = FetchType.LAZY)
    private List<ClickLog> clickLogs;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String originalUrl;
    private int clickCount = 0;

    @Column(length = 10, nullable = false, unique = true)
    private String shortCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getId().toString());
        map.put("originalUrl", originalUrl);
        map.put("shortCode", shortCode);
        map.put("createdAt", getCreatedAt().toString());
        return map;
    }

}