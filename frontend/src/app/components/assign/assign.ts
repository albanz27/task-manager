import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { UserService } from '../../services/user';
import { TaskService } from '../../services/task';
import { UserResponseDTO } from '../../models/user.model';
import { TaskResponseDTO } from '../../models/task.model';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core'; 
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-assign',
  standalone: true, 
  imports: [CommonModule, RouterModule],
  templateUrl: './assign.html',
  styleUrl: './assign.css',
})
export class AssignmentComponent implements OnInit {
  taskId!: number;
  task: TaskResponseDTO | null = null; 
  availableUsers: UserResponseDTO[] = []; 
  loggedInUsername: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private taskService: TaskService,
    private userService: UserService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef

  ) {}

  ngOnInit() {
    this.loggedInUsername = this.authService.getLoggedUser()?.username || null;

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.taskId = Number(id);
        this.refreshData();
      }
    });
  }

  refreshData() {    
    this.taskService.getTaskById(this.taskId).subscribe({
      next: (taskData) => {
          this.userService.getAllUsers().subscribe(users => {
          this.availableUsers = users;
          this.task = taskData;
          this.cdr.markForCheck();
        });
      },
      error: (err) => {
        console.error('Error:', err);
      }
    });
  }

  isAlreadyAssigned(username: string): boolean {
    if (!this.task) return true;
    const isMe = username === this.loggedInUsername;
    const isIncluded = this.task.assignments.some(a => a.username === username);
    
    return isMe || isIncluded;
  }

  assign(username: string) {
    this.taskService.assignTask(username, this.taskId).subscribe(() => this.refreshData());
  }

  remove(username: string) {
    if (username === this.loggedInUsername) return;
    this.taskService.deleteTaskAssignment(username, this.taskId).subscribe(() => this.refreshData());
  }
}