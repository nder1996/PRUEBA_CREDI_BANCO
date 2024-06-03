package crediBanco.controller;



import crediBanco.model.response.ApiResponse;
import crediBanco.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @DeleteMapping("/{cardId}")
    public ResponseEntity<ApiResponse<String>> blockCard(@PathVariable String cardId) {
        return new ResponseEntity<>(cardService.blockCard(cardId),HttpStatus.OK);
    }

    @PostMapping("/balance")
    public ResponseEntity<ApiResponse<String>> reloadBalanceCard(@RequestBody Map<String, Object> request) {
        return new ResponseEntity<>(cardService.reloadBalanceCard(request),HttpStatus.OK);
    }


    @GetMapping("/balance/{cardId}")
    public ResponseEntity<ApiResponse<String>> getByCardBalance(@PathVariable String cardId) {
        return new ResponseEntity<>(cardService.getByCardBalance(cardId), HttpStatus.OK);
    }


}
