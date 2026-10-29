package com.vcube.stockmarket.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "portfolio", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "stock_id" }) })
@Data
public class Portfolio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long portfolioId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;

	private Integer quantity;
	private BigDecimal averagePrice;
	
	@Column(precision = 19, scale = 4) // optional, good for money values
	private BigDecimal totalValue;


	// getters & setters
}
