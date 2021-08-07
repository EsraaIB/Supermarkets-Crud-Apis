package hatley.task.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


import hatley.task.entities.Supermarket;

public interface SupermarketRepository extends CrudRepository <Supermarket ,Long> {
	@Transactional
	@Modifying
	@Query("UPDATE Supermarket s SET s.isActivated=?1 WHERE s.id= ?2")
	void changeSupermarketActiveStatus(Boolean status ,Long id );
	

}
