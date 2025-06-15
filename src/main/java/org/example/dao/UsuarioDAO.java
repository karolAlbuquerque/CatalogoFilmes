package org.example.dao;

import org.example.model.Usuario;

public class UsuarioDAO extends GenericDAO<Usuario> {
    public UsuarioDAO() {
        super(Usuario.class);
    }
} 