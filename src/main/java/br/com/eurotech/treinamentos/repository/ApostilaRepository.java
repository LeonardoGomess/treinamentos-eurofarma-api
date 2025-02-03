package br.com.eurotech.treinamentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eurotech.treinamentos.model.Apostila;

public interface ApostilaRepository extends JpaRepository<Apostila,Long>{

    List<Apostila> findByTreinamentoId(Long treinamentoId);

    
}