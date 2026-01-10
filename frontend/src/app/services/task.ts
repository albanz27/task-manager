import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskResponseDTO } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = '/api/tasks';

  constructor(private http: HttpClient) { }

  getTasks(): Observable<TaskResponseDTO[]> {
    return this.http.get<TaskResponseDTO[]>(this.apiUrl);
  }

  getTasksByUser(username: string): Observable<TaskResponseDTO[]> {
    return this.http.get<TaskResponseDTO[]>(`${this.apiUrl}/user/${username}`);
  }

  updateStatus(id: number, status: string): Observable<TaskResponseDTO> {
    return this.http.patch<TaskResponseDTO>(`${this.apiUrl}/${id}/status?newStatus=${status}`, {});
  }

  addHours(id: number, username: string, hour: number): Observable<TaskResponseDTO> {
    return this.http.post<TaskResponseDTO>(`${this.apiUrl}/${id}/add-hours?username=${username}&hours=${hour}`, {});  
  }

  createTask(taskData: any, username: string): Observable<TaskResponseDTO> {
    return this.http.post<TaskResponseDTO>(`${this.apiUrl}?createdBy=${username}`, taskData);
  }
  
}