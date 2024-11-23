package med.voll.vollmed_web.controller;

import jakarta.validation.Valid;
import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.usuario.DadosAlteracaoSenha;
import med.voll.vollmed_web.domain.usuario.Usuario;
import med.voll.vollmed_web.domain.usuario.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    private static final String FORMULARIO_ALTERACAO_SENHA = "autenticacao/formulario-alteracao-senha";

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String carregarPaginaListagem() {
        return "autenticacao/login";
    }

    @GetMapping("/alterar-senha")
    public String carregarPaginaAlteracao() {
        return FORMULARIO_ALTERACAO_SENHA;
    }

    @PostMapping("/alterar-senha")
    public String alterarSenha(@Valid @ModelAttribute("dados") DadosAlteracaoSenha dados,
                               BindingResult result, Model model, @AuthenticationPrincipal Usuario logado) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERACAO_SENHA;
        }

        try {
            usuarioService.alterarSenha(dados, logado);
            return "redirect:home";
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERACAO_SENHA;
        }
    }
}
