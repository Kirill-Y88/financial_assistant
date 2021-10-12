package ru.geek.financial_assistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.geek.financial_assistant.models.Index;
import ru.geek.financial_assistant.models.Indicator;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {

     Indicator saveAndFlush (Indicator indicator);

     @Query("SELECT i FROM Indicator i where i.index = :index ")
     Indicator findById_indices (Index index);


}
