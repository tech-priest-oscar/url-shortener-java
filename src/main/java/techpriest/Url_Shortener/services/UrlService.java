package techpriest.Url_Shortener.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.dto.UrlDataDto;
import techpriest.Url_Shortener.dto.UrlResponseDto;
import techpriest.Url_Shortener.exceptions.NotFoundException;
import techpriest.Url_Shortener.models.Url;
import techpriest.Url_Shortener.repositories.URLRepository;
import techpriest.Url_Shortener.util.ShortCodeGenerator;

@Service
public class UrlService {
    private final URLRepository urlRepository;

    public UrlService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url createUrl(UrlDataDto urlDataDto) {
        Url url = new Url(urlDataDto.getUrl(), ShortCodeGenerator.generate());

        Url saved = this.urlRepository.save(url);
        return saved;
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


}
