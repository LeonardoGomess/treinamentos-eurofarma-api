package br.com.eurotech.treinamentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.eurotech.treinamentos.model.Aula;


public interface AulaRepository extends JpaRepository<Aula,Long>{

    
    @Query("SELECT a FROM Aula a INNER JOIN a.treinamento t WHERE t.id = :idTreinamento AND a.ativo = true")
    List<Aula> findByTreinamentoIdAndAtivoTrue(Long idTreinamento);
    
}
