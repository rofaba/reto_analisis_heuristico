package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.config.HibernateUtil;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CopiaRepository implements CopiaDao {

    @Override
    public List<Copia> listarPorUsuario(Usuario usuario) throws Exception {
        Transaction tx = null;
        List<Copia> resultado;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Query<Copia> query = session.createQuery(
                    "FROM Copia c " +
                            "JOIN FETCH c.pelicula " +     // cargamos también la película
                            "WHERE c.usuario = :usuario",
                    Copia.class
            );
            query.setParameter("usuario", usuario);

            resultado = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }

        return resultado;
    }
    public void eliminarCopia(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Copia copia = session.get(Copia.class, id);
            if (copia != null) {
                session.delete(copia);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
