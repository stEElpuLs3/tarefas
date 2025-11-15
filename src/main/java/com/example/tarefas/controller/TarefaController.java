package com.example.tarefas.controller;

import com.example.tarefas.model.Tarefa;
import com.example.tarefas.repository.TarefaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    // Criar uma nova tarefa
    @PostMapping
    public ResponseEntity<?> criarTarefa(@Valid @RequestBody Tarefa tarefa) {
        try {
            Tarefa novaTarefa = tarefaRepository.save(tarefa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar tarefa: " + e.getMessage());
        }
    }

    // Consultar todas as tarefas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodasTarefas() {
        List<Tarefa> tarefas = tarefaRepository.findAll();
        return ResponseEntity.ok(tarefas);
    }

    // Consultar uma tarefa específica pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarTarefaPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        
        if (tarefa.isPresent()) {
            return ResponseEntity.ok(tarefa.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada com ID: " + id);
        }
    }

    // Atualizar uma tarefa existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTarefa(@PathVariable Long id, 
                                           @Valid @RequestBody Tarefa tarefaAtualizada) {
        try {
            Optional<Tarefa> tarefaExistente = tarefaRepository.findById(id);
            
            if (tarefaExistente.isPresent()) {
                Tarefa tarefa = tarefaExistente.get();
                tarefa.setNome(tarefaAtualizada.getNome());
                tarefa.setDataEntrega(tarefaAtualizada.getDataEntrega());
                tarefa.setResponsavel(tarefaAtualizada.getResponsavel());
                
                Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                return ResponseEntity.ok(tarefaSalva);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tarefa não encontrada com ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    // Remover uma tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarTarefa(@PathVariable Long id) {
        try {
            if (tarefaRepository.existsById(id)) {
                tarefaRepository.deleteById(id);
                return ResponseEntity.ok("Tarefa deletada com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Tarefa não encontrada com ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar tarefa: " + e.getMessage());
        }
    }
}