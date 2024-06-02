package aw.geez.controller;

import aw.geez.dto.request.TransferRequest;
import aw.geez.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        log.info("Transfer was successful");
        accountService.transfer(transferRequest);
        return ResponseEntity.ok().build();
    }
}
