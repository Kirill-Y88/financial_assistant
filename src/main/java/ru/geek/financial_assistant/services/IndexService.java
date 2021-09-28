package ru.geek.financial_assistant.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geek.financial_assistant.models.IndexDTO;
import ru.geek.financial_assistant.repositories.IndexRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexService {

    private IndexRepository indexRepository;

    @Autowired
    public void setIndexRepository(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    public List<IndexDTO> findAll (){
        return (indexRepository.findAll().stream().map(IndexDTO::new)).collect(Collectors.toList());
    }



}
