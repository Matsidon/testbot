package com.bot.matsTestBot.dto;

import lombok.Data;

@Data
public class CurrencyInputDto {
    private long Cur_ID;
    private String Date;
    private String Cur_Abbreviation;
    private long Cur_Scale;
    private String Cur_Name;
    private double Cur_OfficialRate;
}
