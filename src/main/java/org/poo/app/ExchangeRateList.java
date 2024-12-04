package org.poo.app;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;

@Getter @Setter
public class ExchangeRateList{
    public static ArrayList<ExchangeRate> rates;
    public static String dominant;
    public ExchangeRateList(ExchangeInput[] input) {
        rates = new ArrayList<ExchangeRate>();

        for (ExchangeInput exchangeInput : input) {
            rates.add(new ExchangeRate(exchangeInput.getFrom(), exchangeInput.getTo(), exchangeInput.getRate()));
            rates.add(new ExchangeRate(exchangeInput.getTo(), exchangeInput.getFrom(), 1 / exchangeInput.getRate()));
        }
        getDominant();
    }

    private void getDominant() {
        int nrEur = 0;
        int nrUsd = 0;
        for(ExchangeRate r : rates) {
            if(r.getFrom().equals("EUR"))
                nrEur++;
            if(r.getFrom().equals("USD"))
                nrUsd++;
        }
        dominant = nrEur > nrUsd ? "EUR" : "USD";
    }


    public static double convertRate(String From, String To) {
        for(ExchangeRate r : rates) {
            if(r.getFrom().equals(From) && r.getTo().equals(To)) {
                return r.getRate();
            }
        }


        double FromToEuroRate = convertRate(From, dominant);
        double FromEuroRateToTo = convertRate(dominant, To);


        return FromEuroRateToTo * FromToEuroRate;
    }
}
