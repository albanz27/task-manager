import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentResponseDTO } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = '/api/tasks';

  constructor(private http: HttpClient) {}

  getComments(taskId: number): Observable<CommentResponseDTO[]> {
    return this.http.get<CommentResponseDTO[]>(`${this.apiUrl}/${taskId}/comments`);
  }

  addComment(taskId: number, authorUsername: string, content: string): Observable<CommentResponseDTO> {
    const body = {
      authorUsername: authorUsername,
      content: content
    };
    return this.http.post<CommentResponseDTO>(`${this.apiUrl}/${taskId}/comments`, body);
  }

  deleteComment(taskId: number, commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${taskId}/comments/${commentId}`);
  }
}