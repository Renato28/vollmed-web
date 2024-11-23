package med.voll.vollmed_web.domain.consulta;

import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.medico.MedicoRepository;
import med.voll.vollmed_web.domain.paciente.PacienteRepository;
import med.voll.vollmed_web.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public Page<DadosListagemConsulta> listar(Pageable paginacao, Usuario logado) {
        if (logado.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ATENDENTE")))
            return consultaRepository.findAllByOrderByData(paginacao).map(DadosListagemConsulta::new);
        return consultaRepository.buscarConsultas(paginacao, logado.getId());
    }

    @Transactional
    public void cadastrar(DadosAgendamentoConsulta dados, Usuario logado) {
        var medicoConsulta = medicoRepository.findById(dados.idMedico()).orElseThrow();
        var pacienteConsulta = pacienteRepository.findByCpf(dados.paciente()).orElseThrow();

        if (logado.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PACIENTE"))
                && !pacienteConsulta.getId().equals(logado.getId()))
            throw new RegraDeNegocioException("Cpf inválido!");

        if (dados.id() == null) {
            consultaRepository.save(new Consulta(medicoConsulta, pacienteConsulta, dados));
        } else {
            var consulta = consultaRepository.findById(dados.id()).orElseThrow();
            consulta.modificarDados(medicoConsulta, pacienteConsulta, dados);
        }
    }

    @PreAuthorize("hasRole('ATENDENTE') or " +
            "(hasRole('PACIENTE') and @consultaRepository.findById(#id).get().paciente.id == principal.id")
    public DadosAgendamentoConsulta carregarPorId(Long id) {

        var consulta = consultaRepository.findById(id).orElseThrow();
        var medicoConsulta = medicoRepository.getReferenceById(consulta.getMedico().getId());
        return new DadosAgendamentoConsulta(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getNome(), consulta.getData(), medicoConsulta.getEspecialidade());
    }

    @Transactional
    @PreAuthorize("hasRole('ATENDENTE') or" +
            "(hasRole('PACIENTE') and @consultaRepository.findById(#id).get().paciente.id == principal.id) or " +
            "(hasRole('MEDICO') and #id == principal.id)")
    public void excluir(Long id) {
        consultaRepository.deleteById(id);
    }
}
