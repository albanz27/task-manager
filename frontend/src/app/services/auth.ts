import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';
import { UserResponseDTO } from '../models/user.model';

@Injectable({providedIn: 'root'})
export class AuthService {
  private apiUrl = '/api/users';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<UserResponseDTO> {
    return this.http.post<UserResponseDTO>(`${this.apiUrl}/login`, { username, password });
  }

  getLoggedUser(): UserResponseDTO | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }

  logout() {
    localStorage.removeItem('currentUser');
  }
}