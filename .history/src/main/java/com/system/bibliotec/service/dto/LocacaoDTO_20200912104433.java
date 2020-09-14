package com.system.bibliotec.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class LocacaoDTO {

	
	@NotNull(message = "Campo livro necessario para realizar o despacho da  locação")
	private Long idLivro;

	@NotNull(message = "Codigo do Cliente necessario para registrar em sua tabela de historico")
	private Long idClienteSolicitante;	

	
	private Long idSolicitacao;	

	@Size(max = 255)
	private String observacoesEntregaLivro;
	

}
