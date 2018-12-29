package com.springboot.app.service;

import java.util.List;

import com.springboot.app.models.entity.Cliente;

public interface IClienteService {
	List<Cliente> findAll();
	void save(Cliente cliente);
	Cliente findOne(Long id);
	void delete(Long id);
}
