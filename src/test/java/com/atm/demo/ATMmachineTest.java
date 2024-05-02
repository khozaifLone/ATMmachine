package com.atm.demo;

import com.atm.demo.core.ATMMachine;
import com.atm.demo.core.Cash;
import com.atm.demo.core.CashWithdrawRequest;
import com.atm.demo.core.Denomination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ATMmachineTest {

    private ATMMachine atmMachine;

    @BeforeEach
    void setUp() {
        List<Cash> cashpool = List.of(new Cash(Denomination.DENOM_500, 4), new Cash(Denomination.DENOM_100, 20));
        atmMachine = new ATMMachine(cashpool);
    }

    @Test
    void test_cashWithdrawalwithValidAmount() {
        atmMachine.withDrawCash(500);
        Map<String, CashWithdrawRequest> processedTxns = atmMachine.getTransactionsOnHold();
        Assertions.assertNotNull(processedTxns);
    }


    @Test
    void test_cashWithdrawalwithInvalidAmount() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> atmMachine.withDrawCash(5186));
    }

}
