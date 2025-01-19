package med.voll.vollmed_web.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosRecuperacaoConta(@NotBlank String novaSenha, @NotBlank String novaSenhaConfirmacao) {
}
