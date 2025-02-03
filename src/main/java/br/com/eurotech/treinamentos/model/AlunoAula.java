package br.com.eurotech.treinamentos.model;

import br.com.eurotech.treinamentos.dto.aula.DadosIdAula;
import br.com.eurotech.treinamentos.dto.usuario.DadosIdUsuario;
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
@Table(name="tb_aluno_aula")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoAula {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aula")
    private Aula aula;

    @ManyToOne
    @JoinColumn(name = "id_aluno")
    private Usuario aluno;
    
    private Boolean aula_concluida = false;

    private String assinatura;

    public AlunoAula(Usuario aluno,Aula aula){
        this.aluno = aluno;
        this.aula = aula;
    }

}
