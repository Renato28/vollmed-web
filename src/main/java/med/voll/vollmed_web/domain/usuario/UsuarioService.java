package med.voll.vollmed_web.domain.usuario;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encriptador;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encriptador) {
        this.usuarioRepository = usuarioRepository;
        this.encriptador = encriptador;
    }

    public Long salvarUsuario(String nome, String email, String senha, Perfil perfil) {
        String senhaCriptografada = encriptador.encode(senha);
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, senhaCriptografada, perfil));
        return usuario.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuário não foi encontrado!"));
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }
}
