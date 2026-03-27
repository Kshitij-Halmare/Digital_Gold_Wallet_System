package com.example.Digital_Gold_Wallet_System.APIs;

import com.example.Digital_Gold_Wallet_System.entity.Addresses;
import com.example.Digital_Gold_Wallet_System.repository.AddressesRepo;
import io.swagger.v3.core.util.Json;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AddressesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AddressesRepo addressesRepo;

    private Addresses savedAddress;

    @BeforeEach
    void setUp() {
//        addressesRepo.deleteAll();

        Addresses address = new Addresses();
        address.setStreet("123 Main St");
        address.setCity("Mumbai");
        address.setState("Maharashtra");
        address.setPostalCode("400001");
        address.setCountry("India");

        savedAddress = addressesRepo.save(address);
    }

    // GET all addresses
    @Test
    public void testGetAllAddresses() throws Exception {
        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.addresseses").isArray());
    }

    // GET address by ID
    @Test
    public void testGetAddressById() throws Exception {
        mockMvc.perform(get("/addresses/" + savedAddress.getAddressId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("123 Main St"))
                .andExpect(jsonPath("$.city").value("Mumbai"))
                .andExpect(jsonPath("$.state").value("Maharashtra"))
                .andExpect(jsonPath("$.postalCode").value("400001"))
                .andExpect(jsonPath("$.country").value("India"));
    }

    // GET by invalid ID — 400 (your exception handler)
    @Test
    public void testGetAddressByInvalidId() throws Exception {
        mockMvc.perform(get("/addresses/9999"))
                .andExpect(status().isBadRequest());
    }

    // POST — Spring Data REST returns 201 with empty body + Location header
    //           so we follow the Location to verify fields
    @Test
    public void testCreateAddress() throws Exception {
        String json = """
                {
                    "street": "456 Park Ave",
                    "city": "Pune",
                    "state": "Maharashtra",
                    "postalCode": "411001",
                    "country": "India"
                }
                """;

        String location = mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location"); // e.g. http://localhost/addresses/85

        // extract path from full URL and GET it
        String path = location.substring(location.indexOf("/addresses"));

        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("456 Park Ave"))
                .andExpect(jsonPath("$.city").value("Pune"))
                .andExpect(jsonPath("$.postalCode").value("411001"));
    }

    // PUT — same as POST, empty body returned, follow Location to verify
    @Test
    public void testUpdateAddress() throws Exception {
        String json = """
            {
                "street": "789 New Road",
                "city": "Delhi",
                "state": "Delhi",
                "postalCode": "110001",
                "country": "India"
            }
            """;

        mockMvc.perform(put("/addresses/" + savedAddress.getAddressId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent()); // 204

        // verify fields by GETting the same ID
        mockMvc.perform(get("/addresses/" + savedAddress.getAddressId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("789 New Road"))
                .andExpect(jsonPath("$.city").value("Delhi"))
                .andExpect(jsonPath("$.postalCode").value("110001"));
    }

    // PATCH — partial update, follow Location to verify
    @Test
    public void testPatchAddress() throws Exception {
        String json = """
            {
                "city": "Nagpur",
                "postalCode": "440001"
            }
            """;

        mockMvc.perform(patch("/addresses/" + savedAddress.getAddressId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent()); // 204

        mockMvc.perform(get("/addresses/" + savedAddress.getAddressId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Nagpur"))
                .andExpect(jsonPath("$.postalCode").value("440001"))
                .andExpect(jsonPath("$.street").value("123 Main St")); // unchanged
    }

    // DELETE — 204 no content
    @Test
    public void testDeleteAddress() throws Exception {
        mockMvc.perform(delete("/addresses/" + savedAddress.getAddressId()))
                .andExpect(status().isNoContent());
    }

    // DELETE — then GET returns 400 (your exception handler)
    @Test
    public void testGetAfterDelete() throws Exception {
        mockMvc.perform(delete("/addresses/" + savedAddress.getAddressId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/addresses/" + savedAddress.getAddressId()))
                .andExpect(status().isBadRequest());
    }

    // Count — POST a second record then verify list has 2
    @Test
    public void testAddressCount() throws Exception {
        String json = """
            {
                "street": "101 Lake View",
                "city": "Chennai",
                "state": "Tamil Nadu",
                "postalCode": "600001",
                "country": "India"
            }
            """;
        List<Addresses> result = addressesRepo.findAll();
        mockMvc.perform(post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());



        mockMvc.perform(get("/addresses?size=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.addresseses.length()").value(result.size()+1));
    }
}
