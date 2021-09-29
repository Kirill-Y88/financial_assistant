package ru.geek.financial_assistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/getIndices")
    public void getIndices (){
        indexService.getIndices();
    }

    @GetMapping("/create")
    public void create (){
            indexService.create();
    }

    @PostMapping("/getIndicesForCompany")
    public void getIndicesForCompany(@RequestParam String name,
                                     @RequestParam String dateStart,
                                     @RequestParam String dateFinish){
        System.out.println("name = " + name + " dstart = " + dateStart + " dfinish = " + dateFinish);
        indexService.getIndicesForCompany(name,dateStart,dateFinish);
    }

    @GetMapping("/getIndicesForCompany/{name}/{dateStart}/{dateFinish}")
    public void getIndicesForCompany2(@PathVariable String name,
                                      @PathVariable String dateStart,
                                      @PathVariable String dateFinish){
        indexService.getIndicesForCompany(name,dateStart,dateFinish);
    }


}
