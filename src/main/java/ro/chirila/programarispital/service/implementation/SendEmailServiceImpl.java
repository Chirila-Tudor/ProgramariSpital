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
import ro.chirila.programarispital.repository.AppointmentRepository;
import ro.chirila.programarispital.repository.dto.*;
import ro.chirila.programarispital.repository.entity.Appointment;
import ro.chirila.programarispital.repository.entity.User;
import ro.chirila.programarispital.service.SendEmailService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    private final JavaMailSender emailSender;
    private final Configuration configuration;
    private final AppointmentRepository appointmentRepository;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Value("${spring.mail.password}")
    private String password;
    private String companyName = "Spitalul General \"Armonia\"";

    public SendEmailServiceImpl(JavaMailSender emailSender, Configuration configuration, AppointmentRepository appointmentRepository) {
        this.emailSender = emailSender;
        this.configuration = configuration;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void sendAppointmentEmail(AppointmentResponseDTO appointment) {
        String services = "";
        for (TypeOfServiceDTO typeOfServiceDTO : appointment.getTypeOfServices()) {
            services += typeOfServiceDTO.getService() + "\n";
        }
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(appointment.getEmail());
            helper.setSubject("Appointment to " + companyName);
            Template template = configuration.getTemplate("appointment-mail.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", appointment.getFirstName() + " " + appointment.getLastName());
            templateMapper.put("appointmentDate", appointment.getChooseDate());
            templateMapper.put("appointmentHour", appointment.getAppointmentHour());
            templateMapper.put("service", services);
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordEmail(UserExistsDTO userExistsDTO, AppointmentResponseDTO appointment) {
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            User user = new User();
            user.setPassword(userExistsDTO.getPassword());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(appointment.getEmail());
            helper.setSubject("Appointment to " + companyName);
            Template template = configuration.getTemplate("send-password.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", appointment.getFirstName() + " " + appointment.getLastName());
            templateMapper.put("password", userExistsDTO.getPassword());
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordForHospitalPersonal(UserExistsDTO userExistsDTO, UserRequestDTO userRequestDTO) {
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            User user = new User();
            user.setPassword(userExistsDTO.getPassword());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(userRequestDTO.getEmail());
            helper.setSubject("Appointment to " + companyName);
            Template template = configuration.getTemplate("send-password.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", userRequestDTO.getUsername());
            templateMapper.put("password", userExistsDTO.getPassword());
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendUpdatedAppointmentEmail(AppointmentUpdateDTO appointmentUpdateDTO, Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);

        Appointment appointment = optionalAppointment.get();
        String recipientEmail = appointment.getEmail();

        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(new InternetAddress(recipientEmail));
            helper.setSubject("Appointment to " + companyName);
            Template template = configuration.getTemplate("updated-appointment-mail.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", appointment.getFirstName() + " " + appointment.getLastName());
            templateMapper.put("appointmentDate", appointmentUpdateDTO.getChooseDate());
            templateMapper.put("appointmentHour", appointmentUpdateDTO.getAppointmentHour());
            templateMapper.put("periodOfTime", appointmentUpdateDTO.getPeriodOfAppointment());
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendForgotPasswordEmail(String email, String securityCode,String username) {
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            Template template  = configuration.getTemplate("send-security-code.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", username);
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            templateMapper.put("securityCode", securityCode);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPassword(String email, String username, String password) {
        MimeMessage message = new MimeMessage(getSession());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(new InternetAddress(adminEmail));
            helper.setTo(email);
            helper.setSubject("Appointment to " + companyName);
            Template template = configuration.getTemplate("send-password.html");
            Map<String, Object> templateMapper = new HashMap<>();
            templateMapper.put("username", username);
            templateMapper.put("password", password);
            templateMapper.put("adminEmail", adminEmail);
            templateMapper.put("companyName", companyName);
            String htmlTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateMapper);
            helper.setText(htmlTemplate, true);
            Transport.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
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

