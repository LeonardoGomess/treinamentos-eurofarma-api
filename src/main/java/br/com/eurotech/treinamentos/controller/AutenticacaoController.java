package br.com.eurotech.treinamentos.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.eurotech.treinamentos.dto.usuario.DadosAutenticacao;
import br.com.eurotech.treinamentos.dto.usuario.DadosAutenticacaoMobile;
import br.com.eurotech.treinamentos.dto.usuario.DadosDetalhamentoUsuario;
import br.com.eurotech.treinamentos.dto.usuario.DadosTokenJWT;
import br.com.eurotech.treinamentos.model.TipoUsuario;
import br.com.eurotech.treinamentos.model.Usuario;
import br.com.eurotech.treinamentos.repository.UsuarioRepository;
import br.com.eurotech.treinamentos.services.CloudflareTurnstileService;
import br.com.eurotech.treinamentos.services.TokenService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;


    @Autowired 
    private UsuarioRepository repository;

    @Autowired
    private CloudflareTurnstileService service;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {

        if(dados.tokenCloudFlare() == null || !service.validaCaptchaAsync(dados.tokenCloudFlare()).join()){
            return ResponseEntity.badRequest().build();
        }
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());


        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());
        

        Long idUsuario = repository.findByLogin(dados.login());


        return ResponseEntity.ok(new DadosTokenJWT(idUsuario,tokenJWT));


    }

    @PostMapping("/mobile")
    public ResponseEntity efetuarLoginMobile(@RequestBody @Valid DadosAutenticacaoMobile dados) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());


        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((Usuario) authentication.getPrincipal());
        

        Long idUsuario = repository.findByLogin(dados.login());


        return ResponseEntity.ok(new DadosTokenJWT(idUsuario,tokenJWT));

    }

    @GetMapping
    public ResponseEntity<DadosDetalhamentoUsuario> validarUsuarioLogado(@RequestParam("token") String token,@RequestParam("idUsuario") Long idUsuario){
        Boolean isTokenValido = tokenService.validateToken(token).equals("") ? false : true;
        DadosDetalhamentoUsuario usuario = new DadosDetalhamentoUsuario(repository.getReferenceById(idUsuario));
        Boolean isAnalista = usuario.tipo().equals(TipoUsuario.ANALISTA);  
        if(isTokenValido && isAnalista){
            return ResponseEntity.ok(usuario);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
