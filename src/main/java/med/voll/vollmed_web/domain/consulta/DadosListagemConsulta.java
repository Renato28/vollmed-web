package med.voll.vollmed_web.domain.consulta;

import med.voll.vollmed_web.domain.medico.Especialidade;

import java.time.LocalDateTime;

public record DadosListagemConsulta(Long id, String medico, String paciente, LocalDateTime data, Especialidade especialidade) {

    public DadosListagemConsulta(Consulta consulta) {
        this(consulta.getId(), consulta.getMedico().getNome(), consulta.getPaciente(), consulta.getData(), consulta.getMedico().getEspecialidade());
    }
}
