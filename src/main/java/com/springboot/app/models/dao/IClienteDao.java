package com.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.app.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
	//PagingAndSortingRepository
}
