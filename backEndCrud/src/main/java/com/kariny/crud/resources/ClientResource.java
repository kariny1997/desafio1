package com.kariny.crud.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kariny.crud.dto.ClientDTO;
import com.kariny.crud.services.ClientService;

@RestController
public class ClientResource { 
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping("/buscar-client")
	public ResponseEntity<List<ClientDTO>> getAll(){		
		List<ClientDTO> dto = clientService.Buscar();
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping("/buscar-client/{id}")
	public ResponseEntity<ClientDTO> getFindById(@PathVariable long id){		
		ClientDTO dto = clientService.BuscarPorId(id);
		return ResponseEntity.ok().body(dto);
	}
	
	
	@PostMapping("/inserir-client")
	public ResponseEntity<ClientDTO> criarCategoria(@RequestBody ClientDTO dto){
		dto = clientService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping("/atualizar-client/{id}")
	public ResponseEntity<ClientDTO> criarCategoria(@PathVariable long id, @RequestBody ClientDTO dto){
		dto = clientService.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping("/deletar-client/{id}")
	public ResponseEntity<Void> deletar (@PathVariable long id){
		clientService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	//======= Get com Paginacao==============================
	
	@GetMapping("/buscar-paginada")
	public ResponseEntity<Page<ClientDTO>> BuscarPaginada(
							@RequestParam(value = "page", defaultValue = "0") Integer page,
							@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
							@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
							@RequestParam(value = "direction", defaultValue = "ASC") String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction) ,orderBy);
		
		 Page<ClientDTO> list = clientService.BuscarPorPagina(pageRequest);
		 
		return ResponseEntity.ok().body(list);
		 		
	}		


}
