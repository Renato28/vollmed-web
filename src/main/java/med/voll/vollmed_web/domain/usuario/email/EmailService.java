package med.voll.vollmed_web.domain.usuario.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import med.voll.vollmed_web.domain.RegraDeNegocioException;
import med.voll.vollmed_web.domain.usuario.Usuario;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private static final String EMAIL_ORIGEM = "vollmed@email.com";
    private static final String NOME_ENVIADOR = "Clínica Voll Med";
    private static final String URL_SITE = "http://localhost:8080";

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void enviarEmail(String emailUsuario, String assunto, String conteudo) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(EMAIL_ORIGEM, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RegraDeNegocioException("Erro ao enviar email");
        }

        javaMailSender.send(message);
    }

    public void enviarEmailSenha(Usuario usuario) {
        String assunto = "Aqui está seu link para alterar a senha";
        String conteudo = gerarConteudoEmail("Olá [[name]],<br>"
                + "Por favor clique no link abaixo para alterar a senha:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ALTERAR</a></h3>"
                + "Obrigado,<br>"
                + "Clinica Voll Med.", usuario.getNome(), URL_SITE +
                "/recuperar-conta?codigo=" + usuario.getToken());
        enviarEmail(usuario.getUsername(), assunto, conteudo);
    }

    private String gerarConteudoEmail(String template, String nome, String url) {

        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }
}
