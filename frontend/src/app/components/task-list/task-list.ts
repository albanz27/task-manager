import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from '../../services/task';
import { AuthService } from '../../services/auth';
import { TaskResponseDTO } from '../../models/task.model';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task-list.html'
})
export class TaskListComponent implements OnInit {
  tasks: TaskResponseDTO[] = [];

  constructor(
    private taskService: TaskService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getLoggedUser();
    if (currentUser) {
      this.loadTasks(currentUser.username);
    }
  }

  loadTasks(username: string): void {
    this.taskService.getTasksByUser(username).subscribe({
      next: (data) => {
        this.tasks = data.filter(t => t.status === 'IN_PROGRESS');
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching tasks', err);
      }
    });
  }
}