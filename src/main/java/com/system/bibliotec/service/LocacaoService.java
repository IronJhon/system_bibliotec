package com.system.bibliotec.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.bibliotec.exception.ClienteInadimplenteException;
import com.system.bibliotec.exception.ClienteInexistenteException;
import com.system.bibliotec.exception.LivroInvalidoOuInexistenteException;
import com.system.bibliotec.exception.LivroReservadoException;
import com.system.bibliotec.exception.LocacaoInvalidaOuInexistenteException;
import com.system.bibliotec.exception.LocacaoLimiteDataException;
import com.system.bibliotec.exception.LocacaoLimiteException;
import com.system.bibliotec.exception.LocacaoUpdateException;
import com.system.bibliotec.model.Cliente;
import com.system.bibliotec.model.Livro;
import com.system.bibliotec.model.Locacao;
import com.system.bibliotec.model.enums.StatusCliente;
import com.system.bibliotec.model.enums.StatusLivro;
import com.system.bibliotec.model.enums.StatusLocacao;
import com.system.bibliotec.repository.LocacaoRepository;
import com.system.bibliotec.service.ultis.HoraDiasDataLocalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocacaoService {

	private final ClienteService clienteService;

	private final LivroService livroService;

	private LocacaoRepository locacaoRepository;

	/**
	 * Metodo para avaliar possibilidade de Locar um Livro
	 * 
	 * @param locacao {@link Locacao} Objeto com os respectivos dados para
	 *                homologação do requerimento de locação
	 *                {@link #validaLocacao()}
	 */
	@Transactional
	public Locacao realizarLocacao(Locacao locacao) {

		livroService.validaLivroExistente(locacao.getIdLivro().getId());

		locacao.setHoraLocacao(HoraDiasDataLocalService.horaLocal());

		locacao.setDataLocacao(HoraDiasDataLocalService.dataLocal());

		locacao.setDataTerminoLocacao(HoraDiasDataLocalService.dataLocacaoDevolucao());

		livroService.updateStatusLivro(locacao.getIdLivro().getId(), StatusLivro.LOCADO);

		return locacaoRepository.save(locacao);

	}

	@Transactional
	public void renovarLocacao(Long id) {

		validaLocacaoExistente(id);

		Optional<Locacao> locacaoSalva = findByIdLocacao(id);

		int quantidadeRenovada = locacaoSalva.get().getQuantidadeDeRenovacao();
		locacaoSalva.get().setQuantidadeDeRenovacao(quantidadeRenovada + 1);
		locacaoSalva.get().setDataTerminoLocacao(
				HoraDiasDataLocalService.dataRenovacaoLocacao(locacaoSalva.get().getDataTerminoLocacao()));

	}

	public Iterable<Locacao> findAll() {

		return locacaoRepository.findAll();
	}

	public boolean existsByIdLocacao(Long id) {

		return locacaoRepository.existsById(id);
	}

	@Transactional
	public void updatePropertyLivro(Long idLocacao, Long idLivro) {

		validaLocacaoExistente(idLocacao);
		
		livroService.validaLivroExistente(idLivro);

		Optional<Locacao> locacaoSalva = findByIdLocacao(idLocacao);
		Optional<Livro> livroSalvo = livroService.findByIdLivro(idLivro);

		livroService.updateStatusLivro(locacaoSalva.get().getIdLivro().getId(), StatusLivro.LIVRE);

		livroService.updateStatusLivro(livroSalvo.get().getId(), StatusLivro.LOCADO);

		locacaoSalva.get().setIdLivro(livroSalvo.get());

		locacaoRepository.save(locacaoSalva.get());
	}

	public void updatePropertyCliente(Long idLocacao, Long idCliente) {

		Optional<Cliente> clienteSalvo = clienteService.findByIdCliente(idCliente);

		Optional<Locacao> locacaoSalva = findByIdLocacao(idLocacao);

		clienteService.validandoClienteExistente(locacaoSalva.get().getIdCliente().getCpf());

		clienteService.validandoClienteExistente(clienteSalvo.get().getCpf());

		locacaoSalva.get().setIdCliente(clienteSalvo.get());

		locacaoRepository.save(locacaoSalva.get());
	}

//ADICIONAR @Scheduled para alterar o status do emprestimo caso o prazo de entrega seja atingido ou ultrapassado 
	public void checarDataLimiteAtingida(Long idEmprestimo, StatusLocacao statusLocacao) {

	}

	public void cancelarEmprestimo(Long id) {

		validaLocacaoExistente(id);

		Optional<Locacao> locacaoSalva = findByIdLocacao(id);

		livroService.updateStatusLivro(locacaoSalva.get().getIdLivro().getId(), StatusLivro.LIVRE);

		locacaoSalva.get().setHoraCancelamentoLocacao(HoraDiasDataLocalService.horaLocal());
		locacaoSalva.get().setDataCancelamentoLocacao(HoraDiasDataLocalService.dataLocal());
		locacaoSalva.get().setStatusLocacao(StatusLocacao.CANCELADA);

		locacaoRepository.save(locacaoSalva.get());
	}

	@Transactional
	public void updatePropertyStatusLocacao(Long id, StatusLocacao statusLocacao) {

		validaLocacaoExistente(id);

		Optional<Locacao> locacaoSalva = findByIdLocacao(id);

		locacaoSalva.get().setStatusLocacao(statusLocacao);

		locacaoRepository.save(locacaoSalva.get());

	}

	public Optional<Locacao> findByIdLocacao(Long id) {

		Optional<Locacao> locacaoSalva = locacaoRepository.findById(id);
		if (!locacaoSalva.isPresent()) {
			throw new LocacaoInvalidaOuInexistenteException(
					"Locação Invalida ou Inexistente. Informe uma Locação Valida");
		}
		return locacaoSalva;
	}

	public void validarLocacao(Locacao locacao) {
		Optional<Cliente> clienteSalvo = clienteService.findByIdCliente(locacao.getIdCliente().getId());

		Optional<Livro> livroSalvo = livroService.findByIdLivro(locacao.getIdLivro().getId());

		if (!livroSalvo.isPresent()) {

			throw new LivroInvalidoOuInexistenteException("Operação não realizada. Livro Invalido ou Inexistente");
		}

		if (!clienteSalvo.isPresent()) {

			throw new ClienteInexistenteException("Operação não realizada. Cliente Inexistente");
		}
		if (clienteSalvo.get().getStatusCliente() == StatusCliente.INADIMPLENTE) {

			throw new ClienteInadimplenteException("Operação não realizada devido Inadimplencia.");
		}
		if (clienteSalvo.get().getStatusCliente() == StatusCliente.INADIMPLENTE) {

			throw new ClienteInadimplenteException("Operação não realizada devido Inadimplencia.");
		}
		if (livroSalvo.get().getStatusLivro() == StatusLivro.RESERVADO) {

			throw new LivroReservadoException(
					"Operação não realizada. Livro selecionado estar Reservado. Selecione outro Exemplar");
		}
		if (livroSalvo.get().getStatusLivro() == StatusLivro.LOCADO) {

			throw new LivroReservadoException(
					"Operação não realizada. Livro selecionado estar Locado. Selecione outro Exemplar");
		}

	}

	public void validaLocacaoExistente(Long id) {

		Optional<Locacao> locacaoSalva = findByIdLocacao(id);

		if (locacaoSalva.get().getIdCliente().getStatusCliente() == StatusCliente.INADIMPLENTE) {

			throw new ClienteInadimplenteException("Operação não realizada. Cliente Inadinplente.");
		}
		if (locacaoSalva.get().getStatusLocacao() == StatusLocacao.CANCELADA) {

			throw new LocacaoUpdateException(
					"Operação não realizada. Locacao Cancelada ou Encerrada. Selecione outra Locação para Atualizar");
		}
		if (locacaoSalva.get().getQuantidadeDeRenovacao() == 3) {

			throw new LocacaoLimiteException(
					"Operação não realizada. Quantidade de Renovação Excedida. Selecione outro exemplar para prosseguir com tal Operação");
		}

		if (locacaoSalva.get().getDataTerminoLocacao().isBefore(LocalDate.now())) {

			throw new LocacaoLimiteDataException("Operação não realizada. Data de Termino da Locação ultrapassada.");
		}

	}

}
