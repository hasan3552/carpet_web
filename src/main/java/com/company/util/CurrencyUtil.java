package com.company.util;

import com.company.dto.CurrencyDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.bytebuddy.description.method.MethodDescription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class CurrencyUtil {

    public static Double getRate(){
        try {

            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/usd/");
            URLConnection connection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Type type = new TypeToken<List<CurrencyDTO>>() {}.getType();

            List<CurrencyDTO> currencyList = gson.fromJson(reader, type);

            return Double.parseDouble(currencyList.get(0).getRate());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Double calcPrice(Double height, Double weight, Double priceUSD) {
        double price = priceUSD * CurrencyUtil.getRate() * height * weight;
        return (double) Math.round(price / 10000000) * 1000;
    }
}
