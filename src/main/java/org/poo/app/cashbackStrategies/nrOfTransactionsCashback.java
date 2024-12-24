package org.poo.app.cashbackStrategies;

import lombok.Getter;
import lombok.Setter;
import org.poo.app.commerciants.ClothesCommerciant;
import org.poo.app.commerciants.FoodCommerciant;
import org.poo.app.commerciants.TechCommerciant;

@Getter @Setter
public final class nrOfTransactionsCashback implements CashbackStrategy {
    public double getCashback(FoodCommerciant commerciant, double amount) {
        return amount * 2 / 100;
    }

    public double getCashback(ClothesCommerciant commerciant, double amount) {
        return amount * 5 / 100;
    }

    public double getCashback(TechCommerciant commerciant, double amount) {
        return amount * 2 / 100;
    }

}
