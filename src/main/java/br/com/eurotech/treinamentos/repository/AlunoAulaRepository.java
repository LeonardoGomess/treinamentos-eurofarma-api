package br.com.eurotech.treinamentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.eurotech.treinamentos.model.AlunoAula;
import br.com.eurotech.treinamentos.model.Usuario;

public interface AlunoAulaRepository extends JpaRepository<AlunoAula,Long>{

    List<AlunoAula> findByAulaId(Long id);

    @Query(value = "SELECT id_aluno FROM tb_aluno_aula group by id_aluno", nativeQuery = true)
    List<Usuario> findByIdAluno();

    @Modifying
    @Query(value = "DELETE from tb_aluno_aula WHERE id_aluno = :id_aluno and id_aula= :id_aula", nativeQuery = true)
    void deleteByUsuarioAndAulaId(@Param("id_aluno") Long id_aluno, @Param("id_aula") Long id_aula);

    @Query(value = "select * from tb_aluno_aula where id_aluno = :id_aluno and id_aula = :id_aula", nativeQuery = true)
    AlunoAula findAlunoAulaByIdAlunoAndIdAula(@Param("id_aluno") Long id_aluno,@Param("id_aula") Long id_aula);

}
