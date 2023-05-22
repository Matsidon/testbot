package com.bot.matsTestBot;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.bot.matsTestBot.service.CurrencyConversionServiceImpl.getJsonFromUrl;

@SpringBootApplication
public class MatsTestBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatsTestBotApplication.class, args);
	}
}
