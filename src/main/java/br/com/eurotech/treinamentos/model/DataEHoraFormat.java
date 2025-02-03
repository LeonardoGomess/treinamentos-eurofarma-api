package br.com.eurotech.treinamentos.model;

import java.time.LocalDateTime;

public class DataEHoraFormat {
    public static String returndataFormatada(LocalDateTime data){
        String dia = data.getDayOfMonth() < 10 ? "0" + data.getDayOfMonth()  : data.getDayOfMonth()  +"";
        String mes = data.getMonthValue() < 10 ? "0" + data.getMonthValue() : data.getMonthValue() +"";
        String ano = data.getYear() + "";  
        return dia + "/" + mes + "/" + ano;
    }

}
