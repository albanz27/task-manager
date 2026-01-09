import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from '../../services/task';
import { AuthService } from '../../services/auth';
import { TaskResponseDTO } from '../../models/task.model';
import { ActivatedRoute } from '@angular/router';

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
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.url.subscribe(() => {
      const currentUser = this.authService.getLoggedUser();
      if (currentUser) {
        this.loadTasks(currentUser.username);
      }
    });
  }

  loadTasks(username: string): void {
    const path = this.route.snapshot.url[0].path;
    const targetStatus = path.toUpperCase().replace('-', '_');

    this.taskService.getTasksByUser(username).subscribe({
      next: (data) => {
        console.log(data);
        this.tasks = data.filter(t => t.status === targetStatus);
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error: ', err);
      }
    });
  }
}