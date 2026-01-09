import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { UserResponseDTO } from '../models/user.model';

@Injectable({providedIn: 'root'})
export class AuthService {
  private apiUrl = '/api/users';
  private currentUserSubject = new BehaviorSubject<UserResponseDTO | null>(this.getLoggedUser());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<UserResponseDTO> {
    return this.http.post<UserResponseDTO>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap(user => {
          sessionStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        })
      );
  }

  register(userData: UserResponseDTO): Observable<UserResponseDTO> {
    return this.http.post<UserResponseDTO>(`${this.apiUrl}`, userData);
  }

  getLoggedUser(): UserResponseDTO | null {
    const user = localStorage.getItem('currentUser');
    console.log('Retrieved user from localStorage:', user);
    return user ? JSON.parse(user) : null;
  }

  logout() {
    sessionStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}