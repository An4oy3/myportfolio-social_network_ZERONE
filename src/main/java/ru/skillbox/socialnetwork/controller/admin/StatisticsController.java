package ru.skillbox.socialnetwork.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.skillbox.socialnetwork.data.dto.RegisterRequest;

@RestController
@Api(tags = "Статистика в админ панели")
@Slf4j
@PreAuthorize("hasAuthority('user:administration')")
public class StatisticsController {

    @GetMapping("/api/v1/admin/getCommonStat")
    @ApiOperation(value = "Получить общую статистику")
    @CrossOrigin(allowCredentials = "true", origins = "http://127.0.0.1:8080")
    public String getAll() {
        return "CommonStat";
    }
}
