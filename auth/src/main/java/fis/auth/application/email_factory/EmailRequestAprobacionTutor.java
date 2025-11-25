package fis.auth.application.email_factory;

import fis.auth.application.repository.UserMSRepository;
import fis.auth.domain.model.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Qualifier("email-aprobacion")
@RequiredArgsConstructor
public class EmailRequestAprobacionTutor extends EmailRequestFactory {

    private final UserMSRepository repository;

    @Override
    public EmailRequest create(String email) {
        int userId = repository.findUserIdByEmail(email);

        String subject = "Requerimiento de Aprobación - Registro de Usuario Menor de Edad";

        String approvalLink = "http://localhost:8080/auth/api/v1/approve?userId=" + userId;

        // Obtener la fecha y hora actual formateada
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        String htmlBody = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Aprobación Requerida</title>
                <style>
                    body {
                        font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
                        line-height: 1.6;
                        color: #4A4A4A;
                        background-color: #F8F9FA; /* Fondo muy suave */
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #FFFFFF;
                        border: 1px solid #EAEAEA;
                        border-radius: 8px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
                        overflow: hidden;
                    }
                    .header {
                        background-color: #5D728A; /* Azul grisáceo sólido, elegante */
                        color: white;
                        padding: 30px;
                        text-align: center;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 24px;
                        font-weight: 300;
                    }
                    .content {
                        padding: 30px;
                    }
                    .content h2 {
                        color: #333333;
                        font-size: 20px;
                        margin-top: 0;
                        border-bottom: 2px solid #EEEEEE;
                        padding-bottom: 10px;
                        margin-bottom: 20px;
                    }
                    .user-info-box {
                        background-color: #F0F4F7; /* Color de fondo muy claro para la caja de datos */
                        padding: 15px;
                        border-left: 5px solid #5D728A; /* Borde de acento */
                        border-radius: 4px;
                        margin: 20px 0;
                        font-size: 14px;
                    }
                    .user-info-box strong {
                        display: block;
                        margin-bottom: 5px;
                        color: #333333;
                    }
                    .button-area {
                        text-align: center;
                        margin: 30px 0;
                    }
                    .button {
                        display: inline-block;
                        background-color: #4CAF50; /* Verde de acción, suave y claro */
                        color: white !important; /* Importante para que el color del texto sea visible */
                        padding: 12px 25px;
                        text-decoration: none;
                        border-radius: 4px;
                        font-weight: bold;
                        transition: background-color 0.3s ease;
                    }
                    .button:hover {
                        background-color: #45A049;
                    }
                    .important-note {
                        border-top: 1px solid #EEEEEE;
                        margin-top: 30px;
                        padding-top: 20px;
                        font-size: 14px;
                        color: #666666;
                    }
                    .important-note ul {
                        padding-left: 20px;
                        margin-top: 10px;
                    }
                    .footer {
                        text-align: center;
                        padding: 20px;
                        background-color: #EEEEEE;
                        border-top: 1px solid #EAEAEA;
                        font-size: 11px;
                        color: #888888;
                        border-radius: 0 0 8px 8px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Solicitud de Aprobación de Registro</h1>
                    </div>
                   \s
                    <div class="content">
                        <h2>Estimado/a Tutor/a,</h2>
                       \s
                        <p>Hemos recibido una solicitud de registro para un usuario menor de edad que requiere su autorización explícita para continuar.</p>
                       \s
                        <div class="user-info-box">
                            <strong>Detalles del Registro Pendiente:</strong>
                            Email del usuario: %s<br>
                            ID de registro: %d<br>
                            Fecha de solicitud: %s
                        </div>
                       \s
                        <p>Como tutor o persona autorizada, le solicitamos que apruebe este registro para que el menor pueda acceder y utilizar nuestra plataforma.</p>
                       \s
                        <div class="button-area">
                            <a href="%s" class="button">
                                Aprobar Registro
                            </a>
                        </div>
                       \s
                        <div class="important-note">
                            <strong>Consideraciones Importantes:</strong>
                            <ul>
                                <li>Al aprobar, usted asume la responsabilidad de la cuenta del usuario menor de edad.</li>
                                <li>Este enlace de aprobación es válido por un período de 24 horas.</li>
                                <li>Si usted no reconoce esta solicitud, por favor, ignore este correo electrónico.</li>
                            </ul>
                        </div>
                    </div>
                   \s
                    <div class="footer">
                        <p>Este es un mensaje automático. Por favor, no responda a esta dirección de correo.</p>
                        <p>© %s Plataforma Educativa. Todos los derechos reservados.</p>
                    </div>
                </div>
            </body>
            </html>
           \s""".formatted(
                email,
                userId,
                formattedDateTime,
                approvalLink,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")) // Año actual para el footer
        );

        return new EmailRequest(
                userId,
                "tutor@plataforma.com",
                subject,
                htmlBody,
                true
        );
    }
}