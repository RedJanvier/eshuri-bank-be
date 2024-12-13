package bank.backend.bms.dtos;

import lombok.Data;

@Data
public class ChangePinRequest {
    private String oldPin;
    private String newPin;
}
