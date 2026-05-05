package org.example.demo.controller;

import org.example.demo.entity.Fruit;
import org.example.demo.repository.FruitRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitRepository fruitRepository;

    public FruitController(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @PostMapping
    public Fruit addFruit(@RequestBody Fruit fruit) {
        return fruitRepository.save(fruit);
    }

    @GetMapping
    public List<Fruit> getAllFruits() {
        return fruitRepository.findAll();
    }

    @GetMapping("/{id}")
    public Fruit getFruit(@PathVariable Long id) {
        return fruitRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Fruit updateFruit(@PathVariable Long id, @RequestBody Fruit fruit) {
        Fruit existing = fruitRepository.findById(id).orElse(null);
        if(existing == null) return null;

        existing.setName(fruit.getName());
        existing.setQuantity(fruit.getQuantity());

        return fruitRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteFruit(@PathVariable Long id) {
        fruitRepository.deleteById(id);
        return "Deleted fruit id = " + id;
    }

    @GetMapping("/count")
    public Long countFruits() {
        return fruitRepository.count();
    }
}
