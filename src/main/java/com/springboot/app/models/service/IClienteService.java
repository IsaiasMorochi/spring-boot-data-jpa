package com.springboot.app.models.service;


import java.util.List;
import java.util.Optional;

import com.springboot.app.models.entity.Factura;
import com.springboot.app.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.models.entity.Cliente;

public interface IClienteService {
	List<Cliente> findAll();

	Page<Cliente> findAll(Pageable pageable);

	void save(Cliente cliente);

	Cliente findById(Long id);

	void delete(Long id);

	List<Producto> findByNombre(String term);

	void saveFactura(Factura factura);

	Producto findProductoById(Long id);

	Factura findFacturaById(Long id);

	void deleteFactura(Long id);
}
