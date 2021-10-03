package ru.geek.financial_assistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.geek.financial_assistant.models.Company;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

       Company findCompanyByName(String name);


}
