package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.Payments;
import com.example.Digital_Gold_Wallet_System.entity.Users;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentMethod;
import com.example.Digital_Gold_Wallet_System.entity.enums.PaymentStatus;
import com.example.Digital_Gold_Wallet_System.repository.PaymentsRepo;
import com.example.Digital_Gold_Wallet_System.repository.UsersRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentsRepo paymentRepo;

    @Autowired
    private UsersRepo userRepo;

    private Users createUser() {
        Users u = new Users();
        u.setName("Test User");
        u.setEmail("test@mail.com");
        return userRepo.save(u);
    }

    private Payments createPayment(Users user, BigDecimal amount, PaymentStatus status, PaymentMethod method) {
        Payments p = new Payments();
        p.setAmount(amount);
        p.setPaymentStatus(status);
        p.setPaymentMethod(method);
        p.setCreatedAt(LocalDateTime.now());
        p.setUser(user);
        return paymentRepo.save(p);
    }

    @Test
    void testGetAllPayments_Positive() throws Exception {
        Users u = createUser();
        createPayment(u, BigDecimal.valueOf(100), PaymentStatus.SUCCESS, PaymentMethod.UPI);

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists());
    }

    @Test
    void testGetAllPayments_Empty() throws Exception {
        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPaymentById_Positive() throws Exception {
        Users u = createUser();
        Payments p = createPayment(u, BigDecimal.valueOf(100), PaymentStatus.SUCCESS, PaymentMethod.UPI);

        mockMvc.perform(get("/payments/" + p.getPaymentId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPaymentById_NotFound() throws Exception {
        mockMvc.perform(get("/payments/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPaymentsByUser_Positive() throws Exception {
        Users u = createUser();
        createPayment(u, BigDecimal.valueOf(100), PaymentStatus.SUCCESS, PaymentMethod.UPI);

        mockMvc.perform(get("/payments/search/findByUser_UserId")
                        .param("userId", u.getUserId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPaymentsByUser_Empty() throws Exception {
        mockMvc.perform(get("/payments/search/findByUser_UserId")
                        .param("userId", "999"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSuccessfullPayments_Positive() throws Exception{
        Users u = createUser();
        createPayment(u, BigDecimal.valueOf(100), PaymentStatus.SUCCESS, PaymentMethod.UPI);

        mockMvc.perform(get("/payments/search/findByPaymentStatus")
                .param("paymentStatus","SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists());
    }

    @Test
    void testGetSuccessfullPayments_Negative() throws Exception{
        mockMvc.perform(get("/payments/search/findByPaymentStatus")
                .param("paymentStatus","SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").isEmpty());
    }

    @Test
    void testGetFailedPayments_Positive() throws Exception{
        Users user = createUser();
        createPayment(user,BigDecimal.valueOf(100),PaymentStatus.FAILED,PaymentMethod.UPI);

        mockMvc.perform(get("/payments/search/findByPaymentStatus")
                .param("paymentStatus","FAILED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists());
    }

    @Test
    void testGetFailedPayments_Negative() throws Exception{

        mockMvc.perform(get("/payments/search/findByPaymentStatus")
                        .param("paymentStatus","FAILED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").isEmpty());
    }

    @Test
    void testGetPaymentsByMethod_Positive() throws Exception{
        Users user = createUser();
        createPayment(user,BigDecimal.valueOf(100),PaymentStatus.FAILED,PaymentMethod.UPI);

        mockMvc.perform(get("/payments/search/findByPaymentMethod")
                        .param("paymentMethod","UPI"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.payments").exists());
    }

    @Test
    void testGetPaymentsByMethod_Negative() throws Exception{

        mockMvc.perform(get("/payments/search/findByPaymentMethod")
                        .param("paymentMethod","XYZ"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddPayment_Positive() throws Exception {

        Users user = createUser();

        String json = """
{
  "amount": 100,
  "paymentMethod": "UPI",
  "paymentStatus": "SUCCESS",
  "paymentTransactionType": "CREDIT",
  "createdAt": "2026-03-27T10:00:00",
  "_links": {
    "user": {
      "href": "http://localhost/users/%d"
    }
  }
}
""".formatted(user.getUserId());

        mockMvc.perform(post("/payments")
                        .contentType("application/json")
                        .accept("application/hal+json")
                        .content(json))
                .andExpect(status().isCreated());
    }
}
