package com.coding.challenge.appdirect.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.coding.challenge.appdirect.bean.Account;
import com.coding.challenge.appdirect.bean.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.account= :account ")
	List<User> findByAccount(@Param("account") Account accountUuid);
	
	@Query("SELECT u FROM User u WHERE u.openId= :openId ")
	User findByOpenID(@Param("openId") String openId);

}
