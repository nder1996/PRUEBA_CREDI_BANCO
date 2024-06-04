package crediBanco.service;

import crediBanco.entity.CardEntity;
import crediBanco.entity.TransactionEntity;
import crediBanco.model.response.ApiResponse;
import crediBanco.repository.TransactionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CardServiceTest {

    @MockBean
    private CardRepository cardRepository;

    @Autowired
    private ResponseApiBuilderService apiBuilderService;

    @Autowired
    private CardService cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cardService = new CardService(cardRepository, apiBuilderService);
        List<CardEntity> cardEntities = Arrays.asList(
                new CardEntity("1234567890123456", "12/24", 100.0, "John Doe", "A", new Date(), new Date()),
                new CardEntity("2345678901234567", "01/25", 200.0, "Jane Doe", "A", new Date(), new Date()),
                new CardEntity("3456789012345678", "02/26", 300.0, "Alice Smith", "A", new Date(), new Date()),
                new CardEntity("4567890123456789", "03/27", 400.0, "Bob Smith", "A", new Date(), new Date()),
                new CardEntity("5678901234567890", "04/28", 500.0, "Charlie Brown", "A", new Date(), new Date())
        );
        when(cardRepository.findAll()).thenReturn(cardEntities);
    }

    @Test
    public void testGetAllCard_Success() {
        // Obtener las instancias de CardEntity creadas en el método setUp()
        List<CardEntity> cardEntities = Arrays.asList(
                new CardEntity("1234567890123456", "12/24", 100.0, "John Doe", "A", new Date(), new Date()),
                new CardEntity("2345678901234567", "01/25", 200.0, "Jane Doe", "A", new Date(), new Date()),
                new CardEntity("3456789012345678", "02/26", 300.0, "Alice Smith", "A", new Date(), new Date()),
                new CardEntity("4567890123456789", "03/27", 400.0, "Bob Smith", "A", new Date(), new Date()),
                new CardEntity("5678901234567890", "04/28", 500.0, "Charlie Brown", "A", new Date(), new Date())
        );

        // Mock ApiResponse
        Map<String, Object> data = this.apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards");
        ApiResponse<String> successResponse = this.apiBuilderService.successRespuesta(data);

        // Set up mocks for apiBuilderService
        when(apiBuilderService.convertEntityToMapSinCambios(cardEntities, "cards")).thenReturn(data);

        // Call the service method
        ApiResponse<String> response = cardService.getAllCard();

        // Assert
        assertEquals(successResponse, response);
    }

}


