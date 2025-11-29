package org.example.javafx_hibernate.dao;

import org.example.javafx_hibernate.config.HibernateUtil;
import org.example.javafx_hibernate.entity.Copia;
import org.example.javafx_hibernate.entity.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CopiaRepository implements CopiaDao {

    /*
        * Lista todas las copias asociadas a un usuario específico, incluyendo los detalles de la película relacionada.
     */

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
    /*
        * Elimina una copia por su ID. Si la cantidad es mayor a 1, decrementa la cantidad en lugar de eliminarla.
     */
    @Override
    public void eliminarCopia(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Copia copia = session.get(Copia.class, id);
            if (copia != null) {
                //debe comprobar el numero de copias existentes para eliminar o decrementar
                if (copia.getCantidad() > 1) {
                    copia.setCantidad(copia.getCantidad() - 1);
                    session.update(copia);
                } else {
                    session.delete(copia);
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
