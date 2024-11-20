package med.voll.vollmed_web.domain.consulta;

import med.voll.vollmed_web.domain.medico.MedicoRepository;
import med.voll.vollmed_web.domain.paciente.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public Page<DadosListagemConsulta> listar(Pageable paginacao) {
        return consultaRepository.findAllByOrderByData(paginacao).map(DadosListagemConsulta::new);
    }

    @Transactional
    public void cadastrar(DadosAgendamentoConsulta dados) {
        var medicoConsulta = medicoRepository.findById(dados.idMedico()).orElseThrow();
        var pacienteConsulta = pacienteRepository.findByCpf(dados.paciente()).orElseThrow();
        if (dados.id() == null) {
            consultaRepository.save(new Consulta(medicoConsulta, pacienteConsulta, dados));
        } else {
            var consulta = consultaRepository.findById(dados.id()).orElseThrow();
            consulta.modificarDados(medicoConsulta, pacienteConsulta, dados);
        }
    }

    public DadosAgendamentoConsulta carregarPorId(Long id) {
        var consulta = consultaRepository.findById(id).orElseThrow();
        var medicoConsulta = medicoRepository.getReferenceById(consulta.getMedico().getId());
        return new DadosAgendamentoConsulta(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getNome(), consulta.getData(), medicoConsulta.getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        consultaRepository.deleteById(id);
    }
}
