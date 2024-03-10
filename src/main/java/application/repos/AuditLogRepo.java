package application.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import application.models.AuditLog;

public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {

}
