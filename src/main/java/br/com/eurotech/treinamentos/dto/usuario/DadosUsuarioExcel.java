package br.com.eurotech.treinamentos.dto.usuario;

import br.com.eurotech.treinamentos.model.Setor;
import br.com.eurotech.treinamentos.model.TipoUsuario;
import br.com.eurotech.treinamentos.model.Usuario;

public record DadosUsuarioExcel(
    Long id,
    String cpf,
    String nome,
    Boolean ativo,
    TipoUsuario tipo,
    Setor setor,
    String re
) {

}
