package techpriest.Url_Shortener.services;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.dto.ClickLogResponseDto;
import techpriest.Url_Shortener.models.ClickLog;
import techpriest.Url_Shortener.models.Url;
import techpriest.Url_Shortener.repositories.ClickLogRepository;

@Service
public class ClickLogService {
    private final ClickLogRepository clickLogRepository;

    public ClickLogService(ClickLogRepository clickLogRepository) {
        this.clickLogRepository = clickLogRepository;
    }

    public Page<ClickLog> getClickLogsForUrl(UUID urlId, Pageable pageable) {
        return clickLogRepository.findByUrlIdOrderByCreatedAtDesc(urlId, pageable);
    }

    public Page<ClickLogResponseDto> all(String search, Pageable pageable) {
        Page<ClickLog> clickLogs;
        if (search == null || search.isBlank()) {
            clickLogs = clickLogRepository.findAll(pageable);
        } else {
            clickLogs = clickLogRepository.findByUrlIdOrderByCreatedAtDesc(UUID.fromString(search), pageable);
        }
        return clickLogs.map(ClickLogResponseDto::from);
    }

    public ClickLog logClick(Url url, String ipAddress, String userAgent) {
        ClickLog clickLog = new ClickLog();
        clickLog.setUrl(url);
        clickLog.setIpAddress(ipAddress);
        clickLog.setUserAgent(userAgent);
        return clickLogRepository.save(clickLog);
    }

}
