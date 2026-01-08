import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { TaskService } from './services/task';
import { TaskResponseDTO } from './models/task.model';
import { RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-root',
  standalone: true, 
  imports: [CommonModule, RouterOutlet], 
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent implements OnInit { 
  tasks: TaskResponseDTO[] = [];

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks() {
    this.taskService.getTasks().subscribe({
      next: (data) => this.tasks = data,
      error: (err) => console.error('error loading task', err)
    });
  }
}