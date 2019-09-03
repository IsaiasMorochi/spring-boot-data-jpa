package com.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
    //PagingAndSortingRepository

    @Query("select c from Cliente c left join fetch c.facturas f where c.id = ?1")
    Cliente fetchByIdWithFactura(Long id);
}
