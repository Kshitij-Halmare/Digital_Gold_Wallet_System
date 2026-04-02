package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.Projection.VendorProjection;
import com.example.Digital_Gold_Wallet_System.Projection.VendorsProjection;
import com.example.Digital_Gold_Wallet_System.entity.Vendors;
//import com.example.Digital_Gold_Wallet_System.projection.VendorsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.util.List;
@CrossOrigin(origins = "*")
@RepositoryRestResource(
        path = "vendors",
        collectionResourceRel = "vendors",
        excerptProjection = VendorProjection.class
)
@CrossOrigin(origins = "http://localhost:9090")
public interface VendorsRepo extends JpaRepository<Vendors, Integer> {
    List<Vendors> findByVendorName(String vendorName);
    //List<VendorsProjection> findAllProjectedBy();

    //Search bar
    @RestResource(path = "findByVendorNameContainingIgnoreCase")
    List<Vendors> findByVendorNameContainingIgnoreCase(@Param("keyword") String keyword);

    //Price filter
    @RestResource(path = "findByCurrentGoldPriceBetween")
    List<Vendors> findByCurrentGoldPriceBetween(@Param("minPrice")BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @RestResource(path = "findByCurrentGoldPriceLessThanEqual")
    List<Vendors> findByCurrentGoldPriceLessThanEqual(@Param("maxPrice") BigDecimal maxPrice);

    @RestResource(path = "findByCurrentGoldPriceGreaterThanEqual")
    List<Vendors> findByCurrentGoldPriceGreaterThanEqual(@Param("minPrice")BigDecimal minPrice);

    //Quantity filter
    @RestResource(path = "findByTotalGoldQuantityBetween")
    List<Vendors> findByTotalGoldQuantityBetween(@Param("minQty") BigDecimal minQty,@Param("maxQty") BigDecimal maxQty);
}
