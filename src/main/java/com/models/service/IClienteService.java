package com.models.service;


import java.util.List;

import com.models.entity.Cliente;
import com.models.entity.Producto;
import com.models.entity.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClienteService {
	List<Cliente> findAll();

	Page<Cliente> findAll(Pageable pageable);

	void save(Cliente cliente);

	Cliente findById(Long id);

	Cliente fetchByIdWithFactura(Long id);

	void delete(Long id);

	List<Producto> findByNombre(String term);

	void saveFactura(Factura factura);

	Producto findProductoById(Long id);

	Factura findFacturaById(Long id);

	void deleteFactura(Long id);

	Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);
}
