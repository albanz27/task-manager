import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from '../../services/task';
import { AuthService } from '../../services/auth';
import { TaskResponseDTO } from '../../models/task.model';
import { ActivatedRoute } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './task-list.html',
  styleUrls: ['./task-list.css']
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

  onChangeState(task: TaskResponseDTO): void {
    let nextStatus = '';

    if (task.status === 'BACKLOG') {
      nextStatus = 'IN_PROGRESS';
    } else if (task.status === 'IN_PROGRESS') {
      nextStatus = 'COMPLETED';
    } else if (task.status === 'COMPLETED') {
      nextStatus = 'IN_PROGRESS'; 
    }

    if (nextStatus) {
      this.taskService.updateStatus(task.id, nextStatus).subscribe({
        next: () => {
          this.tasks = this.tasks.filter(t => t.id !== task.id);
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Error updating state:', err)
      });
    }
  }

  onAddHours(task: TaskResponseDTO, hoursValue: string): void {
    const hours = parseFloat(hoursValue);

    if (isNaN(hours) || hours == 0) {
      alert("Please enter a positive number of hours.");
      return;
    }
    if (hoursValue.includes('.')) {
        const decimals = hoursValue.split('.')[1];
        if (decimals.length > 2) {
          alert("Please enter a maximum of two decimal places (e.g., 1.25).");
          return;
        }
      }

    const username = this.authService.getLoggedUser()?.username;

    if (username && !isNaN(hours) && hours > 0) {
      this.taskService.addHours(task.id, username, hours).subscribe({
        next: (updatedTask) => {
          task.totalHours = updatedTask.totalHours;
          this.cdr.detectChanges();
        }
      });
    }
  }
}