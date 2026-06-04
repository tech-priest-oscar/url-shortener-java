package techpriest.Url_Shortener.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import techpriest.Url_Shortener.services.ClickLogService;

@RestController
@RequestMapping("/api/click-logs")
public class ClickLogController {
    private final ClickLogService clickLogService;

    public ClickLogController(ClickLogService clickLogService) {
        this.clickLogService = clickLogService;
    }
    
}
