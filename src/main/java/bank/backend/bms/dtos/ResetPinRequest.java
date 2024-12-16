package bank.backend.bms.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPinRequest {
    @NotBlank(message = "new pin is required")
    @Size(min = 4, max = 4)
    private String newPin;
}
