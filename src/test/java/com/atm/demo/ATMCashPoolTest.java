package com.atm.demo;

import com.atm.demo.core.ATMCashPool;
import com.atm.demo.core.Cash;
import com.atm.demo.core.CashWithdrawRequest;
import com.atm.demo.core.Denomination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ATMCashPoolTest {

    private ATMCashPool atmCashPool;

    @BeforeEach
    void setUp() {
        List<Cash> cashpool = List.of(new Cash(Denomination.DENOM_500,4), new Cash(Denomination.DENOM_100,20));
        atmCashPool = new ATMCashPool(cashpool);
    }


    @Test
    void test_cashWithdrawalWithValidAmount() {
      CashWithdrawRequest withdrawRequest =  atmCashPool.processCashWithdrawalReq(3000);
      Assertions.assertNotNull(withdrawRequest.getRequestId());
      Assertions.assertNotNull(withdrawRequest.getWithdrawalCashMap());

      Integer balance = atmCashPool.getCashPool().entrySet().stream().mapToInt(den -> den.getKey() * den.getValue()).sum();
      Assertions.assertEquals(1000, balance);

    }


    @Test
    void test_Remaining_deniminationsAfterTxn() {
        CashWithdrawRequest withdrawRequest =  atmCashPool.processCashWithdrawalReq(3000);
        Assertions.assertNotNull(withdrawRequest.getRequestId());
        Assertions.assertNotNull(withdrawRequest.getWithdrawalCashMap());

        Integer remaining500Denon = atmCashPool.getCashPool().get(Denomination.DENOM_500.getValue());
        Assertions.assertEquals(0, remaining500Denon);

        Integer remaining100Denon = atmCashPool.getCashPool().get(Denomination.DENOM_100.getValue());
        Assertions.assertEquals(10, remaining100Denon);
    }




    @Test
    void test_cashWithdrawalwithLowBalance() {
        Assertions.assertThrows(
                RuntimeException.class,
                () ->  atmCashPool.processCashWithdrawalReq(5000));
    }

    @Test
    void test_cashWithdrawalwithNoMoneyInATM() {
        atmCashPool = new ATMCashPool(new ArrayList<>());
        Assertions.assertThrows(
                RuntimeException.class,
                () ->  atmCashPool.processCashWithdrawalReq(5000));
    }

}
