package br.com.eurotech.treinamentos.dto.usuario;

public record DadosAutenticacao(
    String login,   
    String senha,
    String tokenCloudFlare
    ) {
    
}
