package br.com.eurotech.treinamentos.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import br.com.eurotech.treinamentos.dto.aluno_aula.DadosAlteracaoAlunoAula;
import br.com.eurotech.treinamentos.dto.aluno_aula.DadosCadastroAlunoAula;
import br.com.eurotech.treinamentos.dto.apostila.DadosAlteracaoApostila;
import br.com.eurotech.treinamentos.dto.aula.DadosAlteracaoAula;
import br.com.eurotech.treinamentos.dto.aula.DadosCadastroAula;
import br.com.eurotech.treinamentos.dto.aula.DadosDetalhamentoAula;
import br.com.eurotech.treinamentos.dto.aula.DadosIdAula;
import br.com.eurotech.treinamentos.dto.presenca.DadosPresenca;
import br.com.eurotech.treinamentos.dto.usuario.DadosCadastroUsuario;
import br.com.eurotech.treinamentos.dto.usuario.DadosIdUsuario;
import br.com.eurotech.treinamentos.model.AlunoAula;
import br.com.eurotech.treinamentos.model.Aula;
import br.com.eurotech.treinamentos.model.Treinamento;
import br.com.eurotech.treinamentos.model.Usuario;
import br.com.eurotech.treinamentos.repository.AlunoAulaRepository;
import br.com.eurotech.treinamentos.repository.AulaRepository;
import br.com.eurotech.treinamentos.repository.TreinamentoRepository;
import br.com.eurotech.treinamentos.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/aula")
public class AulaController {
    

    @Autowired
    private AulaRepository repository;

    @Autowired
    private AlunoAulaRepository alunoAulaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private TreinamentoRepository treinamentoRepository;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @GetMapping("/treinamento/{id}")
    public ResponseEntity<List<DadosDetalhamentoAula>> listarAulas(@PathVariable("id") Long id_treinamento){
        List<Aula> aulas = repository.findByTreinamentoIdAndAtivoTrue(id_treinamento);
        return ResponseEntity.ok(aulas.stream().map(DadosDetalhamentoAula::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAula> mostrarAula(@PathVariable("id") Long id){
        Aula aula = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoAula(aula));
    }


    @PostMapping
    @Transactional
    public List<ResponseEntity> insert(@RequestBody @Valid DadosCadastroAlunoAula dados,UriComponentsBuilder uriBuilder){
        List uris = new ArrayList<ResponseEntity>();
        for (DadosCadastroAula aula_dto : dados.aulas()) {
            Aula aula = new Aula(aula_dto);
            System.out.println(aula);
            repository.save(aula);
            //var uri = uriBuilder.path("/aula/{id}").buildAndExpand(aula.getId()).toUri();
            uris.add(new DadosDetalhamentoAula(aula));
            for(DadosIdUsuario usuario_dto : dados.alunos()){
                alunoAulaRepository.save(new AlunoAula(usuarioRepository.getReferenceById(usuario_dto.id()),aula));
            }
        }
        return uris;
    }


    @PutMapping("/users/edit")
    @Transactional
    public ResponseEntity alterarAlunoAula(@RequestBody @Valid DadosAlteracaoAlunoAula dados,UriComponentsBuilder uriBuilder){
        List<Usuario> usuarios_banco = alunoAulaRepository.findByIdAluno();
        Aula aula = repository.findByTreinamentoIdAndAtivoTrue(dados.id_treinamento()).get(0);
        for (Long dadosIdUsuario : dados.alunos_deletados()) {
            alunoAulaRepository.deleteByUsuarioAndAulaId(dadosIdUsuario,aula.getId());
        }

        for(Long dadosIdUsuario : dados.alunos_adicionados()){
            alunoAulaRepository.save(new AlunoAula(usuarioRepository.getReferenceById(dadosIdUsuario),aula));
        }

        return ResponseEntity.noContent().build();
    }



@PutMapping("/registrarPresenca")
@Transactional
public ResponseEntity registrarPresenca(@RequestBody DadosPresenca dadosPresenca) {
    Bucket bucket = StorageClient.getInstance().bucket();
    Aula aula = repository.findByTreinamentoIdAndAtivoTrue(dadosPresenca.idTreinamento()).get(0);
    AlunoAula alunoAula = alunoAulaRepository.findAlunoAulaByIdAlunoAndIdAula(dadosPresenca.idAluno(), aula.getId());
    Usuario usuario = usuarioRepository.getReferenceById(dadosPresenca.idAluno());
    String name = usuario.getRe() + "treinamento" + dadosPresenca.idTreinamento();
    Treinamento treinamento = treinamentoRepository.getReferenceById(dadosPresenca.idTreinamento());
    String downloadUrl = "";
    
    if(!verificaDiaEHorarioValidacao(treinamento, dadosPresenca)){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Essa validação está sendo feita fora do período permitido,portanto sua presença não pode ser registrada!");
    }

    try {
        byte[] assinaturaBytes = Base64.getDecoder().decode(dadosPresenca.assinaturaFile());
        bucket.create(name, assinaturaBytes, "image/png"); // Ajuste o tipo MIME conforme necessário
        downloadUrl = getImageUrl(imageBaseUrl, name);
    } catch (IllegalArgumentException | IOException | InterruptedException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar a assinatura.");
    }

    alunoAula.setAssinatura(downloadUrl);
    alunoAula.setAula_concluida(true);

    return ResponseEntity.noContent().build();
}

    public Boolean verificaDiaEHorarioValidacao(Treinamento treinamento, DadosPresenca dadosPresenca){
        ZoneOffset offsetTreinamento = ZoneOffset.of(dadosPresenca.timezone());
        ZoneOffset offsetAluno = ZoneOffset.of(treinamento.getTimezone());
        OffsetDateTime inicioTreinamentoUTC = treinamento.getDataInicio().atOffset(offsetTreinamento).toInstant().atOffset(ZoneOffset.UTC);
        OffsetDateTime fimTreinamentoUTC = treinamento.getDataFim().atOffset(offsetTreinamento).toInstant().atOffset(ZoneOffset.UTC);
        OffsetDateTime aparelhoAlunoUTC = dadosPresenca.dataEHoraAparelhoAluno().atOffset(offsetAluno).toInstant().atOffset(ZoneOffset.UTC);
        System.out.println("Início Treinamento: " + inicioTreinamentoUTC);
        System.out.println("Fim Treinamento: " + fimTreinamentoUTC);
        System.out.println("Aparelho: "+aparelhoAlunoUTC);
        Boolean isTreinamentoNoDiaEHoraCorretos = !aparelhoAlunoUTC.isBefore(inicioTreinamentoUTC) && !aparelhoAlunoUTC.isAfter(fimTreinamentoUTC.plusHours(2));
        return isTreinamentoNoDiaEHoraCorretos;
    }
      
    public String getImageUrl(String baseUrl,String name) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl+name))
            .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String jsonResponse = response.body();
    JsonNode node = new ObjectMapper().readTree(jsonResponse);
    String imageToken = node.get("downloadTokens").asText();
    System.out.println(node);
    return baseUrl + name + "?alt=media&token=" + imageToken;
  }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity alterarAula(@PathVariable("id") Long id,@RequestBody @Valid DadosAlteracaoAula dados){
        Aula aula = repository.getReferenceById(id);
        aula.setAula(dados);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirAula(@PathVariable("id") Long id){
        Aula aula = repository.getReferenceById(id);
        aula.setAtivo(false);
        return ResponseEntity.noContent().build();
    }
   

}
