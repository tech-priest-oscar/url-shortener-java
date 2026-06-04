package techpriest.Url_Shortener.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import techpriest.Url_Shortener.dto.UrlDataDto;
import techpriest.Url_Shortener.models.Url;
import techpriest.Url_Shortener.services.UrlService;


@RestController
@RequestMapping("/api/urls")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
        
    }

    @PostMapping("create")
    public ResponseEntity<Url> createShortUrl(@Valid @RequestBody UrlDataDto urlDataDto) {
        Url url = this.urlService.createUrl(urlDataDto);
        return ResponseEntity.ok(url);
    }
    
    
}
