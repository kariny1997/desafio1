package com.kariny.crud.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kariny.crud.dto.ClientDTO;
import com.kariny.crud.entities.Client;
import com.kariny.crud.repositories.ClientRepository;
import com.kariny.crud.services.exceptions.DataBaseException;
import com.kariny.crud.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	public List<ClientDTO> Buscar() {
		List<Client> list = clientRepository.findAll();
		return list.stream().map(x -> new ClientDTO(x)).collect(Collectors.toList());
	}

	public ClientDTO BuscarPorId(long id) {
		Optional<Client> obj = clientRepository.findById(id);
		Client entidade = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(entidade);
	}

	public ClientDTO update(long id, ClientDTO dto) {
		try {
			Client client = clientRepository.getReferenceById(id);

			client.setName(dto.getName());
			client.setCpf(dto.getCpf());
			client.setBirthDate(dto.getBirthDate());
			client.setChildren(dto.getChildren());
			client.setIncome(dto.getIncome());

			client = clientRepository.save(client);
			return new ClientDTO(client);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		}
	}

	public ClientDTO insert(ClientDTO dto) {
		Client client = new Client();

		client.setName(dto.getName());
		client.setCpf(dto.getCpf());
		client.setBirthDate(dto.getBirthDate());
		client.setChildren(dto.getChildren());
		client.setIncome(dto.getIncome());

		client = clientRepository.save(client);

		return new ClientDTO(client);

	}

	public void delete(long id) {
		try {
			clientRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found" + id + " verificar id valido");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}

	}

	public Page<ClientDTO> BuscarPorPagina(PageRequest pageRequest) {
		Page<Client> list = clientRepository.findAll(pageRequest);
		return list.map(x-> new ClientDTO(x));
	}

}
