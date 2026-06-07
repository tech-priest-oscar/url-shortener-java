package techpriest.Url_Shortener.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import techpriest.Url_Shortener.dto.ClickLogResponseDto;
import techpriest.Url_Shortener.services.ClickLogService;

@RestController
@RequestMapping("/api/click-logs")
public class ClickLogController {
    private final ClickLogService clickLogService;

    public ClickLogController(ClickLogService clickLogService) {
        this.clickLogService = clickLogService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<ClickLogResponseDto>> listUrlLogs(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(clickLogService.all(search, pageable));
    }
    
}
