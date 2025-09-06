package panntod.core.library.library_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import panntod.core.library.library_system.entities.Borrow;

import java.util.UUID;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, UUID>, JpaSpecificationExecutor<Borrow> {
}
