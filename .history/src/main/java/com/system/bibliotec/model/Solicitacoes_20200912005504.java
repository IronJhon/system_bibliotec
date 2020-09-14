package com.system.bibliotec.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.system.bibliotec.model.enums.Status;
import com.system.bibliotec.model.enums.TipoSolicitacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Solicitacoes
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitacoes")
public class Solicitacoes extends AbstractAuditingEntity {

    
	@NotNull
	@Column(name = "idCliente")
	protected Long idCliente;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoSolicitacao tipo;

    @Column(name = "dataSolicitacao")
    private Instant dataSolicitacao = Instant.now();

    @Column(name = "dataRetiradaExemplar")
    private LocalDate dataRetiradaExemplar;

    @Column(name = "horaRetiradaExemplar")
    private LocalTime horaRetiradaExemplar;

    @NotNull
    @Column(name = "idExemplar")
    private Long idExemplar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "rejeitado")
    private boolean rejeitado = false;

    @NotNull(message = "Solicitação Precisa ter um usuario vinculado")
	@OneToOne	
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;
}