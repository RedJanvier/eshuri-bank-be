package bank.backend.bms.services;

import bank.backend.bms.repositories.CardRepository;
import bank.backend.bms.utils.CardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardUtils cardUtils;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_create_new_card() {

    }

}