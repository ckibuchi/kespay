package kespay.repositories;

import kespay.enums.PaymentStatus;
import kespay.models.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
Payment findPaymentByRefID(String refID);
    Payment findPaymentById(int Id);
    List<Payment> findPaymentsByMsisdnAndPaymentDateAndStatus(String msisdn,Date paymentDate,PaymentStatus status);
    List<Payment> findPaymentsByPaymentDate(Date paymentDate);
    List<Payment> findPaymentsByCarRegNoAndPaymentDate(String carRegNo,Date paymentDate);
    List<Payment>  findPaymentsByCarRegNoAndCountyAndSubCountyAndPaymentDateAndStatus(String carRegNo,String county,String subCounty,Date paymentDate,PaymentStatus status);
    List<Payment> findPaymentsByCarRegNoIgnoreCaseContainingAndCountyIgnoreCaseContainingAndSubCountyIgnoreCaseContainingAndPaymentDate(String carRegNo,String county,String subCounty,Date paymentDate);
    List<Payment> findPaymentsByCarRegNoIgnoreCaseContainingAndCountyIgnoreCaseContainingAndSubCountyIgnoreCaseContainingAndPaymentDateGreaterThanEqualAndPaymentDateIsLessThanEqual(String carRegNo,String county,String subCounty,Date fromDate,Date toDate);
    List<Payment> findPaymentsByCarRegNoIgnoreCaseContainingAndCountyIgnoreCaseContainingAndSubCountyIgnoreCaseContainingAndPaymentDateGreaterThanEqualAndPaymentDateIsLessThanEqualAndStatusEquals(String carRegNo,String county,String subCounty,Date fromDate,Date toDate,PaymentStatus status);
    Long countByPaymentDateAndStatus(Date today,PaymentStatus status);
    @Query(" select SUM(amount) from Payment p where p.status='COMPLETED' and p.paymentDate = ?1")
    Float getTotalToday(Date today);

}
