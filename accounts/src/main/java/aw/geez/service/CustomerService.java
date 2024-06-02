package aw.geez.service;

import aw.geez.client.ConverterClient;
import aw.geez.dto.request.NewCustomerDto;
import aw.geez.dto.response.CustomerBalance;
import aw.geez.dto.response.NewCustomerResponse;
import aw.geez.entity.Customer;
import aw.geez.exception.CustomerNotFoundException;
import aw.geez.repository.CustomersRepository;
import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomersRepository customersRepository;
    private final ConverterClient converterClient;

    @Transactional
    public NewCustomerResponse createCustomer(NewCustomerDto newCustomerDto) {
        Customer customer = Customer.builder()
                .firstName(newCustomerDto.getFirstName())
                .lastName(newCustomerDto.getLastName())
                .birthDay(newCustomerDto.getBirthDay())
                .build();
        customersRepository.save(customer);

        return NewCustomerResponse.builder()
                .customerId(customer.getId())
                .build();
    }

    @Transactional
    public CustomerBalance getBalance(Integer customerId, Currency currency) {
        Customer customer = customersRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        BigDecimal balance = customer.getAccounts().stream()
                .map(account -> {
                    if (account.getCurrency().equals(currency)) {
                        return account.getBalance();
                    }
                    return converterClient.convertCurrency(account.getCurrency(), currency, account.getBalance()).getAmount();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_EVEN);

        return CustomerBalance.builder()
                .balance(balance)
                .currency(currency)
                .build();
    }
}
