import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from '../../services/task';
import { AuthService } from '../../services/auth';
import { TaskResponseDTO } from '../../models/task.model';
import { ActivatedRoute } from '@angular/router';
import { RouterModule } from '@angular/router';
import { CommentService } from '../../services/comment';
import { CommentResponseDTO } from '../../models/comment.model';

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
    private commentService: CommentService,
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

  isAuthor(commentUsername: string): boolean {
    return commentUsername === this.authService.getLoggedUser()?.username;
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

  onDeleteTask(taskId: number): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(taskId).subscribe({
        next: () => {
          this.tasks = this.tasks.filter(t => t.id !== taskId);
          this.cdr.detectChanges();
        }
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

  visibleComments: { [taskId: number]: boolean } = {};

  toggleComments(task: TaskResponseDTO): void {
    this.visibleComments[task.id] = !this.visibleComments[task.id];

    if (this.visibleComments[task.id] && (!task.comments || task.comments.length === 0)) {
      this.commentService.getComments(task.id).subscribe({
        next: (comments) => {
          task.comments = comments;
          this.cdr.detectChanges();
        }
      });
    }
  }

  onAddComment(task: TaskResponseDTO): void {
    const content = prompt(`entere a comment for: ${task.title}`);
    const currentUser = this.authService.getLoggedUser();

    if (content && currentUser) {
      this.commentService.addComment(task.id, currentUser.username, content).subscribe({
        next: (newComment) => {
          if (!task.comments) task.comments = [];
          task.comments.unshift(newComment);
          this.cdr.detectChanges();
        },
        error: (err) => alert("Error saving comment")
      });
    }
  }

  onDeleteComment(task: TaskResponseDTO, comment: CommentResponseDTO): void {
    const currentUser = this.authService.getLoggedUser();

    if (currentUser && comment.authorUsername === currentUser.username) {
      if (confirm('Are you sure to delete this comment?')) {
        this.commentService.deleteComment(task.id, comment.id).subscribe({
          next: () => {
            task.comments = task.comments.filter((c: any) => c.id !== comment.id);
            this.cdr.markForCheck();
            this.cdr.detectChanges();
          },
          error: (err) => alert("Error during deletion")
        });
      }
    } 
  }

}