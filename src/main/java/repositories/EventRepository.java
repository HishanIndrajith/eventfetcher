package repositories;

import models.EventEventBriteAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEventBriteAPI, Long>
{

}
