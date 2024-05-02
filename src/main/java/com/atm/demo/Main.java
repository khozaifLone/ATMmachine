package com.atm.demo;

import com.atm.demo.core.ATMMachine;
import com.atm.demo.core.Cash;
import com.atm.demo.core.CashWithdrawRequest;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.atm.demo.core.Denomination.DENOM_100;
import static com.atm.demo.core.Denomination.DENOM_500;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        List<Cash> atmCashPool = List.of(new Cash(DENOM_500,4), new Cash(DENOM_100,60));
        ATMMachine atmMachine = new ATMMachine(atmCashPool);
        try {
            var transaction1 = Thread.ofPlatform().name("transaction1").start(() -> {
                logger.info("Withdrawing 5000 rupees");
                atmMachine.withDrawCash(5000);
            });
            var transaction2 = Thread.ofPlatform().name("transaction2").start(() -> {
                logger.info("Withdrawing 1200 rupees");
                atmMachine.withDrawCash(1200);
            });
            transaction1.join();
            transaction2.join();

            Map<String, CashWithdrawRequest> requestsOnHold = atmMachine.getTransactionsOnHold();
            logger.info(requestsOnHold.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
