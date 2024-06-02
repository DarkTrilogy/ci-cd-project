package aw.geez.service;

import aw.geez.client.ConverterClient;
import aw.geez.dto.request.NewAccountDto;
import aw.geez.dto.request.TopUpRequest;
import aw.geez.dto.request.TransferRequest;
import aw.geez.dto.response.AccountBalance;
import aw.geez.dto.response.NewAccountResponse;
import aw.geez.entity.Account;
import aw.geez.exception.CustomerAccountNotFoundException;
import aw.geez.exception.CustomerNotFoundException;
import aw.geez.repository.AccountRepository;
import aw.geez.repository.CustomersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static aw.geez.utils.Constants.INSUFFICIENT_AMOUNT;
import static aw.geez.utils.Constants.NEGATIVE_TOPUP_AMOUNT;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomersRepository customersRepository;
    private final ConverterClient converterClient;

    @Transactional
    public NewAccountResponse createAccount(NewAccountDto newAccountDto) {
        var customer = customersRepository.findById(newAccountDto.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);
        Account account = Account.builder()
                .balance(BigDecimal.ZERO)
                .currency(newAccountDto.getCurrency())
                .owner(customer)
                .build();
        accountRepository.save(account);

        return NewAccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .build();
    }

    @Transactional
    public AccountBalance getAccountBalance(Integer accountNumber) {
        Account entity = accountRepository.findById(accountNumber)
                .orElseThrow(CustomerAccountNotFoundException::new);
        return AccountBalance.builder()
                .balance(entity.getBalance())
                .currency(entity.getCurrency())
                .build();
    }

    @Transactional
    public void topUpAccount(Integer accountNumber, TopUpRequest amount) {
        if (amount.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(NEGATIVE_TOPUP_AMOUNT);
        }
        accountRepository.findById(accountNumber)
                .ifPresentOrElse(account -> {
                    account.setBalance(account.getBalance().add(amount.getAmount()).setScale(2, RoundingMode.HALF_EVEN));
                    accountRepository.save(account);
                }, () -> {
                    throw new CustomerAccountNotFoundException();
                });
    }

    @Transactional
    public void transfer(TransferRequest transferRequest) {
        if (transferRequest.getTransferAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(NEGATIVE_TOPUP_AMOUNT);
        }
        accountRepository.findById(transferRequest.getSenderAccount())
                .ifPresentOrElse(senderAccount -> {
                    accountRepository.findById(transferRequest.getReceiverAccount())
                            .ifPresentOrElse(receiverAccount -> {
                                BigDecimal senderAccountBalance = senderAccount.getBalance();
                                if (senderAccountBalance.compareTo(transferRequest.getTransferAmount()) < 0) {
                                    throw new IllegalArgumentException(INSUFFICIENT_AMOUNT);
                                }
                                senderAccount.setBalance(senderAccountBalance
                                        .subtract(transferRequest.getTransferAmount())
                                        .setScale(2, RoundingMode.HALF_EVEN));

                                BigDecimal toTransfer;
                                if (senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
                                    toTransfer = transferRequest.getTransferAmount();
                                } else {
                                    toTransfer = converterClient.convertCurrency(
                                                    senderAccount.getCurrency(),
                                                    receiverAccount.getCurrency(),
                                                    transferRequest.getTransferAmount())
                                            .getAmount();
                                }
                                receiverAccount.setBalance(receiverAccount.getBalance()
                                        .add(toTransfer)
                                        .setScale(2, RoundingMode.HALF_EVEN));
                                accountRepository.save(senderAccount);
                                accountRepository.save(receiverAccount);
                            }, () -> {
                                throw new CustomerAccountNotFoundException();
                            });
                }, () -> {
                    throw new CustomerAccountNotFoundException();
                });
    }
}
