package com.vcube.stockmarket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vcube.stockmarket.entity.Transaction;
import com.vcube.stockmarket.entity.User;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	 List<Transaction> findByUserOrderByTransactionDateDesc(User user);

}

