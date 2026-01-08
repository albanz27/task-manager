import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router, private cdr: ChangeDetectorRef ) {}

  onLogin(): void {
    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = "Please insert username and password";
      return;
    }

    const loginData = { u: this.username, p: this.password };
    this.username = '';
    this.password = '';
    this.errorMessage = '';

    this.auth.login(loginData.u, loginData.p).subscribe({
      next: (user) => {
        this.router.navigate(['/tasks']); 
      },
      error: (err) => {
      this.errorMessage = "Invalid Username or Password .";
      this.username = ''; 
      this.password = '';
      this.cdr.detectChanges();
    }
    });
  }

}