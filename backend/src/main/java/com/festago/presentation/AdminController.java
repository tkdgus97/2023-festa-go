package com.festago.presentation;

import com.festago.application.FestivalService;
import com.festago.application.StageService;
import com.festago.application.TicketService;
import com.festago.dto.FestivalCreateRequest;
import com.festago.dto.FestivalResponse;
import com.festago.dto.StageCreateRequest;
import com.festago.dto.StageResponse;
import com.festago.dto.TicketCreateRequest;
import com.festago.dto.TicketResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;
    private final StageService stageService;
    private final TicketService ticketService;

    public AdminController(FestivalService festivalService, StageService stageService, TicketService ticketService) {
        this.festivalService = festivalService;
        this.stageService = stageService;
        this.ticketService = ticketService;
    }

    @PostMapping("/festivals")
    public ResponseEntity<FestivalResponse> createFestival(@RequestBody FestivalCreateRequest request) {
        FestivalResponse response = festivalService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/stages")
    public ResponseEntity<StageResponse> createStage(@RequestBody StageCreateRequest request) {
        StageResponse response = stageService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }

    @PostMapping("/tickets")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketCreateRequest request) {
        TicketResponse response = ticketService.create(request);
        return ResponseEntity.ok()
            .body(response);
    }
}