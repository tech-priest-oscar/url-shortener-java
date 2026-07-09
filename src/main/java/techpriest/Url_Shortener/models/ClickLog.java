package techpriest.Url_Shortener.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "click_log") 
public class ClickLog extends Base {
    public ClickLog() {}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    private Url url;

    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")    
    private String userAgent;

}