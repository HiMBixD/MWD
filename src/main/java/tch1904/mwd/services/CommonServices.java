package tch1904.mwd.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tch1904.mwd.entity.RoleAccount;
import tch1904.mwd.repository.RoleRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.Random;


@Service
public class CommonServices {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private RoleRepository roleRepository;



    @Autowired
    private UserService userService;

    public List<RoleAccount> getAllRoles() {
        return roleRepository.findAll();
    }

    public UserDetails getCurrentUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void sendEmail(String directEmail, String content, String header) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        message.setContent(content, "text/html");
        helper.setTo(directEmail);
        helper.setSubject(header);
        this.emailSender.send(message);
    }

    public String randomString(int length) {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string

        for(int i = 0; i < length; i++) {
            // generate random index number
            int index = random.nextInt(alphabet.length());
            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);
            // append the character to string builder
            sb.append(randomChar);
        }

        return sb.toString();
    }

}
