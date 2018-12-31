package com.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.springboot.app.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long> {

}
