package med.voll.vollmed_web.domain.consulta;

import med.voll.vollmed_web.domain.medico.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
    }

    public Page<DadosListagemConsulta> listar(Pageable paginacao) {
        return consultaRepository.findAllByOrderByData(paginacao).map(DadosListagemConsulta::new);
    }

    @Transactional
    public void cadastrar(DadosAgendamentoConsulta dados) {
        var medicoConsulta = medicoRepository.findById(dados.idMedico()).orElseThrow();
        if (dados.id() == null) {
            consultaRepository.save(new Consulta(medicoConsulta, dados));
        } else {
            var consulta = consultaRepository.findById(dados.id()).orElseThrow();
            consulta.modificarDados(medicoConsulta, dados);
        }
    }

    public DadosAgendamentoConsulta carregarPorId(Long id) {
        var consulta = consultaRepository.findById(id).orElseThrow();
        var medicoConsulta = medicoRepository.getReferenceById(consulta.getMedico().getId());
        return new DadosAgendamentoConsulta(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente(), consulta.getData(), medicoConsulta.getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        consultaRepository.deleteById(id);
    }
}
