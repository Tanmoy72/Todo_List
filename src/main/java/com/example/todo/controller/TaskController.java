package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("task", new Task());
        return "add";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Task> taskOpt = taskService.findById(id);
        if (taskOpt.isEmpty()) return "redirect:/";
        model.addAttribute("task", taskOpt.get());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, @ModelAttribute Task task) {
        task.setId(id);
        taskService.save(task);
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTask(@PathVariable Long id) {
        taskService.findById(id).ifPresent(t -> {
            t.setCompleted(!t.isCompleted());
            taskService.save(t);
        });
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
        return "redirect:/";
    }
}
