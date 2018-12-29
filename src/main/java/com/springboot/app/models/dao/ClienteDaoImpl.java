package com.springboot.app.models.dao;

import java.util.List;

import com.springboot.app.models.entity.Cliente;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository("clienteDaoJPA")
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente").getResultList();
	}
	
	@Override
	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return em.find(Cliente.class, id);
	}

	@Override
	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		
		if(cliente.getId() != null && cliente.getId() >0) {
			em.merge(cliente); 	//entoces editar
		} else {
			em.persist(cliente);	// nuevo cliente
		}
	}

	@Override
	public void delete(Long id) {
		em.remove(findOne(id));
	}

}
