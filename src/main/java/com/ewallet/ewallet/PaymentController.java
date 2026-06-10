package com.ewallet.ewallet;

import com.ewallet.wallet.WalletService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private WalletService walletService;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    // Step 1: Create Razorpay Order
    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestBody Map<String, Object> request) {
        try {
            int amount = Integer.parseInt(request.get("amount").toString());

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // paise mein
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", amount);
            response.put("currency", "INR");
            response.put("keyId", keyId);

            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Step 2: Verify Payment & Add Money to Wallet
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestBody Map<String, Object> request) {
        try {
            String razorpayOrderId = (String) request.get("razorpayOrderId");
            String razorpayPaymentId = (String) request.get("razorpayPaymentId");
            String razorpaySignature = (String) request.get("razorpaySignature");
            double amount = Double.parseDouble(request.get("amount").toString());
            Long userId = Long.valueOf(request.get("userId").toString());

            // Signature verify karo
            String payload = razorpayOrderId + "|" + razorpayPaymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    keySecret.getBytes(), "HmacSHA256"));
            String generated = HexFormat.of()
                    .formatHex(mac.doFinal(payload.getBytes()));

            if (!generated.equals(razorpaySignature)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid payment signature!"));
            }

            // Wallet mein paisa add karo
            walletService.addMoney(userId, amount);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Payment verified! ₹" + amount + " added to wallet",
                    "amount", amount
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}