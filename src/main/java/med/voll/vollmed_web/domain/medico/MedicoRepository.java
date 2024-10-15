package med.voll.vollmed_web.domain.medico;

import med.voll.vollmed_web.domain.medico.Especialidade;
import med.voll.vollmed_web.domain.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Query("""
            SELECT
                CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END
            FROM
                Medico m
            WHERE (m.email = :email OR m.crm = :crm) AND (:id IS NULL OR m.id <> :id)
            """)
    boolean isJaCadastrado(String email, String crm, Long id);

    List<Medico> findByEspecialidade(Especialidade especialidade);
}
