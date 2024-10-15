package med.voll.vollmed_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String carregarPaginaListagem() {
        return "autenticacao/login";
    }
}