/*
    @Test
    public void testGetAllCard_NoData() {
        ApiResponse<String> errorResponse = new ApiResponse<>(null, null, new ApiResponse.ErrorDetails("NO_DATA_AVAILABLE", null));

        when(cardRepository.findAll()).thenReturn(Collections.emptyList());
        when(apiBuilderService.errorRespuesta("NO_DATA_AVAILABLE")).thenReturn(errorResponse);

        ApiResponse<String> response = cardService.getAllCard();

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals("NO_DATA_AVAILABLE", response.getError().getCode());
        verify(cardRepository, times(1)).findAll();
        verify(apiBuilderService, times(1)).errorRespuesta("NO_DATA_AVAILABLE");
    }

    @Test
    public void testGetCardById_Success() {
        String cardId = "1234567890123456";
        CardEntity cardEntity = new CardEntity();
        cardEntity.setIdCard(cardId);

        Map<String, Object> data = new HashMap<>();
        ApiResponse<String> successResponse = new ApiResponse<>(data, new HashMap<>(), null);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(cardEntity));
        when(apiBuilderService.successRespuesta(any())).thenReturn(successResponse);

        ApiResponse<String> response = cardService.getCardById(cardId);

        assertNotNull(response);
        assertNull(response.getError());
        assertNotNull(response.getData());
        verify(cardRepository, times(1)).findById(cardId);
        verify(apiBuilderService, times(1)).successRespuesta(any());
    }


    @Test
    public void testGetCardById_InvalidCardNumber() {
        ApiResponse<String> errorResponse = new ApiResponse<>(null, null, new ApiResponse.ErrorDetails("INVALID_CARD_NUMBER", null));

        when(cardRepository.findById("123")).thenReturn(Optional.empty());
        when(apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER")).thenReturn(errorResponse);

        ApiResponse<String> response = cardService.getCardById("123");

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals("INVALID_CARD_NUMBER", response.getError().getCode());
        verify(cardRepository, times(1)).findById("123");
        verify(apiBuilderService, times(1)).errorRespuesta("INVALID_CARD_NUMBER");
    }

    @Test
    public void testGenerateCard_Success() {
        String productId = "productId";
        String customerId = "customerId";
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("productId", productId);
        requestData.put("customerId", customerId);

        Map<String, Object> data = new HashMap<>();
        ApiResponse<String> successResponse = new ApiResponse<>(data, new HashMap<>(), null);

        when(apiBuilderService.successRespuesta(any())).thenReturn(successResponse);

        ApiResponse<String> response = cardService.generateCard(requestData.toString());

        assertNotNull(response);
        assertNull(response.getError());
        assertNotNull(response.getData());
        verify(apiBuilderService, times(1)).successRespuesta(any());
    }


    @Test
    public void testGenerateCard_InvalidProductId() {
        ApiResponse<String> errorResponse = new ApiResponse<>(null, null, new ApiResponse.ErrorDetails("INTERNAL_SERVER_ERROR", null));

        when(apiBuilderService.errorRespuesta("INTERNAL_SERVER_ERROR")).thenReturn(errorResponse);

        ApiResponse<String> response = cardService.generateCard("12345");

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals("INTERNAL_SERVER_ERROR", response.getError().getCode());
        verify(apiBuilderService, times(1)).errorRespuesta("INTERNAL_SERVER_ERROR");
    }


    @Test
    public void testActivateCard_Success() {
        String cardId = "1234567890123456";
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("cardId", cardId);

        CardEntity cardEntity = new CardEntity();
        cardEntity.setIdCard(cardId);

        Map<String, Object> data = new HashMap<>();
        ApiResponse<String> successResponse = new ApiResponse<>(data, new HashMap<>(), null);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(cardEntity));
        when(cardRepository.activeCard(cardId)).thenReturn(1);
        when(apiBuilderService.convertEntityToMapSinCambios(any(), anyString())).thenReturn(data);
        when(apiBuilderService.successRespuesta(any())).thenReturn(successResponse);

        ApiResponse<String> response = cardService.activateCard(requestData);

        assertNotNull(response);
        assertNull(response.getError());
        assertEquals(data, response.getData());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardRepository, times(1)).activeCard(cardId);
        verify(apiBuilderService, times(1)).convertEntityToMapSinCambios(any(), eq("card_active"));
        verify(apiBuilderService, times(1)).successRespuesta(any());
    }

    @Test
    public void testActivateCard_InvalidFormat() {
        Map<String, Object> requestData = new HashMap<>();

        ApiResponse<String> errorResponse = new ApiResponse<>(null, null, new ApiResponse.ErrorDetails("INVALID_FORMAT_INPUT", null));
        when(apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT")).thenReturn(errorResponse);

        ApiResponse<String> response = cardService.activateCard(requestData);

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals("INVALID_FORMAT_INPUT", response.getError().getCode());
        verify(apiBuilderService, times(1)).errorRespuesta("INVALID_FORMAT_INPUT");
    }


    @Test
    public void testBlockCard_Success() {
        String cardId = "1234567890123456";
        CardEntity cardEntity = new CardEntity();
        cardEntity.setIdCard(cardId);

        Map<String, Object> data = new HashMap<>();
        ApiResponse<String> successResponse = new ApiResponse<>(data, new HashMap<>(), null);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(cardEntity));
        when(cardRepository.blockCard(cardId)).thenReturn(1);
        when(apiBuilderService.convertEntityToMapSinCambios(any(), anyString())).thenReturn(data);
        when(apiBuilderService.successRespuesta(any())).thenReturn(successResponse);

        ApiResponse<String> response = cardService.blockCard(cardId);

        assertNotNull(response);
        assertNull(response.getError());
        assertEquals(data, response.getData());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardRepository, times(1)).blockCard(cardId);
        verify(apiBuilderService, times(1)).convertEntityToMapSinCambios(any(), eq("card_blocked"));
        verify(apiBuilderService, times(1)).successRespuesta(any());
    }

    @Test
    public void testBlockCard_InvalidFormat() {
        String cardId = "123";

        ApiResponse<String> errorResponse = new ApiResponse<>(null, null, new ApiResponse.ErrorDetails("INVALID_FORMAT_INPUT", null));
        when(apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT")).thenReturn(errorResponse);

        ApiResponse<String> response = cardService.blockCard(cardId);

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals("INVALID_FORMAT_INPUT", response.getError().getCode());
        verify(apiBuilderService, times(1)).errorRespuesta("INVALID_FORMAT_INPUT");
    }

    @Test
    public void testReloadBalanceCard_Success() {
        String cardId = "1234567890123456";
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("cardId", cardId);
        requestData.put("balance", "100.0");

        CardEntity cardEntity = new CardEntity();
        cardEntity.setIdCard(cardId);
        cardEntity.setBalance(0.0);

        Map<String, Object> data = new HashMap<>();
        ApiResponse<String> successResponse = new ApiResponse<>(data, new HashMap<>(), null);

        when(cardRepository.getByIdCard(cardId)).thenReturn(cardEntity);
        when(cardRepository.reloadBalance(cardId, 100.0)).thenReturn(1);
        when(apiBuilderService.successRespuesta(any())).thenReturn(successResponse);

        ApiResponse<String> response = cardService.reloadBalanceCard(requestData);

        assertNotNull(response);
        assertNull(response.getError());
        assertNotNull(response.getData());
        verify(cardRepository, times(1)).getByIdCard(cardId);
        verify(cardRepository, times(1)).reloadBalance(cardId, 100.0);
        verify(apiBuilderService, times(1)).successRespuesta(any());
    }


    @Test
    public void testReloadBalanceCard_InvalidFormat() {
        Map<String, Object> requestData = new HashMap<>();

        ApiResponse<String> errorResponse = new ApiResponse<>(null, null, new ApiResponse.ErrorDetails("INVALID_FORMAT_INPUT", null));
        when(apiBuilderService.errorRespuesta("INVALID_FORMAT_INPUT")).thenReturn(errorResponse);

        ApiResponse<String> response = cardService.reloadBalanceCard(requestData);

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals("INVALID_FORMAT_INPUT", response.getError().getCode());
        verify(apiBuilderService, times(1)).errorRespuesta("INVALID_FORMAT_INPUT");
    }


    @Test
    public void testGetByCardBalance_Success() {
        String cardId = "1234567890123456";
        CardEntity cardEntity = new CardEntity();
        cardEntity.setIdCard(cardId);
        cardEntity.setBalance(100.0);

        Map<String, Object> data = new HashMap<>();
        ApiResponse<String> successResponse = new ApiResponse<>(data, new HashMap<>(), null);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(cardEntity));
        when(apiBuilderService.convertEntityToMap(any(), anyString())).thenReturn(data);
        when(apiBuilderService.successRespuesta(any())).thenReturn(successResponse);

        ApiResponse<String> response = cardService.getByCardBalance(cardId);

        assertNotNull(response);
        assertNull(response.getError());
        assertEquals(data, response.getData());
        verify(cardRepository, times(1)).findById(cardId);
        verify(apiBuilderService, times(1)).convertEntityToMap(any(), eq("card"));
        verify(apiBuilderService, times(1)).successRespuesta(any());
    }

    @Test
    public void testGetByCardBalance_InvalidCardNumber() {
        String cardId = "123";

        ApiResponse<String> errorResponse = new ApiResponse<>(null, null, new ApiResponse.ErrorDetails("INVALID_CARD_NUMBER", null));
        when(apiBuilderService.errorRespuesta("INVALID_CARD_NUMBER")).thenReturn(errorResponse);

        ApiResponse<String> response = cardService.getByCardBalance(cardId);

        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals("INVALID_CARD_NUMBER", response.getError().getCode());
        verify(apiBuilderService, times(1)).errorRespuesta("INVALID_CARD_NUMBER");
    }
*/


