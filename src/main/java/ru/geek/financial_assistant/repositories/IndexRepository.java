package ru.geek.financial_assistant.repositories;

import org.hibernate.cfg.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geek.financial_assistant.models.Company;
import ru.geek.financial_assistant.models.Index;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<Index,Long> {

   /* @Query("SELECT max(i.id) FROM Index i where i.company = :company")
    Index getLastIndex(@Param("company") Company company);*/


    Index findFirstByCompany(Company company);

    default Index findLastIndexByCompany(Company company) throws NoResultException {
        String query = String.format("SELECT * FROM indices where id_company = %d ORDER BY date DESC LIMIT 1", company.getId());
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Index index = (Index) em.createNativeQuery(query,Index.class).getSingleResult();
        em.getTransaction().commit();
        return index;
    }

    default Index findFirstIndexByCompany(Company company) throws NoResultException{
        String query = String.format("SELECT * FROM indices where id_company = %d ORDER BY date ASC LIMIT 1", company.getId());
        EntityManagerFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        Index index = (Index) em.createNativeQuery(query,Index.class).getSingleResult();
        em.getTransaction().commit();
        return index;
    }

    @Query("SELECT i FROM Index i where i.company = :company ORDER BY i.date ASC")
    List<Index> getAllIndicesByCompany(@Param("company") Company company);

/*    @Query("SELECT i FROM Index i where i.company = :company AND i.date BETWEEN dateStart AND dateFinish")
    List<Index> findIndicesByCompanyAndDate(@Param("company") Company company,
                                            @Param("dateStart") LocalDate dateStart,
                                            @Param("dateFinish") LocalDate dateFinish);*/


  default List<Index> findIndicesByCompanyAndDate(Company company, LocalDate dateStart, LocalDate dateFinish){

      String query = String.format("SELECT * FROM indices where id_company = %d AND date between %s AND %s ORDER BY date ASC",
              company.getId(),dateStart.toString().replace("-",""), dateFinish.toString().replace("-",""));

      EntityManagerFactory factory = new Configuration()
              .configure("hibernate.cfg.xml")
              .buildSessionFactory();
      EntityManager em = factory.createEntityManager();
      em.getTransaction().begin();
      List<Index> indices = em.createNativeQuery(query,Index.class).getResultList();
      em.getTransaction().commit();
      return indices;
  }



}
