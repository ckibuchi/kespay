package kespay.repositories;

import kespay.enums.PaymentStatus;
import kespay.models.Settlement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface SettlementRepository extends CrudRepository<Settlement, Long> {
List<Settlement> findAllBySettlementDateAndStatus(Date date,PaymentStatus status);
}
