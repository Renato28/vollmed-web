package med.voll.vollmed_web.domain.consulta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findAllByOrderByData(Pageable paginacao);

    @Query("SELECT c FROM Consulta c WHERE (c.medico.id = :id OR c.paciente.id = :id) ORDER BY c.data")
    Page<DadosListagemConsulta> buscarConsultas(Pageable paginacao, Long id);
}
