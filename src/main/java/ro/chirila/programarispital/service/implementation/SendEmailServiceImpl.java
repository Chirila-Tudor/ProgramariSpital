package ro.chirila.programarispital.service.implementation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ro.chirila.programarispital.repository.dto.AppointmentResponseDTO;
import ro.chirila.programarispital.repository.dto.TypeOfServiceDTO;
import ro.chirila.programarispital.repository.dto.UserExistsDTO;
import ro.chirila.programarispital.repository.dto.UserSecurityDTO;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.SendEmailService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    private final JavaMailSender emailSender;
    private final Configuration configuration;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Value("${spring.mail.password}")
    private String password;
    private String companyName = "Hospital appointment software";

    public SendEmailServiceImpl(JavaMailSender emailSender, Configuration configuration) {
        this.emailSender = emailSender;
        this.configuration = configuration;
    }

    @Override
    public void sendAppointmentEmail(AppointmentResponseDTO appointment) {
        String services = "";
        for(TypeOfServiceDTO typeOfServiceDTO : appointment.getTypeOfService()){
            services += typeOfServiceDTO.getService() + "\n";
        }
        MimeMessage message = new MimeMessage(getSession());
        try{
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(appointment.getEmail());
            helper.setSubject("Appointment to " + companyName);
            Template template  = configuration.getTemplate("appointment-mail.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", appointment.getFirstName());
            templateMapper.put("appointmentDate", appointment.getChooseDate());
            templateMapper.put("appointmentHour", appointment.getAppointmentHour());
            templateMapper.put("service", services);
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        }catch (MessagingException | IOException | TemplateException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordEmail(UserExistsDTO userExistsDTO, AppointmentResponseDTO appointment) {
        MimeMessage message = new MimeMessage(getSession());
        try{
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            User user = new User();
            user.setPassword(userExistsDTO.getPassword());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(appointment.getEmail());
            helper.setSubject("Appointment to " + companyName);
            Template template  = configuration.getTemplate("send-password.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", appointment.getFirstName());
            templateMapper.put("password", userExistsDTO.getPassword());
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        }catch (MessagingException | IOException | TemplateException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendForgotPasswordEmail(UserSecurityDTO userSecurityDTO) {
//        MimeMessage message = new MimeMessage(getSession());
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(
//                    message,
//                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//                    StandardCharsets.UTF_8.name());
//            helper.setFrom(new InternetAddress(adminEmail));
//            helper.setTo(userSecurityDTO.getEmail());
//            helper.setSubject("Password Reset Request");
//            Template template  = configuration.getTemplate("confirm-changing-password.html");
//            Map<String, Object> templateMapper = new HashMap<>();
//            templateMapper.put("username", userSecurityDTO.getUsername());
//            templateMapper.put("adminEmail", adminEmail);
//            templateMapper.put("companyName", companyName);
//            templateMapper.put("securityCode", userSecurityDTO.getSecurityCode());
//            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
//            helper.setText(htmlTemplate, true);
//            Transport.send(message);
//        } catch (MessagingException | IOException | TemplateException e) {
//            throw new RuntimeException(e);
//        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "*");
        return properties;
    }

    private Session getSession() {
        Properties properties = getProperties();
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, password);
            }
        });
        return session;
    }

}
