package application.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import application.models.AuditLog;
import application.repos.AuditLogRepo;
import application.services.AuthService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final AuditLogRepo auditLogRepo;
    private final AuthService authService;

    @MessageMapping("/users_chat")
    @SendTo("/topic/users_messages")
    @PreAuthorize("hasAuthority('ROLE_USERS') OR hasAuthority('ROLE_ADMIN')")
    public String handleUserChatMessage(String message) {
        String username = authService.getAuthInfo().getName();
        auditLogRepo.save(new AuditLog("Sent message " + message + " to users chat", username));
        forwardMessageToExternalServer(message);
        return message;
    }

    @MessageMapping("/admins_chat")
    @SendTo("/topic/admins_messages")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String handleAdminChatMessage(String message) {
        String username = authService.getAuthInfo().getName();
        auditLogRepo.save(new AuditLog("Sent message " + message + " to admin chat", username));
        forwardMessageToExternalServer(message);
        return message;
    }

    @MessageMapping("/posts_chat")
    @SendTo("/topic/posts_messages")
    @PreAuthorize("hasAuthority('ROLE_POSTS') OR hasAuthority('ROLE_ADMIN')")
    public String handlePostsChatMessage(String message) {
        String username = authService.getAuthInfo().getName();
        auditLogRepo.save(new AuditLog("Sent message " + message + " to POSTS chat", username));
        forwardMessageToExternalServer(message);
        return message;
    }

    @MessageMapping("/albums_chat")
    @SendTo("/topic/albums_messages")
    @PreAuthorize("hasAuthority('ROLE_ALBUMS') OR hasAuthority('ROLE_ADMIN')")
    public String handleAlbumsChatMessage(String message) {
        String username = authService.getAuthInfo().getName();
        auditLogRepo.save(new AuditLog("Sent message " + message + " to albums chat", username));
        forwardMessageToExternalServer(message);
        return message;
    }

    private void forwardMessageToExternalServer(String message) {
        messagingTemplate.convertAndSend("wss://echo.websocket.org", message);
    }
}
