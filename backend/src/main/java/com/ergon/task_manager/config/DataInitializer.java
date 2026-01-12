package com.ergon.task_manager.config;

import com.ergon.task_manager.model.*;
import com.ergon.task_manager.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final CommentRepository commentRepository;

    public DataInitializer(UserRepository userRepository, TaskRepository taskRepository,
            TaskAssignmentRepository taskAssignmentRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User alban = createUser("alban99", "alban@ergon.it", "Alban", "Haka");
        User giorgio = createUser("giorgio89", "giorgio@gmail.com", "Giorgio", "Rossi");
        User mirko = createUser("mirko77", "mirko@ergon.it", "Mirko", "Bianchi");
        User elena = createUser("elena_dev", "elena@gmail.com", "Elena", "Verdi");
        userRepository.saveAll(Arrays.asList(alban, giorgio, mirko, elena));

        Task t1 = createTask("Frontend Navigation", "Implement Sidebar and AuthGuards", TaskStatus.IN_PROGRESS);
        assign(t1, alban, 4.5);
        assign(t1, mirko, 2.0);
        addComment(t1, alban, "AuthGuard is working!", LocalDateTime.now().minusHours(2));
        addComment(t1, mirko, "Great, I will test the routes now.", LocalDateTime.now().minusHours(1));

        Task t2 = createTask("API Optimization", "Refactor TaskController for better performance", TaskStatus.BACKLOG);
        assign(t2, giorgio, 0.0);
        assign(t2, elena, 1.0);
        addComment(t2, elena, "Check the N+1 problem on comments.", LocalDateTime.now().minusDays(1));

        Task t3 = createTask("Database Setup", "Configure PostgreSQL and Liquibase", TaskStatus.COMPLETED);
        assign(t3, mirko, 8.0);
        assign(t3, alban, 2.0);
        addComment(t3, alban, "Migrations are successful.", LocalDateTime.now().minusDays(3));
        addComment(t3, mirko, "Closed the task.", LocalDateTime.now().minusDays(2));

        Task t4 = createTask("User Profile Page", "Display user stats and assigned tasks", TaskStatus.IN_PROGRESS);
        assign(t4, elena, 3.0);
        addComment(t4, elena, "Working on the layout.", LocalDateTime.now().minusMinutes(30));

        Task t5 = createTask("Unit Testing", "Write tests for TaskService", TaskStatus.BACKLOG);
        assign(t5, alban, 0.0);
        addComment(t5, alban, "Use Mockito for the repository layer.", LocalDateTime.now());

        Task t6 = createTask("Bug Fix: Login", "Fix session timeout issue on mobile", TaskStatus.COMPLETED);
        assign(t6, giorgio, 2.5);
        assign(t6, alban, 0);
        addComment(t6, alban, "Login is smooth now.", LocalDateTime.now().minusHours(5));

        Task t7 = createTask("Security Audit", "Review JWT implementation", TaskStatus.IN_PROGRESS);
        assign(t7, mirko, 1.5);
        assign(t7, elena, 2.0);
        addComment(t7, elena, "Found a small vulnerability in headers.", LocalDateTime.now().minusMinutes(10));

        Task t8 = createTask("Logo Design", "Create a new logo for Task Manager", TaskStatus.BACKLOG);
        assign(t8, alban, 0);
        assign(t8, elena, 0);
        addComment(t8, elena, "I can help with design ideas.", LocalDateTime.now().minusDays(2));

        Task t9 = createTask("Documentation", "Write Swagger documentation for APIs", TaskStatus.IN_PROGRESS);
        assign(t9, giorgio, 1.0);
        assign(t9, mirko, 0);
        addComment(t9, mirko, "Include the Comment endpoints please.", LocalDateTime.now().minusHours(4));

        Task t10 = createTask("Deployment", "Setup Docker Compose for production", TaskStatus.COMPLETED);
        assign(t10, alban, 6.0);
        addComment(t10, alban, "Container is running on port 8080.", LocalDateTime.now().minusDays(1));
    }

    private User createUser(String username, String email, String name, String surname) {
        User u = new User();
        u.setUsername(username);
        u.setMail(email);
        u.setPassword("password123");
        u.setName(name);
        u.setSurname(surname);
        return u;
    }

    private Task createTask(String title, String desc, TaskStatus status) {
        Task t = new Task();
        t.setTitle(title);
        t.setDescription(desc);
        t.setStatus(status);
        return taskRepository.save(t);
    }

    private void assign(Task t, User u, double hours) {
        TaskAssignmentId id = new TaskAssignmentId(t.getId(), u.getUsername());
        TaskAssignment a = new TaskAssignment();
        a.setId(id);
        a.setTask(t);
        a.setUser(u);
        a.setWorkedHours(hours);
        a.setAssignmentDate(LocalDateTime.now().minusDays(1));
        taskAssignmentRepository.save(a);
    }

    private void addComment(Task t, User u, String content, LocalDateTime date) {
        Comment c = new Comment();
        c.setTask(t);
        c.setUser(u);
        c.setContent(content);
        c.setCreatedAt(date);
        commentRepository.save(c);
    }
}