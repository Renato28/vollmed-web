package med.voll.vollmed_web.domain.medico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.vollmed_web.domain.medico.Especialidade;

public record DadosCadastroMedico(
    Long id,
    @NotBlank
    String nome,
    @NotBlank
    String email,
    @NotBlank
    String telefone,
    @NotBlank
    @Pattern(regexp = "\\{4,6}", message = "CRM deve ter de 4 a 6 digitos numéricos")
    String crm,
    @NotNull
    Especialidade especialidade
) {}
