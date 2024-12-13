package bank.backend.bms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {
    private Long id;
    private String cardNumber;
    private String cvv;
    private LocalDate issueDate;
    private LocalDate expireDate;
}
