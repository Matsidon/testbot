package com.bot.matsTestBot.service;

import com.bot.matsTestBot.model.Currency;
import com.bot.matsTestBot.dto.CurrencyInputDto;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    @Override
    @SneakyThrows
    public double convert(Currency original, Currency target, double count) {
        String urlFrom = "https://www.nbrb.by/api/exrates/rates/" + original.name().toLowerCase() + "?parammode=2";
        String urlTo = "https://www.nbrb.by/api/exrates/rates/" + target.name().toLowerCase() + "?parammode=2";
        String jsonFrom = getJsonFromUrl(urlFrom);
        String jsonTo = getJsonFromUrl(urlTo);
        Gson gson = new Gson();
        CurrencyInputDto currencyInputFromDto = gson.fromJson(jsonFrom, CurrencyInputDto.class);
        CurrencyInputDto currencyInputToDto = gson.fromJson(jsonTo, CurrencyInputDto.class);
        return count * (currencyInputFromDto.getCur_OfficialRate() / currencyInputFromDto.getCur_Scale()) / (currencyInputToDto.getCur_OfficialRate() / currencyInputToDto.getCur_Scale());
    }

    public static String getJsonFromUrl(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
