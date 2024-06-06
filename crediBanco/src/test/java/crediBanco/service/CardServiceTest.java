package crediBanco.service;

import crediBanco.entity.CardEntity;
import crediBanco.entity.TransactionEntity;
import crediBanco.model.response.ApiResponse;
import crediBanco.repository.TransactionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;
import crediBanco.repository.CardRepository;
import crediBanco.util.ValidationUtilCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.when; //should normally use this one

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    public CardRepository cardRepository;


    public List<CardEntity> cardEntities = new ArrayList<>();


    @Mock
    private ResponseApiBuilderService apiBuilderService;

    @InjectMocks
    private CardService cardService;


    @BeforeEach
    void setUp() {
        cardEntities = Arrays.asList(
                new CardEntity("1234567890123456", "12/24", 100.0, "John Doe", "A", new Date(), new Date()),
                new CardEntity("2345678901234567", "01/25", 200.0, "Jane Doe", "A", new Date(), new Date()),
                new CardEntity("3456789012345678", "02/26", 300.0, "Alice Smith", "A", new Date(), new Date()),
                new CardEntity("4567890123456789", "03/27", 400.0, "Bob Smith", "A", new Date(), new Date()),
                new CardEntity("5678901234567890", "04/28", 500.0, "Charlie Brown", "A", new Date(), new Date())
        );
        MockitoAnnotations.openMocks(this); // Inicializa los mocks

        when(cardRepository.findAll()).thenReturn(cardEntities);

    }


    @Test
    public void testGetAllCard_Success() {

        // Mock ApiResponse
        Map<String, Object> data = apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards");
        ApiResponse<String> successResponse = apiBuilderService.successRespuesta(data);
        //        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        // Configura el comportamiento del mock apiBuilderService
        when(apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards")).thenReturn(data);
        when(apiBuilderService.successRespuesta(data)).thenReturn(successResponse);

        // Llama al método del servicio
        ApiResponse<String> response = cardService.getAllCard();

        // Verifica que la respuesta sea la esperada
        assertEquals(successResponse, response);
    }


}
