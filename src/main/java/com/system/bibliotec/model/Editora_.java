package com.system.bibliotec.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Editora.class)
public abstract class Editora_ extends com.system.bibliotec.model.AbstractAuditingEntity_ {

	public static volatile ListAttribute<Editora, Livro> livros;
	public static volatile SingularAttribute<Editora, String> nome;
	public static volatile SingularAttribute<Editora, String> descricao;

	public static final String LIVROS = "livros";
	public static final String NOME = "nome";
	public static final String DESCRICAO = "descricao";

}

