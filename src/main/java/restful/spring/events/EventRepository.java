package restful.spring.events;

// JpaRepository를 상속받기 위해 'Spring Data Jpa' dependency 필요
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
