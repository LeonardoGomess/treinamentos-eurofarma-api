package br.com.eurotech.treinamentos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.eurotech.treinamentos.dto.apostila.DadosAlteracaoApostila;
import br.com.eurotech.treinamentos.dto.apostila.DadosCadastroApostila;
import br.com.eurotech.treinamentos.dto.apostila.DadosDetalhamentoApostila;
import br.com.eurotech.treinamentos.dto.treinamento.DadosAlteracaoTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosDetalhamentoTreinamento;
import br.com.eurotech.treinamentos.model.Apostila;
import br.com.eurotech.treinamentos.model.Treinamento;
import br.com.eurotech.treinamentos.repository.ApostilaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/apostila")
public class ApostilaController {
    
    @Autowired
    private ApostilaRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosCadastroApostila dados,UriComponentsBuilder uriBuilder){
        Apostila apostila = new Apostila(dados);
        repository.save(apostila);
        var uri = uriBuilder.path("/apostila/{id}").buildAndExpand(apostila.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoApostila(apostila));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoApostila> findApostilaById(@PathVariable("id") Long id){
        Apostila apostila = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoApostila(apostila));
    }

    @GetMapping("/treinamento/{id}")
    public ResponseEntity findApostilasByTreinamento(@PathVariable("id") Long id_treinamento){
        List<Apostila> apostilas = repository.findByTreinamentoId(id_treinamento);
        return ResponseEntity.ok(apostilas.stream().map(DadosDetalhamentoApostila::new).toList());
    }
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoApostila> alterarApostila(@PathVariable("id") Long id, @RequestBody @Valid DadosAlteracaoApostila dados){
        Apostila apostila = repository.getReferenceById(id);
        apostila.setApostila(dados);
        System.out.println(dados);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoApostila> excluirApostila(@PathVariable("id") Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    
} 
