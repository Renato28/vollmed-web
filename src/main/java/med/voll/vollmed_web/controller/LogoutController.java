package med.voll.vollmed_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String carregarPaginaLogout() {
        return "autenticacao/logout";
    }
}
