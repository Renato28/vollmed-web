package med.voll.vollmed_web.domain.consulta;

import jakarta.persistence.*;
import med.voll.vollmed_web.domain.medico.Medico;
import med.voll.vollmed_web.domain.paciente.Paciente;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "medico_id")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    private LocalDateTime data;

    @Deprecated
    public Consulta(){}

    public Consulta(Medico medico, Paciente paciente, DadosAgendamentoConsulta dados) {
        modificarDados(medico, paciente, dados);
    }

    public void modificarDados(Medico medico, Paciente paciente, DadosAgendamentoConsulta dados) {
        this.medico = medico;
        this.paciente = paciente;
        this.data = dados.data();
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getData() {
        return data;
    }
}
