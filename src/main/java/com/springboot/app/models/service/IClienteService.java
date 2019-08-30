package com.springboot.app.models.service;


import java.util.List;
import java.util.Optional;

import com.springboot.app.models.entity.Factura;
import com.springboot.app.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.models.entity.Cliente;

public interface IClienteService {
	public List<Cliente> findAll();

	public Page<Cliente> findAll(Pageable pageable);

	public void save(Cliente cliente);

	public Cliente findById(Long id);

	public void delete(Long id);

	public List<Producto> findByNombre(String term);

	public void saveFactura(Factura factura);

	public Producto findProductoById(Long id);

	public Factura findFacturaById(Long id);
}
