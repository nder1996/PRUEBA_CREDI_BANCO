package crediBanco.controller;


import crediBanco.model.response.ApiResponse;
import crediBanco.model.response.ApiResponseJson;
import crediBanco.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
