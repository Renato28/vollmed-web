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

    public void salvarUsuario(String nome, String email, String senha) {
        String senhaCriptografada = encriptador.encode(senha);
        usuarioRepository.save(new Usuario(nome, email, senhaCriptografada));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuário não foi encontrado!"));
    }
}
