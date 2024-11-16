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

    public Long salvarUsuario(String nome, String email, String senha) {
        String senhaCriptografada = encriptador.encode(senha);
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, senhaCriptografada));
        return usuario.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuário não foi encontrado!"));
    }
}
