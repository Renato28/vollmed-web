package med.voll.vollmed_web.domain.medico;

import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.usuario.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final UsuarioService usuarioService;

    public MedicoService(MedicoRepository medicoRepository, UsuarioService usuarioService) {
        this.medicoRepository = medicoRepository;
        this.usuarioService = usuarioService;
    }

    public Page<DadosListagemMedico> listar(Pageable paginacao) {
        return medicoRepository.findAll(paginacao).map(DadosListagemMedico::new);
    }

    @Transactional
    public void cadastrar(DadosCadastroMedico dados) {
        if (medicoRepository.isJaCadastrado(dados.email(), dados.crm(), dados.id())) {
            throw new RegraDeNegocioException("E-mail ou CRM já cadastrado para outro médico!");
        }

        if (dados.id() == null) {
            medicoRepository.save(new Medico(dados));
            usuarioService.salvarUsuario(dados.nome(), dados.email(), dados.crm());
        } else {
            var medico = medicoRepository.findById(dados.id()).orElseThrow();
            medico.atualizarDados(dados);
        }
    }

    public DadosCadastroMedico carregarPorId(Long id) {
        var medico = medicoRepository.findById(id).orElseThrow();
        return new DadosCadastroMedico(medico.getId(), medico.getNome(), medico.getEmail(), medico.getTelefone(), medico.getCrm(), medico.getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        medicoRepository.deleteById(id);
    }

    public List<DadosListagemMedico> listarPorEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidade(especialidade).stream().map(DadosListagemMedico::new).toList();
    }
}
