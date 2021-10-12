package ru.geek.financial_assistant.models;


import lombok.Data;


@Data
public class IndicatorDTO {

    private Long id;
    private IndexDTO indexDTO;
    private Float rsi;
    private Float macd;
    private Float macdg;
    private Float garch;

    public IndicatorDTO(Indicator indicator) {
        this.id = indicator.getId();
        this.indexDTO = new IndexDTO(indicator.getIndex());
        this.rsi = indicator.getRsi();
        this.macd = indicator.getMacd();
        this.macdg = indicator.getMacdg();
        this.garch = indicator.getGarch();
    }

    public IndicatorDTO() {
    }

    public Indicator convertToIndicator (){
    Indicator indicator = new Indicator(indexDTO.convertToIndex(),rsi,macd,macdg,garch);
    return indicator;
    }

}
