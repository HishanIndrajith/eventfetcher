package net.codegen.repositories;

import net.codegen.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long>
{	@Query(value = "SELECT updated_time FROM event WHERE category = ?1 AND city = ?2 ORDER BY updated_time DESC  LIMIT 1;", nativeQuery = true)
	String getLastUpdatedTime( String category, String city );
}
