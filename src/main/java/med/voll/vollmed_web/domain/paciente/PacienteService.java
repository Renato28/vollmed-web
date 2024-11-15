package med.voll.vollmed_web.domain.paciente;

import med.voll.vollmed_web.domain.RegraDeNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Page<DadosListagemPaciente> listar(Pageable paginacao) {
        return pacienteRepository.findAll(paginacao).map(DadosListagemPaciente::new);
    }

    @Transactional
    public void cadastrar(DadosCadastroPaciente dados) {
        if (pacienteRepository.isJaCadastrado(dados.email(), dados.cpf(), dados.id())) {
            throw new RegraDeNegocioException("E-mail ou CPF já cadastrado para outro paciente!");
        }

        if (dados.id() == null) {
            pacienteRepository.save(new Paciente(dados));
        } else {
            var paciente = pacienteRepository.findById(dados.id()).orElseThrow();
            paciente.modificarDados(dados);
        }
    }

    public DadosCadastroPaciente carregarPorId(Long id) {
        var paciente = pacienteRepository.findById(id).orElseThrow();
        return new DadosCadastroPaciente(paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getCpf());
    }

    @Transactional
    public void excluir(Long id) {
        pacienteRepository.deleteById(id);
    }
}
