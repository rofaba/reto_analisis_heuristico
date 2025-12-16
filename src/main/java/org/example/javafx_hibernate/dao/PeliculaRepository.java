package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.config.HibernateUtil;
import org.example.javafx_hibernate.entity.Pelicula;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PeliculaRepository implements PeliculaDao {

    /*
     * Implementación del DAO para la entidad Pelicula.
     */

    @Override
    public void guardar(Pelicula p) {
        Transaction tx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Long count = session.createQuery(
                            "SELECT COUNT(p) FROM Pelicula p WHERE LOWER(p.titulo) = LOWER(:titulo)",
                            Long.class
                    ).setParameter("titulo", p.getTitulo())
                    .uniqueResult();

            if (count != null && count > 0) {
                tx.rollback();
                throw new SaveException("Ya existe una película con el título: " + p.getTitulo());
            }

            session.save(p);
            tx.commit();

        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e; // importante: re-lanzar
        }
    }


    /*
     * Lista todas las películas en la base de datos.
     */
    @Override
    public List<Pelicula> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pelicula", Pelicula.class).list();
        }
    }
    public class SaveException extends RuntimeException {
        public SaveException(String message) {
            super(message);
        }
    }
    public int deleteById(Long id) {
        Transaction tx = null;
        Integer deletedCount = 0;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            deletedCount = session.createQuery("DELETE FROM Pelicula p WHERE p.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e; // importante: re-lanzar
        }

        return deletedCount;
    }
    public int eliminarPelicula(Long id) {
        int result = deleteById(id);
        if (result == 0) {
            throw new RuntimeException("No se encontró la película con ID: " + id);
        }
    return result;}
}