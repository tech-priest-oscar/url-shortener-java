package techpriest.Url_Shortener.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import techpriest.Url_Shortener.dto.UrlDataDto;
import techpriest.Url_Shortener.dto.UrlResponseDto;
import techpriest.Url_Shortener.dto.response.ResponseWrapper;

import java.util.UUID;

public interface UrlService {
    ResponseWrapper<UrlResponseDto> createUrl(UrlDataDto urlDataDto, UUID userId);
    public String resolveAndTrack(String shortCode, String ipAddress, String userAgent);
    public Void deleteUrl(UUID urlId);
    Page<UrlResponseDto> getUrls(String search, Pageable pageable);
}
