package com.kea;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Rateinfo> rateinfos = new ArrayList<>();
    static ArrayList<String> codes = new ArrayList<>();

    public static void main(String[] args){
        boolean go = true;
        Scanner input = new Scanner(System.in);
        readCurrenciesFromWeb();
        printWelcomeAndCurrencies();

        while (go) {
            convertCurrencies();
            System.out.print("Type 'exit' to exit, or any other input if you wish to continue: ");
            String choice = input.next();
            if (choice.equals("exit")) go = false;
        }
        System.out.println("Have a nice day!");
    }

    public static void readCurrenciesFromWeb() {
        rateinfos.add(new Rateinfo("DKK", "Danske kroner", 100));

        try {
            //Instantiating the URL class
            URL url = new URL("https://www.nationalbanken.dk/_vti_bin/DN/DataService.svc/CurrencyRatesXML?lang=da");
            //Retrieving the contents of the specified page
            Scanner sc = new Scanner(url.openStream());
            //Instantiating the StringBuffer class to hold the result
//        StringBuffer sb = new StringBuffer();
            int count = 0;
            String code = "";
            String desc = "";
            String rate = "";

            while (sc.hasNext()) {
                String token = sc.next();
//            sb.append(t);
                count++;

                if (count > 12 && count < 181) {
                    if (token.contains("code")) {
                        code = token.replaceAll("code=", "").replaceAll("\"", "");
                        // System.out.print(code);
                        codes.add(code);
                    } else if (token.contains("desc")) {
                        token = token.replaceAll("desc=", "").replaceAll("\"", "").replaceAll("�", "æ");

                        if (token.contains("Euro")) {
                            desc = token;

                        } else {
                            token = token + " " + sc.next();
                            token = token.replaceAll("\"", "");
                            desc = token;

                        }
                    } else if (token.contains("rate")) {
                        token = token.replaceAll("rate=", "").replaceAll("\"", "");
                        rate = token;

                        if (rate.equals("-")) {
                            rate = "1509.53";
                        }
                        double rateDouble = Double.parseDouble(rate.replace(",", "."));

                        if (!codes.isEmpty()) {
                            boolean containsSameCode = false;
                            for (int i = 0; i < codes.size(); i++) {
                                String loopCode1 = codes.get(i);
                                for (int j = 0; j < rateinfos.size(); j++) {
                                    String loopCode2 = rateinfos.get(j).getCode();
                                    if (loopCode1.equals(loopCode2)) containsSameCode = true;
                                }
                            }

                            if (!containsSameCode) {
                                Rateinfo rateinfo = new Rateinfo(code, desc, rateDouble);
                                rateinfos.add(rateinfo);
                                codes.remove(rateinfo.getCode());
                            }
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printWelcomeAndCurrencies() {
        System.out.println("Welcome to the currency converter");
        for (Rateinfo rateinfo : rateinfos) {
            System.out.println(rateinfo);
        }
    }

    public static void convertCurrencies() {
        Scanner input = new Scanner(System.in);
        boolean mainLoop = true;
        boolean loop1 = true;
        boolean loop2 = true;
        boolean loop3 = true;
        String firstCurr = "";
        String secondCurr = "";
        String amountString = "";
        double amountDouble = 0;



            while (loop1) {
                System.out.print("Type in the currency with the three letter code you wish to convert from (for example USD): ");
                firstCurr = input.next().toUpperCase();
                for (Rateinfo rateinfo : rateinfos) {
                    if (rateinfo.getCode().equals(firstCurr)) {
                        loop1 = false;
                    }
                }
                if (loop1) {
                    System.out.println("Invalid currency code");
                }
            }


            while (loop2) {
                System.out.print("Type in the amount you wish to convert: ");
                amountString = input.next();

                try {
                    amountDouble = Double.parseDouble(amountString);
                    loop2 = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                }

            }


            while (loop3) {
                System.out.print("Type in the currency you wish to convert to: ");
                secondCurr = input.next().toUpperCase();
                for (Rateinfo rateinfo : rateinfos) {
                    if (rateinfo.getCode().equals(secondCurr)) {
                        loop3 = false;
                    }
                }
                if (loop3) {
                    System.out.println("Invalid currency code");
                }
            }

            double firstAmount = 0;
            double secondAmount = 0;

            for (Rateinfo rateinfo : rateinfos) {
                if (rateinfo.getCode().equals(firstCurr)) firstAmount = rateinfo.getRate();
            }

            for (Rateinfo rateinfo : rateinfos) {
                if (rateinfo.getCode().equals(secondCurr)) secondAmount = rateinfo.getRate();
            }

            double convertedAmount = firstAmount / secondAmount * amountDouble;

            System.out.println("Converted amount: " + convertedAmount + " " + secondCurr);




    }
}
