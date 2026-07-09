package techpriest.Url_Shortener.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.dto.ClickLogResponseDto;
import techpriest.Url_Shortener.models.ClickLog;
import techpriest.Url_Shortener.models.Url;
import techpriest.Url_Shortener.repositories.ClickLogRepository;
import techpriest.Url_Shortener.services.ClickLogService;

@Service
public class ClickLogServiceImpl implements ClickLogService {
    private final ClickLogRepository clickLogRepository;

    public ClickLogServiceImpl(ClickLogRepository clickLogRepository) {
        this.clickLogRepository = clickLogRepository;
    }

    @Override
    public Page<ClickLog> getClickLogsForUrl(UUID urlId, Pageable pageable) {
        return clickLogRepository.findByUrlIdOrderByCreatedAtDesc(urlId, pageable);
    }

    @Override
    public Page<ClickLogResponseDto> all(String search, Pageable pageable) {
        Page<ClickLog> clickLogs;
        if (search == null || search.isBlank()) {
            clickLogs = clickLogRepository.findAll(pageable);
        } else {
            clickLogs = clickLogRepository.findByUrlIdOrderByCreatedAtDesc(UUID.fromString(search), pageable);
        }
        return clickLogs.map(ClickLogResponseDto::from);
    }

    @Override
    public ClickLog logClick(Url url, String ipAddress, String userAgent) {
        ClickLog clickLog = new ClickLog();
        clickLog.setUrl(url);
        clickLog.setIpAddress(ipAddress);
        clickLog.setUserAgent(userAgent);
        return clickLogRepository.save(clickLog);
    }

}
