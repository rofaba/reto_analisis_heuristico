package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.config.HibernateUtil;
import org.example.javafx_hibernate.entity.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UsuarioRepository implements UsuarioDao {
/*
     * Implementación del DAO para la entidad Usuario.
     */

    @Override
    public Usuario buscarPorNombreYPassword(String nombreUsuario, String password) throws Exception {
        Transaction tx = null;
        Usuario resultado = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Query<Usuario> query = session.createQuery(
                    "FROM Usuario u WHERE u.nombreUsuario = :nombre AND u.contrasena = :pass",
                    Usuario.class
            );
            query.setParameter("nombre", nombreUsuario);
            query.setParameter("pass", password);

            resultado = query.uniqueResult();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return resultado;
    }
}
