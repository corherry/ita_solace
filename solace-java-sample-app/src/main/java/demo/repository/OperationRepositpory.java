package demo.repository;

import demo.domain.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepositpory extends JpaRepository<OperationLog, Long> {
}
