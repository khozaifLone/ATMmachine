package com.atm.demo.core;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@ToString
public class CashWithdrawRequest {

    @Getter
    private UUID requestId;

    private Map<Integer, Integer> cashWithdrawal;

    public CashWithdrawRequest(){
        requestId =  UUID.randomUUID();
        cashWithdrawal = new HashMap<>();
    }

    public void addAmount(Integer denomination, Integer count){
        if(denomination == null || count == null && count <=0){
            throw new RuntimeException("Invalid amount being added");
        }
        cashWithdrawal.put(denomination, count);
    }

    public Map<Integer, Integer> getWithdrawalCashMap(){
        return Collections.unmodifiableMap(cashWithdrawal);
    }
}
