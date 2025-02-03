package br.com.eurotech.treinamentos.model;

import java.time.LocalDateTime;
import java.util.Set;

import br.com.eurotech.treinamentos.dto.treinamento.DadosAlteracaoTreinamento;
import br.com.eurotech.treinamentos.dto.treinamento.DadosCadastroTreinamento;
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
@Table(name="tb_treinamento")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Treinamento {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private Formato formato;
    
    private Boolean ativo = true;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private String nomeProfessor;
    
    private String timezone;

    private String cpfProfessor;

    @OneToMany(mappedBy = "treinamento")
    private Set<Apostila> apostilas;

    @OneToMany(mappedBy = "treinamento")
    private Set<Aula> aulas;


    public Treinamento(DadosCadastroTreinamento dto) {
        this.nome = dto.nome();
        this.formato = dto.formato();
        this.nomeProfessor = dto.nomeProfessor();
        //this.cpfProfessor = dto.cpfProfessor();
        this.dataInicio = dto.dataInicio();
        this.dataFim = dto.dataFim();
        this.descricao = dto.descricao();
        this.formato = dto.formato();
        this.timezone = dto.timezone();
    }


    public void excluir() {
        this.ativo = false;
    }

    public void setTreinamento(DadosAlteracaoTreinamento dto){
        this.nome = dto.nome();
        this.formato = dto.formato();
        //this.capa = dto.capa();
        this.nomeProfessor = dto.nomeProfessor();
        this.cpfProfessor = dto.cpfProfessor();
        this.dataInicio = dto.dataInicio();
        this.dataFim = dto.dataFim();
        this.descricao = dto.descricao();
        this.formato = dto.formato();
        this.timezone = dto.timezone();
    }



}
