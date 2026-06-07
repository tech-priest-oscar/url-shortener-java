package techpriest.Url_Shortener.services;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.models.ClickLog;
import techpriest.Url_Shortener.models.Url;
import techpriest.Url_Shortener.repositories.ClickLogRepository;



@Service
public class ClickLogService {
    private final ClickLogRepository clickLogRepository;

    public ClickLogService(ClickLogRepository clickLogRepository) {
        this.clickLogRepository = clickLogRepository;
    }

    public List<ClickLog> getClickLogsForUrl(UUID urlId) {
        return clickLogRepository.findByUrlId(urlId);
    }

    public ClickLog logClick(Url url, String ipAddress, String userAgent) {
        ClickLog clickLog = new ClickLog();
        clickLog.setUrl(url);
        clickLog.setIPAddress(ipAddress);
        clickLog.setUserAgent(userAgent);
        return clickLogRepository.save(clickLog);
    }

}
