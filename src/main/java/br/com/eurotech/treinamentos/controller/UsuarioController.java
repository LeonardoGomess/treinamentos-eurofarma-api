package br.com.eurotech.treinamentos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.eurotech.treinamentos.dto.apostila.DadosDetalhamentoApostila;
import br.com.eurotech.treinamentos.dto.aula.DadosDetalhamentoAula;
import br.com.eurotech.treinamentos.dto.treinamento.DadosHistoricoTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosListagemTreinamento;
import br.com.eurotech.treinamentos.dto.usuario.DadosAlteracaoUsuario;
import br.com.eurotech.treinamentos.dto.usuario.DadosAlunoPresenca;
import br.com.eurotech.treinamentos.dto.usuario.DadosCadastroUsuario;
import br.com.eurotech.treinamentos.dto.usuario.DadosDetalhamentoUsuario;
import br.com.eurotech.treinamentos.model.Apostila;
import br.com.eurotech.treinamentos.model.Aula;
import br.com.eurotech.treinamentos.model.Setor;
import br.com.eurotech.treinamentos.model.Usuario;
import br.com.eurotech.treinamentos.repository.AulaRepository;
import br.com.eurotech.treinamentos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController{
    
    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoUsuario>> listarUsuarios(Pageable paginacao){
        var page = repository.findAll(paginacao).map(DadosDetalhamentoUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/findAlunosPresencaByTreinamento/{id_treinamento}")
    public ResponseEntity<List<DadosAlunoPresenca>> findDadosAlunoPresencasByTreinamento(@PathVariable("id_treinamento") Long id_treinamento){
        List<DadosAlunoPresenca> alunosPresencas = repository.findDadosAlunoPresencasByTreinamento(id_treinamento);
        return ResponseEntity.ok(alunosPresencas);
    }
    
    @GetMapping("/research")
    public ResponseEntity<List<DadosDetalhamentoUsuario>> listarByQuery(@RequestParam("query") String query){
        List<DadosDetalhamentoUsuario> usuarios = repository.findByQuery(query).stream().map(DadosDetalhamentoUsuario::new).toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/research/re_nome")
    public ResponseEntity<List<DadosDetalhamentoUsuario>> listarByQueryNomeOrRe(@RequestParam("query") String query){
        List<DadosDetalhamentoUsuario> usuarios = repository.findByNomeOrRe(query).stream().map(DadosDetalhamentoUsuario::new).toList();
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/setor/{setor}")
    public ResponseEntity<List<DadosDetalhamentoUsuario>> listarUsuariosBySetor(@PathVariable("setor") Setor setor){
        List<DadosDetalhamentoUsuario> usuarios = repository.findBySetor(setor).stream().map(DadosDetalhamentoUsuario::new).toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/setor")
    public ResponseEntity<Setor[]> listarSetores(){
        Setor[] usuarios = Setor.values();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/re")
    public ResponseEntity<Usuario> listarUsuariosByRe(@RequestParam("re") String re){
        Usuario usuario = repository.findByRe(re);
        return ResponseEntity.ok(usuario);
    }


    @GetMapping("/treinamento/{id}")
    public ResponseEntity findAlunoByTreinamento(@PathVariable("id") Long id_treinamento){
        List<Usuario> usuarios= repository.findByTreinamento(id_treinamento);
        return ResponseEntity.ok(usuarios.stream().map(DadosDetalhamentoUsuario::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity exibirUsuario(@PathVariable("id") Long id){
        Usuario usuario = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @PostMapping
    @Transactional
    public ResponseEntity insert(@RequestBody @Valid DadosCadastroUsuario dados,UriComponentsBuilder uriBuilder){
        Usuario usuario = new Usuario(dados);
        String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
        usuario.setSenha(encryptedPassword);
        //System.out.println(usuario);
        repository.save(usuario);
        var uri =uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity alterarUsuario(@PathVariable("id") Long id,@RequestBody @Valid DadosAlteracaoUsuario dados){
        Usuario usuario = repository.getReferenceById(id);
        String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
        usuario.setUsuario(dados);
        usuario.setSenha(encryptedPassword);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirUsuario(@PathVariable("id") Long id){
        Usuario usuario = repository.getReferenceById(id);
        usuario.setAtivo(false);
        return ResponseEntity.noContent().build();
    }


}
