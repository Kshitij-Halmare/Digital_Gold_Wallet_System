package com.example.Digital_Gold_Wallet_System.service;

import com.example.Digital_Gold_Wallet_System.entity.*;
import com.example.Digital_Gold_Wallet_System.entity.enums.*;
import com.example.Digital_Gold_Wallet_System.exception.InsufficientGoldException;
import com.example.Digital_Gold_Wallet_System.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class VirtualGoldHoldingsService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private PaymentsRepo paymentsRepo;

    @Autowired
    private VendorBranchesRepo vendorBranchesRepo;

    @Autowired
    private VirtualGoldHoldingsRepo holdingRepo;

    @Autowired
    private PhysicalGoldTransactionsRepo txnRepo;

    @Autowired
    private VendorsRepo vendorsRepo;

    @Autowired
    private VirtualGoldHoldingsRepo virtualGoldHoldingsRepo;

    @Autowired
    private TransactionHistoryRepo transactionHistoryRepo;

    @Transactional
    public void convertToPhysical(Integer holdingId) {

        VirtualGoldHoldings holding = holdingRepo.findById(holdingId).orElseThrow(() -> new RuntimeException("Holding not found"));

        if (HoldingStatus.CONVERTED.equals(holding.getHoldingStatus())) {
            throw new RuntimeException("Already converted");
        }

        if (holding.getUser() == null || holding.getUser().getAddress() == null) {
            throw new RuntimeException("Delivery address required");
        }

        PhysicalGoldTransactions txn = new PhysicalGoldTransactions();
        txn.setUser(holding.getUser());
        txn.setBranch(holding.getBranch());
        txn.setQuantity(holding.getQuantity());
        txn.setCreatedAt(LocalDateTime.now());
        txn.setDeliveryAddress(holding.getUser().getAddress());

        txnRepo.save(txn);

        holding.setHoldingStatus(HoldingStatus.CONVERTED);
        holdingRepo.save(holding);
    }

    @Transactional
    public void buyVirtualGoldHoldings(Integer userId, BigDecimal qty, Integer branchId, PaymentMethod pMethod) {

        Users user = usersRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        VendorBranches branch = vendorBranchesRepo.findById(branchId).orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        Vendors vendor = branch.getVendors();

        if(qty.compareTo(branch.getQuantity()) == 1) {
            throw new InsufficientGoldException("Not enough gold in the given branch...");
        }

        if((qty.multiply(vendor.getCurrentGoldPrice())).compareTo(user.getBalance()) == 1) {
            throw new InsufficientGoldException("User balance is low...");
        }

        VirtualGoldHoldings holding = new VirtualGoldHoldings();
        holding.setQuantity(qty);
        holding.setCreatedAt(LocalDateTime.now());
        holding.setUser(user);
        holding.setBranch(branch);
        holding.setHoldingStatus(HoldingStatus.ACTIVE);

        holdingRepo.save(holding);

        BigDecimal remainingQty = branch.getQuantity().subtract(qty);
        branch.setQuantity(remainingQty);
        vendorBranchesRepo.save(branch);

        BigDecimal remainingQty2 = vendor.getTotalGoldQuantity().subtract(qty);
        vendor.setTotalGoldQuantity(remainingQty2);
        vendorsRepo.save(vendor);

        user.setBalance(user.getBalance().subtract(qty.multiply(vendor.getCurrentGoldPrice())));
        usersRepo.save(user);

        Payments payment = new Payments();
        payment.setAmount(qty.multiply(vendor.getCurrentGoldPrice()));
        payment.setPaymentMethod(pMethod);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentTransactionType(PaymentTransactionType.DEBITED);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUser(user);

        paymentsRepo.save(payment);

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setCreatedAt(LocalDateTime.now());
        transactionHistory.setQuantity(qty);
        transactionHistory.setAmount(qty.multiply(vendor.getCurrentGoldPrice()));
        transactionHistory.setTransactionType(TransactionType.BUY);
        transactionHistory.setTransactionStatus(TransactionStatus.SUCCESS);
        transactionHistory.setUser(user);
        transactionHistory.setBranch(branch);

        transactionHistoryRepo.save(transactionHistory);


    }

    @Transactional
    public void sellVirtualGoldHoldings(Integer holdingId, PaymentMethod pMethod) {

        TransactionHistory txn = transactionHistoryRepo.findById(holdingId).orElseThrow(() -> new ResourceNotFoundException("TransactionHistory not found"));

        Users user = usersRepo.findById(txn.getUser().getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        VendorBranches branch = vendorBranchesRepo.findById(txn.getBranch().getBranchId()).orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        Vendors vendor = branch.getVendors();

        VirtualGoldHoldings holding = virtualGoldHoldingsRepo.findById(holdingId).orElseThrow(() -> new ResourceNotFoundException("Holding not found"));
        holding.setHoldingStatus(HoldingStatus.SOLD);
        holdingRepo.save(holding);

        BigDecimal remainingQty = branch.getQuantity().add(holding.getQuantity());
        branch.setQuantity(remainingQty);
        vendorBranchesRepo.save(branch);

        BigDecimal remainingQty2 = vendor.getTotalGoldQuantity().add(holding.getQuantity());
        vendor.setTotalGoldQuantity(remainingQty2);
        vendorsRepo.save(vendor);

        user.setBalance(user.getBalance().add(holding.getQuantity().multiply(vendor.getCurrentGoldPrice())));
        usersRepo.save(user);

        Payments payment = new Payments();
        payment.setAmount(holding.getQuantity().multiply(vendor.getCurrentGoldPrice()));
        payment.setPaymentMethod(pMethod);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentTransactionType(PaymentTransactionType.CREDITED);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUser(user);

        paymentsRepo.save(payment);

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setCreatedAt(LocalDateTime.now());
        transactionHistory.setQuantity(holding.getQuantity());
        transactionHistory.setAmount(holding.getQuantity().multiply(vendor.getCurrentGoldPrice()));
        transactionHistory.setTransactionType(TransactionType.SELL);
        transactionHistory.setTransactionStatus(TransactionStatus.SUCCESS);
        transactionHistory.setUser(user);
        transactionHistory.setBranch(branch);

        transactionHistoryRepo.save(transactionHistory);


    }
}
