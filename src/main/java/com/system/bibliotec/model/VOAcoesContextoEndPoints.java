package com.system.bibliotec.model;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "acoesEndPoint")
public class VOAcoesContextoEndPoints extends AbstractAuditingEntity {


    @Column(name = "recursoSolicitado")
    private String recursoSolicitado;

    @ElementCollection
    @MapKeyColumn(name = "header")
    @Column(name = "value")
    @CollectionTable(name = "acoesEndPoint_data_headers", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> dataHeaders = new HashMap<>();

    @ElementCollection
    @MapKeyColumn(name = "param")
    @Column(name = "value")
    @CollectionTable(name = "acoesEndPoint_data_parans", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> dataParans = new HashMap<>();

    @Column(name = "body")
    private String body;

    @Column(name = "method")
    private String method;

    @Column(name = "atividadeEsperada")
    private String atividadeEsperada;

    @Column(name = "ip")
    private String ip;

}
