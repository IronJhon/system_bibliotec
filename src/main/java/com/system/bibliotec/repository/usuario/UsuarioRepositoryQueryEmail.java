package com.system.bibliotec.repository.usuario;

import java.util.Optional;

import com.system.bibliotec.model.Usuario;

public interface UsuarioRepositoryQueryEmail {

    public Usuario findOneByEmail(String email);


}
