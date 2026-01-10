import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { TaskService } from '../../services/task';
import { AuthService } from '../../services/auth';


@Component({
  selector: 'app-create-task',
  templateUrl: './create-task.html',
  styleUrls: ['./create-task.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule]
})
export class CreateTaskComponent {
  taskForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private authService: AuthService,
    private router: Router
  ) {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required]],
      description: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.taskForm.valid) {
      const currentUser = this.authService.getLoggedUser();
      
      if (!currentUser || !currentUser.username) {
        alert("Error: session expired. Please log in again.");
        this.router.navigate(['/login']);
        return;
      }

      const taskData = {
        title: this.taskForm.value.title,
        description: this.taskForm.value.description
      };

      this.taskService.createTask(taskData, currentUser.username).subscribe({
        next: (res) => {
          this.router.navigate(['/tasks/backlog']);
        },
        error: (err) => {
          console.error('Error details:', err);
        }
      });
    }
  }

}