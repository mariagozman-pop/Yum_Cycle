/*
package com.example.leftover_rescue.BARCODE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

    private final BarcodeService barcodeService;

    public BarcodeController(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    // Endpoint to receive the barcode from the Android app
    @PostMapping("/scan")
    public ResponseEntity<?> scanBarcode(@RequestBody String barcode) {
        try {
            // Call service method to handle the barcode
            ScannedProduct product = barcodeService.getProductDetails(barcode);
            return ResponseEntity.ok(product); // Return product details
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving product data.");
        }
    }
}
*/
