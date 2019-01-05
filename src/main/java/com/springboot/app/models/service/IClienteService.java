package com.springboot.app.models.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.models.entity.Cliente;

public interface IClienteService {
	List<Cliente> findAll();
	Page<Cliente> findAll(Pageable pageable);
	void save(Cliente cliente);
	Optional<Cliente> findById(Long id);
	void delete(Long id);
}
