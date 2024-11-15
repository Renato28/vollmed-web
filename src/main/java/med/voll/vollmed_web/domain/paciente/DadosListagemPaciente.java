package med.voll.vollmed_web.domain.paciente;

public record DadosListagemPaciente(Long id, String nome, String email, String cpf, String telefone) {
    public DadosListagemPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf(), paciente.getTelefone());
    }
}
