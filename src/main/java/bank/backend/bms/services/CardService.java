package bank.backend.bms.services;

import bank.backend.bms.dtos.CardResponseDto;
import bank.backend.bms.dtos.ChangePinRequest;
import bank.backend.bms.dtos.ResetPinRequest;
import bank.backend.bms.models.Card;
import bank.backend.bms.repositories.CardRepository;
import bank.backend.bms.utils.CardUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardUtils cardUtils;


    private static final int EXPIRY_PERIOD = 5; // Card Expiry period


    public Card createCard() {

        String generatedCardNumber;
        //If the card number is already taken, regenerate another one
        do {
            generatedCardNumber = cardUtils.generateAccountNumber();
        }while (cardRepository.findByCardNumber(generatedCardNumber).isPresent());

        String generatedCvv;
        do {
            generatedCvv = cardUtils.generateCvv();
        }while (cardRepository.findByCvv(cardUtils.encrypt(generatedCvv)).isPresent());

        Card card = new Card();
        card.setCardNumber(generatedCardNumber);
        card.setCvv(cardUtils.encrypt(generatedCvv));
        card.setPin(cardUtils.encrypt(cardUtils.generatePin()));
        card.setIssueDate(LocalDate.now());
        card.setExpireDate(LocalDate.now().plusYears(EXPIRY_PERIOD));

        Card createdCard = cardRepository.save(card);

        return new Card(
                createdCard.getId(),
                createdCard.getCardNumber(),
                cardUtils.decrypt(createdCard.getCvv()),
                createdCard.getIssueDate(),
                createdCard.getExpireDate(),
                cardUtils.decrypt(createdCard.getPin())
        );

    }

    public CardResponseDto getCard(Long id) {
        Card existingCard = cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Requested card does not exist"));
        return new CardResponseDto(
                existingCard.getId(),
                existingCard.getCardNumber(),
                cardUtils.decrypt(existingCard.getCvv()),
                existingCard.getIssueDate(),
                existingCard.getExpireDate()
        );
    }

    public void deleteCard(Long cardId) {
        Card existingCard = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Requested card does not exist"));
        cardRepository.delete(existingCard);
    }


    public List<CardResponseDto> getAllCards() {
        List<Card> allCards = cardRepository.findAll();
        return allCards.stream().map(card -> new CardResponseDto(
                card.getId(),
                card.getCardNumber(),
                cardUtils.decrypt(card.getCvv()),
                card.getIssueDate(),
                card.getExpireDate()
        )).collect(Collectors.toList());
    }

    public void updatePin(String cardNumber, ChangePinRequest changePinRequest) {
        Card existingCard = cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("Requested card does not exist"));

        String oldPin = changePinRequest.getOldPin();
        if(!existingCard.getPin().equals(cardUtils.encrypt(oldPin))) {
            throw new RuntimeException("Old pin does not match");
        }
        String newPinEncrypted = cardUtils.encrypt(changePinRequest.getNewPin());

        existingCard.setPin(newPinEncrypted);

        cardRepository.save(existingCard);

    }

    //This service method should only be used by admins
    public void resetPin(String cardNumber, ResetPinRequest resetPinRequest) {
        var existingCard = cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> new RuntimeException("Requested card does not exist"));

        existingCard.setPin(cardUtils.encrypt(resetPinRequest.getNewPin()));

        cardRepository.save(existingCard);
    }



}
