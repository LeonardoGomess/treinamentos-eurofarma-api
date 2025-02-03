package br.com.eurotech.treinamentos.dto.usuario;

import br.com.eurotech.treinamentos.model.Setor;
import br.com.eurotech.treinamentos.model.TipoUsuario;
import br.com.eurotech.treinamentos.model.Usuario;

public record DadosDetalhamentoUsuario(
    Long id,
    String cpf,
    String nome,
    Boolean ativo,
    TipoUsuario tipo,
    Setor setor,
    String re
) {
    public DadosDetalhamentoUsuario(Usuario usuario){
        this(usuario.getId(),usuario.getCpf(), usuario.getNome(), usuario.getAtivo(), usuario.getTipo(),usuario.getSetor(),usuario.getRe());
    }
}
