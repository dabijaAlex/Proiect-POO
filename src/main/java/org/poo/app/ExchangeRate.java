package org.poo.app;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ExchangeRate {
    private String from;
    private String to;
    private double rate;

    public ExchangeRate(String from, String to, double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }
}
