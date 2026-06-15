package techpriest.Url_Shortener.controllers;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import techpriest.Url_Shortener.dto.UrlDataDto;
import techpriest.Url_Shortener.dto.UrlResponseDto;
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
    public ResponseEntity<UrlResponseDto> createShortUrl(
            @Valid @RequestBody UrlDataDto urlDataDto,
            @RequestAttribute(value = "userId", required = false) String userId
        ) {
        UUID creatorId = userId != null ? UUID.fromString(userId) : null;
        UrlResponseDto url = this.urlService.createUrl(urlDataDto, creatorId);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/")
    public ResponseEntity<Page<UrlResponseDto>> listUrls(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(urlService.getUrls(search, pageable));
    }
    
    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteUrl(@PathVariable UUID id) {
        urlService.deleteUrl(id);
        return ResponseEntity.noContent().build();
    }
    
}