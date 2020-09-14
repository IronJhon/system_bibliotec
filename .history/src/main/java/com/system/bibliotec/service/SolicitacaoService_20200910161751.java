package com.system.bibliotec.service;



import com.system.bibliotec.model.Solicitacoes;
import com.system.bibliotec.repository.SolicitacaoRepository;

import org.springframework.stereotype.Service;

@Service
public class SolicitacaoService {
    

    private final SolicitacaoRepository repository;

    public SolicitacaoService(SolicitacaoRepository repository) {
        this.repository = repository;
    }



    public Solicitacoes findByIdSolicitacao(Long id){

        return repository.findById(id).orElseThrow(exceptionSupplier)
    }

}