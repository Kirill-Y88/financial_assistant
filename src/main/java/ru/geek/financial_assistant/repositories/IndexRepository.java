package ru.geek.financial_assistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geek.financial_assistant.models.Index;

@Repository
public interface IndexRepository extends JpaRepository<Index,Long> {

}
