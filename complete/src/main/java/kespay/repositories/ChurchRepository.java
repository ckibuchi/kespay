package kespay.repositories;

import kespay.models.Church;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChurchRepository  extends CrudRepository<Church, Long> {
    Church findChurchById(int Id);
}
