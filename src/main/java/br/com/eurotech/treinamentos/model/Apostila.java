package br.com.eurotech.treinamentos.model;

import br.com.eurotech.treinamentos.dto.apostila.DadosAlteracaoApostila;
import br.com.eurotech.treinamentos.dto.apostila.DadosCadastroApostila;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tb_apostila")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Apostila {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_treinamento",nullable = false)
    private Treinamento treinamento;

    private String link;


    public Apostila(DadosCadastroApostila dados){
        this.link = dados.link();
        this.treinamento = dados.treinamento();
    }

    public void setApostila(DadosAlteracaoApostila dados) {
        this.link = dados.link();
    }





}
