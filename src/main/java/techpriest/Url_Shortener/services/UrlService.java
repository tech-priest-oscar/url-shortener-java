package techpriest.Url_Shortener.services;

import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.dto.UrlDataDto;
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

}
