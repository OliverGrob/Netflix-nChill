package com.codecool.netflixandchill.dao.implementation;

import com.codecool.netflixandchill.dao.SeriesDao;
import com.codecool.netflixandchill.model.Series;
import com.codecool.netflixandchill.util.TransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class SeriesDaoDB implements SeriesDao {

    private TransactionManager transactionManager;
    private EntityManagerFactory emfManager;


    public SeriesDaoDB(TransactionManager transactionManager, EntityManagerFactory emfManager) {
        this.transactionManager = transactionManager;
        this.emfManager = emfManager;
    }


    @Override
    public void add(Series series) {
        EntityManager em = emfManager.createEntityManager();
        transactionManager.addToTable(em, series);
        em.close();
    }

    @Override
    public Series find(long seriesId) {
        EntityManager em = emfManager.createEntityManager();
        Series series = em.find(Series.class, seriesId);
        em.close();
        return series;
    }

    @Override
    public List<Series> getAll() {
        EntityManager em = emfManager.createEntityManager();

        List<Series> result = em.createQuery(
                "SELECT s " +
                        "FROM Series s", Series.class)
                .getResultList();
        result.stream().forEach(series -> series.getSeasons());
//        result.stream().forEach(series -> series.getGenres());
        em.close();
        return result;
    }

    @Override
    public List<Series> findBySubstring(String substring) {
        EntityManager em = emfManager.createEntityManager();
        List<Series> result = em.createQuery(
                "SELECT s " +
                        "FROM Series s WHERE UPPER(s.title) LIKE UPPER('%' || :param || '%')", Series.class)
                .setParameter("param", substring)
                .getResultList();
        em.close();
        return result;
    }
}
