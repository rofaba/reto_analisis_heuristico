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
    public void guardar(Pelicula pelicula) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT count(p) FROM Pelicula p WHERE p.titulo = :titulo", Long.class)
                    .setParameter("titulo", pelicula.getTitulo())
                    .uniqueResult();

            if (count != null && count > 0) {
                System.out.println("La película con título \"" + pelicula.getTitulo() + "\" ya existe. No se guardará.");
                return;
            }

            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(pelicula);
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            }
        }
    }
```
/*
     * Lista todas las películas en la base de datos.
     */
    @Override
    public List<Pelicula> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Pelicula", Pelicula.class).list();
        }
    }
}