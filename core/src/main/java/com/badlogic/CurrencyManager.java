package com.badlogic;

public class CurrencyManager {
    private static CurrencyManager instance;
    private int currentBalance;

    private CurrencyManager() {
        currentBalance = 0; // 初始货币数量
    }

    public static CurrencyManager getInstance() {
        if (instance == null) {
            instance = new CurrencyManager();
        }
        return instance;
    }

    public void collectCurrency(int amount) {
        if (amount > 0) {
            currentBalance += amount;
        }
    }

    public boolean spendCurrency(int amount) {
        if (amount > 0 && currentBalance >= amount) {
            currentBalance -= amount;
            return true;
        }
        return false;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public boolean canAfford(int amount) {
        return currentBalance >= amount;
    }
}
