package techpriest.Url_Shortener.controllers;

import java.net.URI;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import techpriest.Url_Shortener.services.UrlService;

@Slf4j
@RestController
@RequestMapping("/short")
public class RedirectController {
    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode, HttpServletRequest request) {
        String rawIp = request.getRemoteAddr();
        String ipAddress = InetAddressValidator.getInstance().isValid(rawIp) ? rawIp : null;
        String userAgent = request.getHeader("User-Agent");

        log.atDebug()
            .addKeyValue("shortCode", shortCode)
            .addKeyValue("rawIp", rawIp)
            .log("Received redirect request for shortCode '{}' from IP '{}'", shortCode, rawIp);

        String originalUrl = urlService.resolveAndTrack(shortCode, ipAddress, userAgent);
        log.info("Redirecting shortCode '{}' to '{}'", shortCode, originalUrl);

        return ResponseEntity
                .status(HttpStatus.FOUND)            // 302
                .location(URI.create(originalUrl))   // the Location header
                .build();
    }
}
