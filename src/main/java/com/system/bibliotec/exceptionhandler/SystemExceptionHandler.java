package com.system.bibliotec.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.system.bibliotec.exception.ClienteInexistenteException;
import com.system.bibliotec.exception.CpfInvalidoException;
import com.system.bibliotec.exception.CpfInvalidoOuInexistenteException;
import com.system.bibliotec.exception.DocumentoInvalidoException;
import com.system.bibliotec.exception.LivroInvalidoOuInexistenteException;
import com.system.bibliotec.exception.LivroLocadoException;
import com.system.bibliotec.exception.LivroReservadoException;
import com.system.bibliotec.exception.ReservaInexistenteException;
import com.system.bibliotec.exception.ReservaUpdateException;

import lombok.RequiredArgsConstructor;


@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemExceptionHandler extends ResponseEntityExceptionHandler {

	// NECESSITA-SE IMPLEMENTAR UM METODO GENERICO PARA TODAS EXCEÇÕES...
	
	
	private final MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Erro> erros = criarListaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	
	@ExceptionHandler({ CpfInvalidoException.class, ClienteInexistenteException.class })
	public ResponseEntity<Object> cpfInvalidoOuInexistenteException(RuntimeException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.cpf.invalido", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ DocumentoInvalidoException.class })
	public ResponseEntity<Object> documentoInvalidoException(DocumentoInvalidoException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.cliente.documento.operacao-nao-permitida", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	
	@ExceptionHandler({ LivroInvalidoOuInexistenteException.class })
	public ResponseEntity<Object> livroInvalidoOuInexistenteException(LivroInvalidoOuInexistenteException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.livro.nao-encontrado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
		
	
	@ExceptionHandler({ LivroLocadoException.class })
	public ResponseEntity<Object> livroAlugadoException(LivroLocadoException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.livro.locado", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ LivroReservadoException.class })
	public ResponseEntity<Object> livroReservadoException(LivroReservadoException ex, WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.livro.reservado", null,	LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	
	@ExceptionHandler({ ReservaUpdateException.class })
	public ResponseEntity<Object> reservaUpdateException(ReservaUpdateException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("regra.reserva.atualizar", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ ReservaInexistenteException.class })
	public ResponseEntity<Object> reservaInexistenteException(ReservaInexistenteException ex,
			WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("regra.reserva.inexistente", null,
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}	
		

	private List<Erro> criarListaDeErros(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<>();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldError.toString();
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}

		return erros;
	}

	public static class Erro {

		private String mensagemUsuario;
		private String mensagemDesenvolvedor;

		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

		public String getMensagemUsuario() {
			return mensagemUsuario;
		}

		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}

	}

}
