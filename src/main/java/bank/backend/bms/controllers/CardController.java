package bank.backend.bms.controllers;

import bank.backend.bms.dtos.CardResponseDto;
import bank.backend.bms.dtos.ChangePinRequest;
import bank.backend.bms.dtos.ResetPinRequest;
import bank.backend.bms.models.Card;
import bank.backend.bms.services.CardService;
import jakarta.validation.Valid;
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

    //Only admins
    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<?> getSingleCard(@PathVariable(name = "cardId") Long cardId) {
        try{
            CardResponseDto card = cardService.getCard(cardId);
            return ResponseEntity.ok(card);
        }catch (RuntimeException ex) {
            Map<String, String> response = Map.of("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //This route will only be used by admins
    @PutMapping("/{cardNumber}/pin-reset")
    public ResponseEntity<Map<String, String>> resetPin(
            @PathVariable(name = "cardNumber") String cardNumber,
            @Valid @RequestBody ResetPinRequest resetPinRequest
            ) {
        Map<String, String> response;
        try{
            cardService.resetPin(cardNumber, resetPinRequest);
            response = Map.of("message", "Pin reset successfully");
            return ResponseEntity.ok(response);
        }catch (RuntimeException ex) {
            response = Map.of("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
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
            @Valid @RequestBody ChangePinRequest changePinRequest
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
