package br.com.eurotech.treinamentos.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.eurotech.treinamentos.dto.aula.DadosDetalhamentoAula;
import br.com.eurotech.treinamentos.dto.treinamento.DadosAlteracaoTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosCadastroTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosDetalhamentoTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosHistoricoTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosListagemTreinamento;
import br.com.eurotech.treinamentos.model.Treinamento;
import br.com.eurotech.treinamentos.repository.TreinamentoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/treinamento")
public class TreinamentoController {
    
    @Autowired
    private TreinamentoRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosListagemTreinamento>> listarTreinamentos(Pageable paginacao){
        var page = repository.findAll(paginacao).map(DadosListagemTreinamento::new);
        return ResponseEntity.ok(page);
    }  

    @GetMapping("findByAluno/{id_aluno}")
    public ResponseEntity<List<DadosHistoricoTreinamento>> findTreinamentosByAluno(@PathVariable("id_aluno") Long id_aluno){
        List<DadosHistoricoTreinamento> treinamentos = repository.findTreinamentosByAluno(id_aluno);
        return ResponseEntity.ok(treinamentos);
    }

    @GetMapping("{idAluno}/hoje")
    public ResponseEntity<List<DadosHistoricoTreinamento>> findTreinamentosDeHoje(@PathVariable("idAluno")Long idAluno,@RequestParam("diaEHoraAparelhoFuncionario") String diaEHoraAparelhoFuncionario){
        List<DadosHistoricoTreinamento> treinamentosHoje = returnTreinamentosDeHoje(idAluno,diaEHoraAparelhoFuncionario);
        return ResponseEntity.ok(treinamentosHoje);
    }

    public List<DadosHistoricoTreinamento> returnTreinamentosDeHoje(Long idAluno,String diaEHoraAparelhoFuncionarioString){
        List<DadosHistoricoTreinamento> treinamentos = repository.findTreinamentosByAluno(idAluno);
        List<DadosHistoricoTreinamento> treinamentosHoje = new ArrayList<>();
        LocalDateTime diaEHoraAparelhoFuncionario = LocalDateTime.parse(diaEHoraAparelhoFuncionarioString);
         for (DadosHistoricoTreinamento treinamento : treinamentos) {
             if(treinamento.data_inicio().toLocalDate().isEqual(diaEHoraAparelhoFuncionario.toLocalDate())){
                treinamentosHoje.add(treinamento);
             }
         }
         return treinamentosHoje;
    }

    @GetMapping("/search")
    public ResponseEntity<List<DadosDetalhamentoTreinamento>> findTreinamentosByNome(@RequestParam("nome") String nome){
        List<DadosDetalhamentoTreinamento> treinamentosEncontrados = repository.findByNome(nome).stream().map(DadosDetalhamentoTreinamento::new).toList();
        return ResponseEntity.ok(treinamentosEncontrados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTreinamento> findById(@PathVariable("id") Long id){
        Treinamento treinamento = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoTreinamento(treinamento));
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosCadastroTreinamento dto, UriComponentsBuilder uriBuilder){
        Treinamento treinamento = new Treinamento(dto);
        repository.save(treinamento);
        var uri = uriBuilder.path("/treinamento/{id}").buildAndExpand(treinamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTreinamento(treinamento));
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(@PathVariable("id") Long id,@RequestBody @Valid DadosAlteracaoTreinamento dados){
        Treinamento treinamento = repository.getReferenceById(id);
        treinamento.setTreinamento(dados);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        Treinamento treinamento = repository.getReferenceById(id);
        treinamento.excluir();
        return ResponseEntity.noContent().build();
    }
}
