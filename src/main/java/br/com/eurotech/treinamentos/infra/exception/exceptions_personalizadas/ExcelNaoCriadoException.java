package br.com.eurotech.treinamentos.infra.exception.exceptions_personalizadas;

public class ExcelNaoCriadoException extends RuntimeException {
    public ExcelNaoCriadoException(String message){
        super(message);
    }    
}
