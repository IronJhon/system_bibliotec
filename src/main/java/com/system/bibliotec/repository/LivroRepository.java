package com.system.bibliotec.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.bibliotec.model.Livro;


@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{

	public Optional<Livro> findByIsbn13(String cpf);

	public Page<Livro> findByNomeContaining(String nome, Pageable pageable);

	public Optional<Livro> findById(Long id);

	public void deleteById(Long id);

	public boolean existsById(Long id);

}
