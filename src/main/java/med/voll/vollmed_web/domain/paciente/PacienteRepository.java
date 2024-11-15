package med.voll.vollmed_web.domain.paciente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @Query("""
            SELECT 
                CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END
            FROM 
                Paciente p
            WHERE (p.email = :email OR p.cpf = :cpf) AND (:id IS NULL OR p.id <> :id)
            """)
    boolean isJaCadastrado(String email, String cpf, Long id);

    Optional<Paciente> findByCpf(String cpf);
}
