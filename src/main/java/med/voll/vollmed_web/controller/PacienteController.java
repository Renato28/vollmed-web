package med.voll.vollmed_web.controller;

import jakarta.validation.Valid;
import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.paciente.DadosCadastroPaciente;
import med.voll.vollmed_web.domain.paciente.PacienteService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("pacientes")
@PreAuthorize("hasRole('ATENDENTE')")
public class PacienteController {

    private static final String PAGINA_LISTAGEM = "paciente/listagem-pacientes";
    private static final String PAGINA_CADASTRO = "paciente/formulario-paciente";
    private static final String REDIRECT_LISTAGEM = "redirect:/pacientes?sucesso";

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public String carregarPaginaListagem(@PageableDefault Pageable paginacao, Model model) {
        var pacientesCadastrados = pacienteService.listar(paginacao);
        model.addAttribute("pacientes", pacientesCadastrados);
        return PAGINA_LISTAGEM;
    }

    @GetMapping("formulario")
    public String carregarPaginaCadastro(Long id, Model model) {
        if (id != null) {
            model.addAttribute("dados", pacienteService.carregarPorId(id));
        } else {
            model.addAttribute("dados", new DadosCadastroPaciente(null, "", "", "", ""));
        }

        return PAGINA_CADASTRO;
    }

    @PostMapping
    public String cadastrar(@Valid @ModelAttribute("dados") DadosCadastroPaciente dados, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }

        try {
            pacienteService.cadastrar(dados);
            return REDIRECT_LISTAGEM;
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }
    }

    @DeleteMapping
    public String excluir(Long id) {
        pacienteService.excluir(id);
        return REDIRECT_LISTAGEM;
    }
}
