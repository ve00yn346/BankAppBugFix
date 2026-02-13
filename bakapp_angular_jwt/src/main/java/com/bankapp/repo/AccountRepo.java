package com.bankapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bankapp.entities.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer>{

	@Modifying
	@Query(value = "UPDATE account_table_demo SET id = id - 1 WHERE id > :deletedId", nativeQuery = true)
	void compactIdsAfterDelete(@Param("deletedId") int deletedId);

	@Modifying
	@Query(value = "ALTER TABLE account_table_demo AUTO_INCREMENT = 1", nativeQuery = true)
	void resetAutoIncrement();
}
