package br.com.eurotech.treinamentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import br.com.eurotech.treinamentos.dto.treinamento.DadosHistoricoTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosListagemTreinamento;
import br.com.eurotech.treinamentos.model.Treinamento;

public interface TreinamentoRepository extends JpaRepository<Treinamento,Long>{

    @Query("SELECT new br.com.eurotech.treinamentos.dto.treinamento.DadosHistoricoTreinamento(" +
           "aa.aluno.id, aa.aula_concluida,t.nome,t.dataInicio,t.dataFim,t.timezone,a.sala,t.nomeProfessor,a.treinamento.id) " +
           "FROM AlunoAula aa " +
           "JOIN aa.aula a " +
           "JOIN a.treinamento t " +
           "WHERE aa.aluno.id = :id_aluno")
    List<DadosHistoricoTreinamento> findTreinamentosByAluno(@Param("id_aluno") Long id_aluno);



    @Query(value = "select * from tb_treinamento where nome like concat('%', :nome, '%')", nativeQuery = true)
    List<Treinamento> findByNome(String nome);
}
