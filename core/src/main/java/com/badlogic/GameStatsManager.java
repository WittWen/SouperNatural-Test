package com.badlogic;

public class GameStatsManager {
    private static GameStatsManager instance;

    // 基础属性
    private int maxHungryPointLevel = 0;  // 最大生命值等级
    private int attackDamageLevel = 0;    // 攻击伤害等级
    private int moveSpeedLevel = 0;       // 移动速度等级
    private int coinRangeLevel = 0;       // 金币吸取范围等级
    private int damageReductionLevel = 0; // 伤害减免等级
    private boolean hasRevive = false;    // 复活次数（只有一次）

    // 升级花费定义
    private static final int[] MAX_HUNGRY_POINT_COSTS = {50, 100, 200};
    private static final int[] ATTACK_DAMAGE_COSTS = {50, 100, 200};
    private static final int[] MOVE_SPEED_COSTS = {50, 100, 200};
    private static final int[] COIN_RANGE_COSTS = {50, 100, 200};
    private static final int[] DAMAGE_REDUCTION_COSTS = {50, 100, 200};
    private static final int REVIVE_COST = 200;

    // 各属性的基础值和每级增加值
    private static final float BASE_MAX_HUNGRY_POINT = 6;    // 基础最大生命值
    private static final float PER_LEVEL_MAX_HUNGRY_POINT = 2; // 每级增加生命值

    private static final float BASE_ATTACK_DAMAGE = 1f;      // 基础攻击伤害
    private static final float PER_LEVEL_ATTACK_DAMAGE = 0.5f; // 每级增加伤害

    private static final float BASE_MOVE_SPEED = 90;       // 基础移动速度
    private static final float PER_LEVEL_MOVE_SPEED = 5;   // 每级增加速度

    private static final float BASE_MAGNET_RANGE = 0;        // 基础金币吸取范围
    private static final float PER_LEVEL_MAGNET_RANGE = 5;   // 每级增加范围

    private static final float BASE_DAMAGE_REDUCTION = 0;   // 基础伤害减免
    private static final float PER_LEVEL_DAMAGE_REDUCTION = 0.05f; // 每级增加减免（5%）

    private static final int MAX_LEVEL = 3;  // 最大等级（除复活外）

    private float hungryPoint;

    private GameStatsManager() {
        resetStats();
    }

    public static GameStatsManager getInstance() {
        if (instance == null) {
            instance = new GameStatsManager();
        }
        return instance;
    }

    // 重置所有属性
    public void resetStats() {
        maxHungryPointLevel = 0;
        attackDamageLevel = 0;
        moveSpeedLevel = 0;
        coinRangeLevel = 0;
        damageReductionLevel = 0;
        hasRevive = false;
    }

    // 获取升级花费的方法
    public int getMaxHungryPointUpgradeCost() {
        return maxHungryPointLevel >= MAX_LEVEL ? -1 : MAX_HUNGRY_POINT_COSTS[maxHungryPointLevel];
    }

    public int getAttackDamageUpgradeCost() {
        return attackDamageLevel >= MAX_LEVEL ? -1 : ATTACK_DAMAGE_COSTS[attackDamageLevel];
    }

    public int getMoveSpeedUpgradeCost() {
        return moveSpeedLevel >= MAX_LEVEL ? -1 : MOVE_SPEED_COSTS[moveSpeedLevel];
    }

    public int getCoinRangeUpgradeCost() {
        return coinRangeLevel >= MAX_LEVEL ? -1 : COIN_RANGE_COSTS[coinRangeLevel];
    }

    public int getDamageReductionUpgradeCost() {
        return damageReductionLevel >= MAX_LEVEL ? -1 : DAMAGE_REDUCTION_COSTS[damageReductionLevel];
    }

    public int getReviveCost() {
        return hasRevive ? -1 : REVIVE_COST;
    }

    // 尝试升级的方法（包含货币检查）
    public boolean tryUpgradeMaxHungryPoint() {
        int cost = getMaxHungryPointUpgradeCost();
        if (cost > 0 && CurrencyManager.getInstance().spendCurrency(cost)) {
            maxHungryPointLevel++;
            return true;
        }
        return false;
    }

