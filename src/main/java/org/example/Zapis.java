package org.example;

import java.util.ArrayList;

public class Zapis {
    private String common;
    private String botanical;
    private int zone;
    private String light;
    private double price;
    private int availability;
    private String uuid;

    public  Zapis(ArrayList<String> dlyaZapisey){
        try {common = dlyaZapisey.get(0);
            botanical = dlyaZapisey.get(1);
            //случай с записью "годичный"
            if(dlyaZapisey.get(2).equals("Годичный")){
                zone = 12;
            }
            else{
                //стандартный случай с одним числом
                try {
                    zone = Integer.parseInt(dlyaZapisey.get(2));
                }
                //случай с промежутком из 2х чисел, в таком случае вычисляется среднее из двух чисел
                catch (Exception e){
                    double a=0;
                    double b=0;
                    try{a = Double.parseDouble(dlyaZapisey.get(2).substring(0, 1));} catch (Exception t){System.out.println("не удалось взять первое число");}
                    try{b = Double.parseDouble(dlyaZapisey.get(2).substring(4, 5));} catch (Exception t){System.out.println("не удалось взять второе число");}
                    zone = (int) Math.round((a+b)/2);
                }
            }

            light = dlyaZapisey.get(3);
            price = Double.parseDouble(dlyaZapisey.get(4).replace("$", "").replace(",", ""));
            availability = Integer.parseInt(dlyaZapisey.get(5));
            uuid = dlyaZapisey.get(6);
        }
        catch (Exception e){System.out.println("что-то пошло не по плану");}


    }


    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public String getBotanical() {
        return botanical;
    }

    public void setBotanical(String botanical) {
        this.botanical = botanical;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String catalog_id) {
        this.uuid = catalog_id;
    }
}