package com.springboot.app.service;

import java.util.List;
import java.util.Optional;

import com.springboot.app.models.entity.Cliente;

public interface IClienteService {
	List<Cliente> findAll();
	void save(Cliente cliente);
	Optional<Cliente> findOne(Long id);
	void delete(Long id);
}
