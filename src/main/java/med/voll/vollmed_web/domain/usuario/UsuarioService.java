package med.voll.vollmed_web.domain.usuario;

import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.usuario.email.EmailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encriptador;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encriptador, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.encriptador = encriptador;
        this.emailService = emailService;
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

    public void alterarSenha(DadosAlteracaoSenha dados, Usuario logado) {

        if (!encriptador.matches(dados.senhaAtual(), logado.getPassword())) {
            throw new RegraDeNegocioException("Senha e confirmação não conferem!");
        }

        String senhaCriptografada = encriptador.encode(dados.novaSenha());
        logado.alterarSenha(senhaCriptografada);

        usuarioRepository.save(logado);
    }

    public void enviarToken(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(() ->
                new RegraDeNegocioException("Usuário não encontrado"));
        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);
        emailService.enviarEmailSenha(usuario);
    }

    public void recuperarConta(String codigo, DadosRecuperacaoConta dados) {
        Usuario usuario = usuarioRepository.findByTokenIgnoreCase(codigo)
                .orElseThrow(
                        () -> new RegraDeNegocioException("Link inválido!"));

        if (usuario.getExpiracaoToken().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Link expirado");
        }

        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao())) {
            throw new RegraDeNegocioException("Senha e confirmação não conferem");
        }

        String senhaCriptografada = encriptador.encode(dados.novaSenha());
        usuario.alterarSenha(senhaCriptografada);

        usuario.setToken(null);
        usuario.setExpiracaoToken(null);

        usuarioRepository.save(usuario);
    }
}
