package com.example.Digital_Gold_Wallet_System.repository;

import com.example.Digital_Gold_Wallet_System.entity.Vendors;
//import com.example.Digital_Gold_Wallet_System.projection.VendorsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(
        path = "vendors",
        collectionResourceRel = "vendors"
)
public interface VendorsRepo extends JpaRepository<Vendors, Integer> {
    List<Vendors> findByVendorName(String vendorName);

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
