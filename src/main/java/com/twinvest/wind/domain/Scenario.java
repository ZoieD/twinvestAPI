
package com.twinvest.wind.domain;

import java.util.List;

public class Scenario {
    private final int nTurbines;
    private final double pRatedKw;
    private final double dtHours;
    private final List<Double> windSpeed;      // m/s
    private final List<Double> priceEurMwh;    // €/MWh

    public Scenario(int nTurbines, double pRatedKw, double dtHours,
                    List<Double> windSpeed, List<Double> priceEurMwh) {
        this.nTurbines = nTurbines; this.pRatedKw = pRatedKw; this.dtHours = dtHours;
        this.windSpeed = windSpeed; this.priceEurMwh = priceEurMwh;
    }
    public int getnTurbines(){ return nTurbines; }
    public double getpRatedKw(){ return pRatedKw; }
    public double getDtHours(){ return dtHours; }
    public List<Double> getWindSpeed(){ return windSpeed; }
    public List<Double> getPriceEurMwh(){ return priceEurMwh; }
}
