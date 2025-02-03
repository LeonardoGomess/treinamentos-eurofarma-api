package br.com.eurotech.treinamentos.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.eurotech.treinamentos.dto.usuario.DadosAlteracaoUsuario;
import br.com.eurotech.treinamentos.dto.usuario.DadosCadastroUsuario;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tb_usuario")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String cpf;
    
    private String nome;
    
    private String senha;
    
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    private Setor setor;

    private String re;
    
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @OneToMany(mappedBy = "aluno")
    private List<AlunoAula> alunoAula;

    public Usuario(Long id){
        this.id = id;
    }
    
    public Usuario(DadosCadastroUsuario dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.senha = dados.senha();
        this.tipo = dados.tipo(); 
        this.setor = dados.setor();  
        this.re = dados.re(); 
    }


    public void setUsuario(DadosAlteracaoUsuario dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.senha = dados.senha();
        this.tipo = dados.tipo();    
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.tipo == TipoUsuario.ANALISTA) 
            return List.of(new SimpleGrantedAuthority("ROLE_ANALISTA"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_ALUNO"));    
    }


    @Override
    public String getPassword() {
        return senha;
    }


    @Override
    public String getUsername() {
       return cpf;
    }

    

}
