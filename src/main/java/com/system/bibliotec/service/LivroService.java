package com.system.bibliotec.service;

import java.util.Optional;

import org.hibernate.validator.internal.constraintvalidators.hv.ISBNValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.system.bibliotec.config.ConstantsUtils;
import com.system.bibliotec.exception.CodBarrasExistenteException;
import com.system.bibliotec.exception.EstoqueInsuficienteException;
import com.system.bibliotec.exception.IsbnInvalidoException;
import com.system.bibliotec.exception.LivroAvariadoException;
import com.system.bibliotec.exception.LivroExistenteException;
import com.system.bibliotec.exception.LivroInvalidoOuInexistenteException;
import com.system.bibliotec.exception.LivroLocadoException;
import com.system.bibliotec.exception.LivroReservadoException;
import com.system.bibliotec.model.Livro;
import com.system.bibliotec.model.enums.StatusLivro;
import com.system.bibliotec.repository.LivroRepository;
import com.system.bibliotec.service.ultis.RandomUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LivroService {

	private final LivroRepository livroRepository;

	private ISBNValidator isbnValidator;

	/**
	 * Metodo para Registrar um novo Livro.
	 * 
	 * @param livro {@link Livro} Objeto com as novas Informações a ser persistida e
	 *              analisada {@link #validaLivroNovo()}
	 */
	@Transactional
	public Livro save(Livro livro) {
		
		validaLivroNovo(livro);
		livro.setStatusLivro(ConstantsUtils.DEFAULT_VALUE_STATUSLIVRO);
		livro.setCodBarras(RandomUtils.randomCodBarras()); // TODO: Modificar essa implementação em um ambiente de
															// produção...

		return livroRepository.save(livro);

	}

	/**
	 * Metodo para Atualizar um Livro.
	 *
	 * @param id {@link Long} id do Livro Existente para ser buscada e analisada
	 *           pelo metodo {@link #validaLivroExistente()}
	 */
	@Transactional
	public Livro updateLivro(Long id, Livro livro) {

		Optional<Livro> livroSalvo = findByIdLivro(id);
		validaLivroExistente(livroSalvo.get());

		BeanUtils.copyProperties(livro, livroSalvo.get(), "id");

		return livroRepository.save(livroSalvo.get());
	}

	@Transactional
	public void updateStatusLivro(Long id, StatusLivro statusLivro) {

		Optional<Livro> livroSalvo = findByIdLivro(id);

		if (!livroSalvo.isPresent()) {

			throw new LivroInvalidoOuInexistenteException("Operação não realizada.  Livro selecionado Inexistente");

		}
		livroSalvo.get().setStatusLivro(statusLivro);

		livroRepository.save(livroSalvo.get());
	}

	@Transactional
	public void updatePropertyIsbn13Livro(Long id, String isbn13) {
		if(!isbnValidator.isValid(isbn13, null)) {throw new IsbnInvalidoException("ISBN 13 Invalido. Informe outro valido");}
		Optional<Livro> livroSalvo = findByIdLivro(id);
		if (!livroSalvo.isPresent()) {throw new LivroInvalidoOuInexistenteException("Operação não realizada.  Livro selecionado Inexistente");}

		validaLivroExistente(livroSalvo.get());
		
		livroSalvo.get().setIsbn13(isbn13);
	}
	
	@Transactional
	public void baixarEstoque(Integer quantidade, Long idLivro) {
		Optional<Livro> livroSalvo = findByIdLivro(idLivro);
		
		int novaQuantidade = livroSalvo.get().getQuantidade() - quantidade;

		if (novaQuantidade < 0) {
			throw new EstoqueInsuficienteException();
		}
		livroSalvo.get().setQuantidade(novaQuantidade);
		livroRepository.save(livroSalvo.get());
	}

	@Transactional
	public void adicionarEstoque(Integer quantidade,Long idLivro) {
		Optional<Livro> livroSalvo = findByIdLivro(idLivro);
		
		livroSalvo.get().setQuantidade(livroSalvo.get().getQuantidade() + quantidade);
	}

	@Transactional
	public void deleteLivro(Long id) {
		Optional<Livro> livroSalvo = findByIdLivro(id);
		validaLivroExistente(livroSalvo.get());
		livroRepository.deleteById(id);

	}

	public Iterable<Livro> findAll() {

		return livroRepository.findAll();
	}

	public Optional<Livro> findByIdLivro(Long id) {
		Optional<Livro> livroSalvo = livroRepository.findById(id);
		if (livroSalvo.get() == null) {
			throw new LivroInvalidoOuInexistenteException("Livro Invalido ou Inexistente. Informe um Livro Valido");
		}
		return livroSalvo;
	}

	public static boolean isReservado(Livro livro) {

		boolean isReservado_ = false;

		if (livro.getStatusLivro() == StatusLivro.RESERVADO) {

			isReservado_ = true;

		}

		return isReservado_;

	}

	/**
	 * Metodo para avaliar um Livro existente.
	 * 
	 * @param id {@link Long} do Livro a ser analisado
	 * @throws LivroInvalidoOuInexistenteException Para um {@link Livro} Invalido ou
	 *                                             Inexistente
	 * @throws LivroReservadoException             Para um {@link Livro} Reservado
	 * @throws LivroLocadoException                Para um {@link Livro} Locado
	 * @throws LivroAvariadoException              Para um {@link Livro} Avariado ou
	 *                                             com marcas
	 */

	public void validaLivroExistente(Livro livro) {

		if (livro.getId() == null) {
			throw new LivroInvalidoOuInexistenteException(
					"Livro selecionado invalido ou Inexistente. Selecione um Livro Valido");
		}

		if (livro.getStatusLivro() == StatusLivro.RESERVADO) {

			throw new LivroReservadoException("O Livro Selecionado estar Reservado. Operação não Realizada");
		}

		if (livro.getStatusLivro() == StatusLivro.LOCADO) {

			throw new LivroLocadoException("O livro selecionado estar Locado. Operação não Realizada");
		}

		if (livro.getStatusLivro() == StatusLivro.AVARIADO) {

			throw new LivroAvariadoException("O livro selecionado estar Avariado. Operação não Realizada");
		}
		if(livro.getQuantidade() == 0) {
			throw new EstoqueInsuficienteException();
		}

	}

	/**
	 * Metodo para avaliar a criação de um Livro
	 * 
	 * @param livro {@link Livro} Objeto a ser analisado
	 * @throws LivroExistenteException Para um {@link Livro} Existente/Registrado no
	 *                                 Banco de dados
	 * @throws IsbnInvalidoException   Para um {@link Livro} com o Registro geral de
	 *                                 ISBN Invalido
	 */
	public void validaLivroNovo(Livro livro) {

		if (existsByIdLivro(livro.getId())) {

			throw new LivroExistenteException("Este livro já possui cadastro no sistema. Informe outro livro.");
		}
		if (existsCodBarrasLivro(livro.getCodBarras())) {

			throw new CodBarrasExistenteException("Codigo de barras existente! Gere outro codigo de barras!");
		}

		if (!isbnValidator.isValid(livro.getIsbn13(), null)) {

			throw new IsbnInvalidoException("ISBN Invalido. Informe um Registro Valido");
		}

	}

	public boolean existsByIdLivro(Long id) {

		return livroRepository.existsById(id);
	}

	public boolean existsCodBarrasLivro(String codBarras) {

		return livroRepository.findOneByCodBarrasIgnoreCase(codBarras).isPresent();
	}


}
