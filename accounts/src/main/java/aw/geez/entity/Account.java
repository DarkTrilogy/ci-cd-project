package aw.geez.entity;

import io.swagger.client.model.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"currency", "owner"})})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountNumber;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @JoinColumn(name = "owner", referencedColumnName="id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Customer owner;
}
