package br.com.eurotech.treinamentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.eurotech.treinamentos.dto.usuario.DadosAlunoPresenca;
import br.com.eurotech.treinamentos.model.Setor;
import br.com.eurotech.treinamentos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

    UserDetails findUserDetailsByRe(String subject);

    @Query(value = "select id from tb_usuario where re = :login;",nativeQuery = true)
    Long findByLogin(@Param("login") String login);

    @Query("SELECT u FROM Usuario u WHERE u.id IN " +
       "(SELECT aa.aluno.id FROM AlunoAula aa WHERE aa.aula.id IN " +
       "(SELECT a.id FROM Aula a WHERE a.treinamento.id = :id_treinamento AND a.ativo = true)) " +
       "AND u.ativo = true")
        List<Usuario> findByTreinamento(@Param("id_treinamento") Long idTreinamento);


    List<Usuario> findBySetor(Setor setor);

    Usuario findByRe(String re);

    @Query(value = "select * from tb_usuario where nome like concat('%', :query, '%') or re like concat('%', :query, '%') or setor like concat('%', :query, '%')", nativeQuery = true)
    List<Usuario> findByQuery(@Param("query") String query);

    @Query(value = "select * from tb_usuario where nome like concat('%', :query, '%') or re like concat('%', :query, '%')",nativeQuery = true)
    List<Usuario> findByNomeOrRe(@Param("query") String query);

    @Query("SELECT new br.com.eurotech.treinamentos.dto.usuario.DadosAlunoPresenca(u.cpf, u.nome, u.re, u.setor, aa.aula_concluida,aa.assinatura) " +
    "FROM Usuario u JOIN AlunoAula aa ON u.id = aa.aluno.id " +
    "JOIN aa.aula a " +
    "WHERE a.treinamento.id = :id_treinamento")
    List<DadosAlunoPresenca> findDadosAlunoPresencasByTreinamento(@Param("id_treinamento") Long id_treinamento);



}
    
    
