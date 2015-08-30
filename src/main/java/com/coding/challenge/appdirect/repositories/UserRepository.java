package com.coding.challenge.appdirect.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.coding.challenge.appdirect.bean.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.account= :uuid ")
	List<User> findByAccount(@Param("uuid") String accountUuid);

}
