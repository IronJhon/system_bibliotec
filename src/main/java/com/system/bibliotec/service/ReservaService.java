package com.system.bibliotec.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.bibliotec.exception.DocumentoInvalidoException;
import com.system.bibliotec.exception.LivroInvalidoOuInexistenteException;
import com.system.bibliotec.exception.ReservaCanceladaException;
import com.system.bibliotec.exception.ReservaInexistenteException;
import com.system.bibliotec.exception.ReservaLocadaException;
import com.system.bibliotec.exception.ReservaUpdateException;
import com.system.bibliotec.model.Cliente;
import com.system.bibliotec.model.Livro;
import com.system.bibliotec.model.Reserva;
import com.system.bibliotec.model.enums.StatusLivro;
import com.system.bibliotec.model.enums.StatusReserva;
import com.system.bibliotec.repository.ReservaRepository;
import com.system.bibliotec.service.ultis.CpfUtilsValidator;
import com.system.bibliotec.service.ultis.HoraDiasDataLocalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReservaService {

	
	private final ReservaRepository repository;

	
	private final ClienteService clienteService;

	
	private final LivroService livroService;

	// RESERVANDO UM LIVRO.....

	/**
	 * Metodo para criar uma Reserva caso prosseguir com todos os requisitos requeridos pelo {@link #validaReserva(Long)}
	 *
	 * @param reserva {@link Reserva} para ser  analisado e persistido
	 *
	 */	
	@Transactional
	public Reserva save(Reserva reserva) {

		livroService.validaLivroExistente(reserva.getIdLivro().getId());

		clienteService.validaClienteExistente(reserva.getIdCliente().getCpf());

		
		reserva.setHoraReserva(HoraDiasDataLocalService.horaLocal());

		reserva.setDataReserva(HoraDiasDataLocalService.dataLocal());

		reserva.setDataLimite(HoraDiasDataLocalService.dataReservaLimite());

		livroService.updateStatusLivro(reserva.getIdLivro().getId(), StatusLivro.RESERVADO);

		return repository.save(reserva);

	}

	public void existsByIdReserva(Reserva idReserva) {

		if (!repository.existsById(idReserva.getId())) {

			throw new ReservaInexistenteException("Reserva Inexistente");
		}

	}

	
	@Transactional
	public void updatePropertyLivro(Long idReserva, Livro livro) {

		validaReservaExistente(idReserva);

		livroService.validaLivroExistente(livro.getId());
		
		Optional<Reserva> reservaSalvo = findByIdReserva(idReserva);

		livroService.updateStatusLivro(reservaSalvo.get().getIdLivro().getId(), StatusLivro.LIVRE);


		livroService.updateStatusLivro(livro.getId(), StatusLivro.RESERVADO);

		reservaSalvo.get().setIdLivro(livro);

		repository.save(reservaSalvo.get());

	}

	@Transactional
	public void updatePropertyCliente(Long idReserva, Cliente cliente) {

		clienteService.validaClienteExistente(cliente.getCpf());
		
		Optional<Reserva> reservaSalva = repository.findById(idReserva);

		if (!reservaSalva.isPresent()) {
			throw new ReservaInexistenteException("Operação não realizada. Reserva Inexistente");
		}

		reservaSalva.get().setIdCliente(cliente);

		repository.save(reservaSalva.get());
	}

	public void deleteReserva(Long id) {
  		
		Optional<Reserva> reservaSalva = findByIdReserva(id);
	
		// ATUALIZANDO O STATUS DO LIVRO RESERVADO...
		livroService.updateStatusLivro(reservaSalva.get().getIdLivro().getId(), StatusLivro.LIVRE);
		repository.deleteById(id);

	}

	public Optional<Reserva> findByIdReserva(Long id) {
		Optional<Reserva> reservaSalvo = repository.findById(id);
		if (!reservaSalvo.isPresent()) {
			throw new ReservaInexistenteException("Reserva Selecionada Invalida ou Inexistente");
		}
		return reservaSalvo;
	}
	
	/**
	 * Metodo para validar uma Solicitação e Atualização de Reserva de Exemplar/Livro
	 *
	 * @param id LONG para ser  buscado e analisado
	 * @throws ReservaInexistenteException Se a Reserva selecionada não for encontrada para tal prodecimento
	 * @throws LivroInvalidoOuInexistenteException Se o Livro informado na Reserva não possuir cadastro valido na Base de dados
	 * @throws DocumentoInvalidoException Se Cliente Possuir Pendencias ou Documentação sem valía
	 * @throws ReservaCanceladaException Se a Reserva selecionada Possuir registro de cancelamento em seu status junto a Base de dados
	 * @throws ReservaLocadaException Se a Reserva selecionada Possuir registro de Locação em seu status junto a Base de dados
	 * @throws ReservaUpdateException Se a Reserva selecionada Possuir pendencias ou quaisquer outra exceção supracitada registrada, registrada junto a base de dados
	 */
	public void validaReservaExistente(Long id) {
		
		Optional<Reserva> reservaSalva = findByIdReserva(id);
		
		if (!reservaSalva.isPresent()) {
			throw new ReservaInexistenteException("Reserva Selecionada Invalida ou Inexistente");
		}

		if (reservaSalva.get().getIdLivro() == null || !livroService.existsByIdLivro(reservaSalva.get().getIdLivro().getId())) {
			throw new LivroInvalidoOuInexistenteException("Operação não Realizada. Livro Selecionado Invalido");
		}
		if (!CpfUtilsValidator.isCPF(reservaSalva.get().getIdCliente().getCpf())) {
			throw new DocumentoInvalidoException( "Operação não Realizada.  Documentação do Cliente Invalida");
		}
		if (reservaSalva.get().getStatusReserva() == StatusReserva.CANCELADA) {
			throw new ReservaCanceladaException( "Operação não Realizada.  Reserva Cancelada ou Encerrada");
		}
		if (reservaSalva.get().getStatusReserva() == StatusReserva.ALUGADA) {
			throw new ReservaLocadaException( "Operação não Realizada.  O Intem reservado já estar sob processo de Locação");
		}
		if (reservaSalva.get().getDataLimite().isBefore(LocalDate.now())) {
			throw new ReservaUpdateException("Operação não Realizada. Data limite de Reserva Ultrapassada");
		}
		
	}

}
