package med.voll.vollmed_web.controller;

import jakarta.validation.Valid;
import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.consulta.ConsultaService;
import med.voll.vollmed_web.domain.consulta.DadosAgendamentoConsulta;
import med.voll.vollmed_web.domain.medico.Especialidade;
import med.voll.vollmed_web.domain.usuario.Usuario;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("consultas")
public class ConsultaController {

    private static final String PAGINA_LISTAGEM = "consulta/listagem-consultas";
    private static final String PAGINA_CADASTRO = "consulta/formulario-consulta";
    private static final String REDIRECT_LISTAGEM = "redirect:/consultas?sucesso";

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @ModelAttribute("especialidades")
    public Especialidade[] especialidades() {
        return Especialidade.values();
    }

    @GetMapping
    public String carregarPaginaListagem(@PageableDefault Pageable paginacao, Model model, Usuario logado) {
        var consultasAtivas = consultaService.listar(paginacao, logado);
        model.addAttribute("consultas", consultasAtivas);
        return PAGINA_LISTAGEM;
    }

    @GetMapping("formulario")
    @PreAuthorize("hasRole('ATENDENTE') OR hasRole('PACIENTE')")
    public String carregarPaginaAgendaConsulta(Long id, Model model) {
        if (id != null) {
            model.addAttribute("dados", consultaService.carregarPorId(id));
        } else {
            model.addAttribute("dados", new DadosAgendamentoConsulta(null, null, "", null, null));
        }

        return PAGINA_CADASTRO;
    }

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE') OR hasRole('PACIENTE')")
    public String cadastrar(@Valid @ModelAttribute("dados") DadosAgendamentoConsulta dados, BindingResult result, Model model, @AuthenticationPrincipal Usuario logado) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }

        try {
            consultaService.cadastrar(dados, logado);
            return REDIRECT_LISTAGEM;
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }
    }

    @DeleteMapping
    public String excluir(Long id) {
        consultaService.excluir(id);
        return REDIRECT_LISTAGEM;
    }
}
