package com.applojamc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.applojamc.domain.Categoria;
import com.applojamc.dto.CategoriaDTO;
import com.applojamc.repositories.CategoriaRepository;
import com.applojamc.services.exceptions.DataIntegrityException;
import com.applojamc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = categoriaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria obj) {
		obj.setId(null); // << garante que vai ser um insert e não um update
		return categoriaRepository.save(obj);
	}

	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId());
		updateData(newObj, obj); 
		return categoriaRepository.save(newObj);
	}

	public void delete(Integer id) {
		find(id); 
		try {
			categoriaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma Categoria que possui produto(s)"); 
		}
	}

	public List<Categoria> findAll() {
		return categoriaRepository.findAll(); 
	}

	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy); 
		return categoriaRepository.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
}
