package com.rafaelguzman.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rafaelguzman.cursomc.domain.Categoria;
import com.rafaelguzman.cursomc.dto.CategoriaDTO;
import com.rafaelguzman.cursomc.repositories.CategoriaRepository;
import com.rafaelguzman.cursomc.resources.CategoriaResource;
import com.rafaelguzman.cursomc.services.exceptions.DataIntegrityException;
import com.rafaelguzman.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		obj.setId(null);// Garantindo que SEJA um novo objeto.
		return repo.save(obj);
	}

	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(obj);
	}

	public void delete(Integer id) {
		find(id);

		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma Categoria que possui Produtos.");
		}
	}

	public List<Categoria> findAll() {
		return repo.findAll();
	}

	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	/**
	 * Mét. Aux. Convertendo um objeto CategoriaDTO para Categoria
	 * 
	 * @param 1 objeto DTO do Tipo categoria
	 * @return 1 objeto do Tipo Categoria 
	 * Aplicação:
	 * @see CategoriaResource.java
	 */
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
	
	/**
	 * Mét. Aux. Atualiza um objeto Categoria a partir de um Obj recebido como
	 * argumento.
	 * 
	 * @param 2 objetos do Tipo Categoria
	 */
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}

}