    public boolean tryUpgradeAttackDamage() {
        int cost = getAttackDamageUpgradeCost();
        if (cost > 0 && CurrencyManager.getInstance().spendCurrency(cost)) {
            attackDamageLevel++;
            return true;
        }
        return false;
    }

    public boolean tryUpgradeMoveSpeed() {
        int cost = getMoveSpeedUpgradeCost();
        if (cost > 0 && CurrencyManager.getInstance().spendCurrency(cost)) {
            moveSpeedLevel++;
            return true;
        }
        return false;
    }

    public boolean tryUpgradeCoinRange() {
        int cost = getCoinRangeUpgradeCost();
        if (cost > 0 && CurrencyManager.getInstance().spendCurrency(cost)) {
            coinRangeLevel++;
            return true;
        }
        return false;
    }

    public boolean tryUpgradeDamageReduction() {
        int cost = getDamageReductionUpgradeCost();
        if (cost > 0 && CurrencyManager.getInstance().spendCurrency(cost)) {
            damageReductionLevel++;
            return true;
        }
        return false;
    }

    public boolean tryUnlockRevive() {
        if (!hasRevive && CurrencyManager.getInstance().spendCurrency(REVIVE_COST)) {
            hasRevive = true;
            return true;
        }
        return false;
    }

    // 检查是否可以升级（包含等级和货币检查）
    public boolean canUpgradeMaxHungryPoint() {
        int cost = getMaxHungryPointUpgradeCost();
        return cost > 0 && CurrencyManager.getInstance().canAfford(cost);
    }

    public boolean canUpgradeAttackDamage() {
        int cost = getAttackDamageUpgradeCost();
        return cost > 0 && CurrencyManager.getInstance().canAfford(cost);
    }

    public boolean canUpgradeMoveSpeed() {
        int cost = getMoveSpeedUpgradeCost();
        return cost > 0 && CurrencyManager.getInstance().canAfford(cost);
    }

    public boolean canUpgradeCoinRange() {
        int cost = getCoinRangeUpgradeCost();
        return cost > 0 && CurrencyManager.getInstance().canAfford(cost);
    }

    public boolean canUpgradeDamageReduction() {
        int cost = getDamageReductionUpgradeCost();
        return cost > 0 && CurrencyManager.getInstance().canAfford(cost);
    }

    public boolean canUnlockRevive() {
        return !hasRevive && CurrencyManager.getInstance().canAfford(REVIVE_COST);
    }

    // 获取当前值的方法
    public float getCurrentMaxHungryPoint() {
        return BASE_MAX_HUNGRY_POINT + (maxHungryPointLevel * PER_LEVEL_MAX_HUNGRY_POINT);
    }

    public float getCurrentAttackDamage() {
        return BASE_ATTACK_DAMAGE + (attackDamageLevel * PER_LEVEL_ATTACK_DAMAGE);
    }

    public float getCurrentMoveSpeed() {
        return BASE_MOVE_SPEED + (moveSpeedLevel * PER_LEVEL_MOVE_SPEED);
    }

    public float getCurrentCoinRange() {
        return BASE_MAGNET_RANGE + (coinRangeLevel * PER_LEVEL_MAGNET_RANGE);
    }

    public float getCurrentDamageReduction() {
        return BASE_DAMAGE_REDUCTION + (damageReductionLevel * PER_LEVEL_DAMAGE_REDUCTION);
    }

    public boolean hasRevive() {
        return hasRevive;
    }

    // 获取等级的方法
    public int getMaxHungryPointLevel() { return maxHungryPointLevel; }
    public int getAttackDamageLevel() { return attackDamageLevel; }
    public int getMoveSpeedLevel() { return moveSpeedLevel; }
    public int getCoinRangeLevel() { return coinRangeLevel; }
    public int getDamageReductionLevel() { return damageReductionLevel; }

    // 计算实际受到的伤害（考虑伤害减免）
    public float calculateActualDamage(float incomingDamage) {
        float reduction = getCurrentDamageReduction();
        return incomingDamage * (1 - reduction);
    }
}
