package techpriest.Url_Shortener.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import techpriest.Url_Shortener.dto.ClickLogResponseDto;
import techpriest.Url_Shortener.models.ClickLog;
import techpriest.Url_Shortener.models.Url;

public interface ClickLogService {
    Page<ClickLog> getClickLogsForUrl(UUID urlId, Pageable pageable);
    Page<ClickLogResponseDto> all(String search, Pageable pageable);
    ClickLog logClick(Url url, String ipAddress, String userAgent);
}
