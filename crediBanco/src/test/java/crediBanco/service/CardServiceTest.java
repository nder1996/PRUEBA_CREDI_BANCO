package crediBanco.service;

import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;
import crediBanco.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    public CardRepository cardRepository;

    @Mock
    private ResponseApiBuilderService apiBuilderService;

    public List<CardEntity> cardEntities = new ArrayList<>();

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

        when(cardRepository.findAll()).thenReturn(cardEntities);


        // Configura el comportamiento del mock del servicio
        Map<String, Object> data = new HashMap<>();
        data.put("cards", cardEntities.stream()
                .map(card -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idCard", card.getIdCard());
                    map.put("expirationDate", card.getExpirationDate());
                    map.put("balance", card.getBalance());
                    map.put("ownerName", card.getOwnerName());
                    map.put("state", card.getState());
                    map.put("createdAt", card.getCreatedAt());
                    map.put("updatedAt", card.getUpdatedAt());
                    return map;
                })
                .collect(Collectors.toList()));

        when(apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards")).thenReturn(data);

        Map<String, Object> metaJson = new HashMap<>();





    }


    @Test
    public void testGetAllCard_Success() {

        // Mock ApiResponse
        Map<String, Object> data = apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards");
        ApiResponse<String> successResponse = apiBuilderService.successRespuesta(data);

        // Configura el comportamiento del mock apiBuilderService
        when(apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards")).thenReturn(data);
        when(apiBuilderService.successRespuesta(data)).thenReturn(successResponse);

        // Llama al método del servicio
        ApiResponse<String> response = cardService.getAllCard();

        // Verifica que la respuesta sea la esperada
        assertEquals(successResponse, response);
    }


    @Test
    public void testGetAllCard_Invalid() {
        // Mock cardEntities as null or empty list
        List<CardEntity> cardEntitiesNull = null; // or Collections.emptyList()
        // Configura el comportamiento del mock apiBuilderService
        when(cardRepository.findAll()).thenReturn(cardEntitiesNull);
        // Invalida: No configurar apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards")
        // Llama al método del servicio
        ApiResponse<String> response = cardService.getAllCard();
        // Verifica que la respuesta no sea la esperada
        assertEquals(apiBuilderService.errorRespuesta("NO_DATA_AVAILABLE"), response);
    }







/*
    @Test
    public void testGetCardById_Success() {
        // Mock CardEntity data
        String idCard = "1234567890123456";
        CardEntity card = cardEntities.stream()
                .filter(c -> c.getIdCard().equals(idCard))
                .findFirst()
                .orElse(null);

        // Mock ApiResponse
        Map<String, Object> data = this.apiBuilderService.convertEntityToMapSinCambios(card,"card");
        ApiResponse<String> successResponse = this.apiBuilderService.successRespuesta(data);


        // Configure the mock behavior
        when(cardRepository.findById(idCard)).thenReturn(Optional.of(card));
        when(apiBuilderService.convertEntityToMapSinCambios(card, "card")).thenReturn(data);
        when(apiBuilderService.successRespuesta(data)).thenReturn(successResponse);

        // Call the service method
        ApiResponse<String> response = cardService.getCardById(idCard);

        // Verify that the response is as expected
        assertEquals(successResponse, response);
    }

*/

    /*
    @Test
    public void testGetCardById_Success() {
            // Mock CardEntity data
            String idCard = "1234567890123456";
            CardEntity card = new CardEntity();

        for (CardEntity cardEntity : cardEntities) {
            if (cardEntity.getIdCard().equals(idCard)) {
                card = cardEntity;
                break;
            }
        }


            // Mock ApiResponse
            Map<String, Object> data = apiBuilderService.convertEntityToMapSinCambios(card, "card");
            ApiResponse<String> successResponse = apiBuilderService.successRespuesta(data);

            // Configure the mock behavior
           // when(cardRepository.findById(idCard)).thenReturn(card.getIdCard());
            when(apiBuilderService.convertEntityToMapSinCambios(card, "card")).thenReturn(data);
            when(apiBuilderService.successRespuesta(data)).thenReturn(successResponse);

            // Call the service method
            ApiResponse<String> response = cardService.getCardById(idCard);

            // Verify that the response is as expected
            assertEquals(successResponse, response);
    }
*/




/*

    @Test
    public void testGetCardById_Invalid() {
        // Mock CardEntity as empty Optional
        String idCard = "123";
        Optional<CardEntity> optionalCard = Optional.empty(); // simulating card not found

        // Configura el comportamiento del mock
        when(cardRepository.findById(idCard)).thenReturn(optionalCard);
        // Incorrect: No configurar apiBuilderService.convertEntityToMapSinCambios(card, "card")

        // Llama al método del servicio
        ApiResponse<String> response = cardService.getCardById(idCard);

        // Verifica que la respuesta no sea la esperada
        ApiResponse<String> expectedResponse = apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER");
        assertEquals(expectedResponse, response);
    }

*/
}
