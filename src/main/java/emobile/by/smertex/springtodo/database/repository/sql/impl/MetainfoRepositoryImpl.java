package emobile.by.smertex.springtodo.database.repository.sql.impl;

import emobile.by.smertex.springtodo.database.entity.sql.realisation.Metainfo;
import emobile.by.smertex.springtodo.database.repository.sql.MetainfoRepository;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MetainfoRepositoryImpl implements MetainfoRepository {

    private SessionFactory sessionFactory;

    @Override
    public Metainfo save(Metainfo metainfo) {
        try {
            sessionFactory.getCurrentSession().persist(metainfo);
            return metainfo;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Metainfo update(Metainfo metainfo) {
        try {
            return sessionFactory.getCurrentSession().merge(metainfo);
        } catch (Exception e) {
            return null;
        }
    }

    @Autowired
    void setSessionFactory(EntityManagerFactory emf){
        sessionFactory = emf.unwrap(SessionFactory.class);
    }
}
