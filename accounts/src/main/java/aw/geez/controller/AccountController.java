package aw.geez.controller;

import aw.geez.dto.request.NewAccountDto;
import aw.geez.dto.request.TopUpRequest;
import aw.geez.dto.response.AccountBalance;
import aw.geez.dto.response.NewAccountResponse;
import aw.geez.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<NewAccountResponse> createAccount(@Valid @RequestBody NewAccountDto newAccountDto) {
        log.info("Creating account: " + newAccountDto.toString());
        return ResponseEntity.ok(accountService.createAccount(newAccountDto));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountBalance> getAccountBalance(@PathVariable("accountNumber") Integer accountNumber) {
        log.info("Getting balance of AccountNumber " + accountNumber);
        return ResponseEntity.ok(accountService.getAccountBalance(accountNumber));
    }

    @PostMapping("/{accountNumber}/top-up")
    public ResponseEntity<?> topUpAccount(@PathVariable("accountNumber") Integer accountNumber,
                                                          @Valid @RequestBody TopUpRequest topUpRequest) {
        log.info("Top up account: " + accountNumber + " with amount: " + topUpRequest);
        accountService.topUpAccount(accountNumber, topUpRequest);
        return ResponseEntity.ok().build();
    }
}
