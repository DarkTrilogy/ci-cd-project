package aw.geez.controller;

import aw.geez.dto.request.NewCustomerDto;
import aw.geez.dto.response.CustomerBalance;
import aw.geez.dto.response.NewCustomerResponse;
import aw.geez.service.CustomerService;
import io.swagger.client.model.Currency;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<NewCustomerResponse> createCustomer(@Valid @RequestBody NewCustomerDto newCustomerDto) {
        log.info("New customer: " + newCustomerDto.toString() + " was created");
        return ResponseEntity.ok(customerService.createCustomer(newCustomerDto));
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<CustomerBalance> getBalance(@PathVariable("customerId") Integer customerId,
                                                      @RequestParam("currency") Currency currency) {
        log.info("Getting balance of customer id: " + customerId + " in " + currency.toString());
        return ResponseEntity.ok(customerService.getBalance(customerId, currency));
    }
}
