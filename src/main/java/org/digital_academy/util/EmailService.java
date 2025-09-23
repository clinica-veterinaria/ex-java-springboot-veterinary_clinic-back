package org.digital_academy.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String UTF8_ENCODING = "UTF-8";
    private static final String DASHBOARD_URL = "http://localhost:5173/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendAppointmentConfirmation(String toEmail, String tutorName, String petName, LocalDateTime fechaHora)
            throws MessagingException {
        sendEmail(toEmail,
                "Confirmación de cita en Clínica Margarita 🐾",
                "AppointmentConfirmation",
                tutorName, petName, fechaHora);
    }

    public void sendAppointmentUpdate(String toEmail, String tutorName, String petName, LocalDateTime fechaHora)
            throws MessagingException {
        sendEmail(toEmail,
                "Tu cita ha sido modificada 🐾",
                "AppointmentUpdate",
                tutorName, petName, fechaHora);
    }

    public void sendAppointmentCancellation(String toEmail, String tutorName, String petName, LocalDateTime fechaHora)
            throws MessagingException {
        sendEmail(toEmail,
                "Tu cita ha sido cancelada 🐾",
                "AppointmentCancellation",
                tutorName, petName, fechaHora);
    }

    private void sendEmail(String toEmail, String subject, String templateName,
            String tutorName, String petName, LocalDateTime fechaHora) throws MessagingException {

        Context context = new Context();
        context.setVariable("tutorName", tutorName);
        context.setVariable("petName", petName);
        context.setVariable("fecha", fechaHora.format(DATE_FORMAT));
        context.setVariable("hora", fechaHora.format(TIME_FORMAT));
        context.setVariable("dashboardUrl", DASHBOARD_URL);

        String htmlContent = templateEngine.process("email/" + templateName, context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF8_ENCODING);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        ClassPathResource image = new ClassPathResource("static/assets/imgs/Oliwa_LOGO.png");
        helper.addInline("logoImage", image);

        mailSender.send(message);
    }
}
