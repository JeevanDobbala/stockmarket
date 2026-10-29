package com.vcube.stockmarket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vcube.stockmarket.entity.Portfolio;
import com.vcube.stockmarket.entity.Stock;
import com.vcube.stockmarket.entity.User;
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUserAndStock(User user, Stock stock);
    List<Portfolio> findByUser(User user);
    

    @Query("SELECT p FROM Portfolio p WHERE p.user.userId = :userId AND p.stock.stockId = :stockId")
    Optional<Portfolio> findByUserIdAndStockId(@Param("userId") Long userId, @Param("stockId") Long stockId);

}

