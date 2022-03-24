package com.kea;

public class Rateinfo {
    private String code;
    private String desc;
    private double rate;

    /**
     * @param Code Kode der anvendes for denne valuta
     * @param Desc Beskrivelse til valuta
     * @param Rate Antal danske kr for 100 af den valuta vi omregner til
     */
    public Rateinfo(String Code, String Desc, double Rate) {
        code=Code;
        desc=Desc;
        rate=Rate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return code + " " + desc + " " + rate;
    }
}
