package ru.geek.financial_assistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geek.financial_assistant.models.IndexDTO;
import ru.geek.financial_assistant.services.IndexService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/indices")
public class IndexController {


    private IndexService indexService;

    @Autowired
    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping
    public List<IndexDTO> getAll (){
        return indexService.findAll();
    }


}
