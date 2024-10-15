package med.voll.vollmed_web.controller;

import jakarta.validation.Valid;
import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.medico.DadosCadastroMedico;
import med.voll.vollmed_web.domain.medico.DadosListagemMedico;
import med.voll.vollmed_web.domain.medico.Especialidade;
import med.voll.vollmed_web.domain.medico.MedicoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MedicoController {

    private static final String PAGINA_LISTAGEM = "medico/listagem-medicos";
    private static final String PAGINA_CADASTRO = "medico/formulario-medico";
    private static final String REDIRECT_LISTAGEM = "redirect:/medicos?sucesso";

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @ModelAttribute("especialidades")
    public Especialidade[] especialidades() {
        return Especialidade.values();
    }

    @GetMapping
    public String carregarPaginaListagem(@PageableDefault Pageable paginacao, Model model) {
        var medicosCadastrados = medicoService.listar(paginacao);
        model.addAttribute("medicos", medicosCadastrados);
        return PAGINA_LISTAGEM;
    }

    @GetMapping("formulario")
    public String carregarPaginaCadastro(Long id, Model model) {
        if (id != null) {
            model.addAttribute("dados", medicoService.carregarPorId(id));
        } else {
            model.addAttribute("dados", new DadosCadastroMedico(null, "", "", "", "", null));
        }

        return PAGINA_CADASTRO;
    }

    @PostMapping
    public String cadastrar(@Valid @ModelAttribute("dados") DadosCadastroMedico dados, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }

        try {
            medicoService.cadastrar(dados);
            return REDIRECT_LISTAGEM;
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }
    }

    @DeleteMapping
    public String excluir(Long id) {
        medicoService.excluir(id);
        return REDIRECT_LISTAGEM;
    }

    @GetMapping({"especialidade"})
    @ResponseBody
    public List<DadosListagemMedico> listarMedicosPorEspecialidade(@PathVariable String especialidade) {
        return medicoService.listarPorEspecialidade(Especialidade.valueOf(especialidade));
    }
}
