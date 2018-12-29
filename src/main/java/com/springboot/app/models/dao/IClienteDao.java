package com.springboot.app.models.dao;

import java.util.List;

import com.springboot.app.models.entity.Cliente;

public interface IClienteDao {
	
	void save(Cliente c);
	List<Cliente> findAll();

}
