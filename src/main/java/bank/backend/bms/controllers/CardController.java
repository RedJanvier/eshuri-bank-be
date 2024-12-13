package bank.backend.bms.controllers;

import bank.backend.bms.dtos.CardResponseDto;
import bank.backend.bms.dtos.ChangePinRequest;
import bank.backend.bms.models.Card;
import bank.backend.bms.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Card> createCard() {
        Card createCard = cardService.createCard();

        return new ResponseEntity<>(createCard, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Map<String, String>> deleteCard(@PathVariable(name = "cardId") Long cardId) {
        Map<String, String> response;
        try {
            cardService.deleteCard(cardId);
            response = Map.of("message", "Card deleted successfully");
            return ResponseEntity.ok(response);
        }catch (RuntimeException ex) {
            response = Map.of("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{cardNumber}/change-pin")
    public ResponseEntity<Map<String, String>> changePin(
            @PathVariable(name = "cardNumber") String cardNumber,
            @RequestBody ChangePinRequest changePinRequest
            ) {
        Map<String, String> response;
        try{
            cardService.updatePin(cardNumber, changePinRequest);
            response = Map.of("message", "Pin updated sucessfully");
            return ResponseEntity.ok(response);
        }catch (RuntimeException ex) {
            response = Map.of("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
