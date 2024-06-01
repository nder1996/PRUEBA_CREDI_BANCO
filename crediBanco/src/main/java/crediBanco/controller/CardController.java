package crediBanco.controller;


import crediBanco.entity.CardEntity;
import crediBanco.model.response.ApiResponse;
import crediBanco.model.response.ApiResponseJson;
import crediBanco.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card")
public class CardController {


    @Autowired
    CardService cardService;


    @GetMapping("/getAllCard")
    public ResponseEntity<ApiResponse<String>> getAllCard() {
        return new ResponseEntity<>(cardService.getAllCard(), HttpStatus.OK);
    }

    @GetMapping("/getCardById/{cardId}")
    public ResponseEntity<ApiResponse<String>> getCardById(@PathVariable String cardId) {
        return new ResponseEntity<>(cardService.getCardById(cardId), HttpStatus.OK);
    }

    @GetMapping("/{productId}/number")
    public ResponseEntity<ApiResponse<String>> generateCardNumber(@PathVariable String productId) {
        return new ResponseEntity<>(cardService.generateCard(productId),HttpStatus.OK);
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse<String>> activarTarjeta(@RequestBody Map<String, Object> requestData) {
        return new ResponseEntity<>(cardService.activateCard(requestData),HttpStatus.OK);
    }
}
