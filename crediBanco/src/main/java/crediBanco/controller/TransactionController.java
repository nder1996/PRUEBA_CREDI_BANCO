package crediBanco.controller;


import crediBanco.model.response.ApiResponse;
import crediBanco.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {



    @Autowired
    private TransactionService transactionService;


    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<String>> purchase(@RequestBody Map<String, Object> request) {
        return new ResponseEntity<>(transactionService.createTransactionCard(request), HttpStatus.OK);
    }

    @GetMapping("/transaction/{idTransaction}")
    public ResponseEntity<ApiResponse<String>> getByTransactionCard(@PathVariable Integer idTransaction) {
        return new ResponseEntity<>(transactionService.getByTransactionCard(idTransaction), HttpStatus.OK);
    }


    @PostMapping("/anulation")
    public ResponseEntity<ApiResponse<String>> anulateTransaction(@RequestBody Map<String, Object> requestData) {
        return new ResponseEntity<>(transactionService.anulateTransaction(requestData),HttpStatus.OK);
    }


}
