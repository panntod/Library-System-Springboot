package panntod.core.library.library_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import panntod.core.library.library_system.entities.Book;

import java.util.UUID;

public interface BookRepository  extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
}
