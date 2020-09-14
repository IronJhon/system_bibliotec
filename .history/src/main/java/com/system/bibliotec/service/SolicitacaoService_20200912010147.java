package com.system.bibliotec.service;



import java.time.LocalDate;
import java.time.LocalTime;

import com.system.bibliotec.exception.RecursoNaoEncontradoException;
import com.system.bibliotec.model.Solicitacoes;
import com.system.bibliotec.model.enums.Status;
import com.system.bibliotec.model.enums.TipoPessoa;
import com.system.bibliotec.model.enums.TipoSolicitacao;
import com.system.bibliotec.repository.SolicitacaoRepository;

import org.springframework.stereotype.Service;



@Service
public class SolicitacaoService {
    
  


    private final SolicitacaoRepository repository;

    public SolicitacaoService(SolicitacaoRepository repository) {
        this.repository = repository;
    }



    public Solicitacoes findByIdSolicitacao(Long id){

        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Operação Não realizada. Solicitação do Cliente não foi Localizada"));
    }

    public void gravarHistorico(Solicitacoes entity){

        repository.save(entity)
    }


    public void updateStatus(Status status, Long id){
    
     Solicitacoes contexto = findByIdSolicitacao(id);

     contexto.setStatus(status);

     repository.saveAndFlush(contexto); //flush
     

        
    }

    public void updateStatusAndDescricao(Status status, Long id, String descricao){
    
        Solicitacoes contexto = findByIdSolicitacao(id);
   
        contexto.setStatus(status);
        contexto.setDescricao(descricao);
        repository.saveAndFlush(contexto); //flush
        
   
           
       }


       public void updateStatusAndDescricao(Status status, Long id, String descricao, boolean isRejeitado){
    
        Solicitacoes contexto = findByIdSolicitacao(id);
   
        contexto.setStatus(status);
        contexto.setDescricao(descricao);
        contexto.setRejeitado(isRejeitado);
        repository.saveAndFlush(contexto); //flush
        
   
           
       }

}