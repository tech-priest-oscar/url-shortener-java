package techpriest.Url_Shortener.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import techpriest.Url_Shortener.dto.UrlDataDto;
import techpriest.Url_Shortener.dto.UrlResponseDto;
import techpriest.Url_Shortener.exceptions.NotFoundException;
import techpriest.Url_Shortener.models.Url;
import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.repositories.URLRepository;
import techpriest.Url_Shortener.repositories.UserRepository;
import techpriest.Url_Shortener.util.ShortCodeGenerator;

@Service
@Slf4j
public class UrlService {
    private final URLRepository urlRepository;
    private final UserRepository userRepository;
    private final ClickLogService clickLogService;

    public UrlService(URLRepository urlRepository, UserRepository userRepository,
            ClickLogService clickLogService) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
        this.clickLogService = clickLogService;
    }

    public UrlResponseDto createUrl(UrlDataDto urlDataDto, UUID userId) {
        Url url = new Url(urlDataDto.getUrl(), ShortCodeGenerator.generate());

        // userId may be null (e.g. for legacy/unauthenticated paths); only attach when present
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            url.setUser(user);
        } else {
            log.debug("No userId provided; creating URL without associated user.");
        }

        UrlResponseDto responseUrl = UrlResponseDto.from(this.urlRepository.save(url));
        return responseUrl;
    }

    public Page<UrlResponseDto> getUrls(String search, Pageable pageable) {
        Page<Url> urls;
        if (search == null || search.isBlank()) {
            urls = this.urlRepository.findAll(pageable);
        } else {
            urls = this.urlRepository
                    .findByOriginalUrlContainingIgnoreCaseOrShortCodeContainingIgnoreCase(
                            search, search, pageable);
        }
        return urls.map(UrlResponseDto::from);
    }

    public Void deleteUrl(UUID urlId) {
        if (!urlRepository.existsById(urlId)) {
            throw new NotFoundException("URL object not found");
        }

        this.urlRepository.deleteById(urlId);
        return null;
    }

    public String resolveAndTrack(String shortCode, String ipAddress, String userAgent) {
        Url url = this.urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new NotFoundException("Short code not found: " + shortCode));

        url.setClickCount(url.getClickCount() + 1);
        url.setLastClickedAt(Instant.now());
        this.urlRepository.save(url);

        this.clickLogService.logClick(url, ipAddress, userAgent);

        return url.getOriginalUrl();
    }


}
