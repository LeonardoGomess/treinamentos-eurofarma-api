package br.com.eurotech.treinamentos.model;

import java.util.List;

import br.com.eurotech.treinamentos.dto.aula.DadosAlteracaoAula;
import br.com.eurotech.treinamentos.dto.aula.DadosCadastroAula;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name="tb_aula")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Aula {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sala;

    private Double duracao;

    private String nome;

    private String link;

    @ManyToOne
    @JoinColumn(name = "id_treinamento",nullable = false)
    private Treinamento treinamento;


    private Boolean ativo = true;

    @OneToMany(mappedBy = "aula")
    private List<AlunoAula> alunoAula;
    

    // public Long getTempoAula(){
    //     return Duration.between(dataInicio, dataFim).toMinutes();
    // }

    public void setAula(DadosAlteracaoAula dados) {
        this.sala = dados.sala();
        this.nome = dados.nome();
        this.link = dados.link();
        this.duracao = dados.duracao();
    }

    public Aula(DadosCadastroAula dados) {
        this.sala = dados.sala();
        this.nome = dados.nome();
        this.link = dados.link();
        this.treinamento = dados.treinamento();
        this.duracao = dados.duracao();
    }
    
}
