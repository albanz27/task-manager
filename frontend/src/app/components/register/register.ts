import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';


@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class RegisterComponent {
  username = '';
  mail = '';
  name = '';
  surname = '';
  password = '';
  errorMessage = '';

  constructor(private auth: AuthService, private router: Router, private cdr: ChangeDetectorRef ) {}

  onRegister() {
    this.errorMessage = '';

    if (!this.username.trim() || !this.mail.trim() || !this.password.trim() || !this.name.trim() || !this.surname.trim()) {
      this.errorMessage = "Please, fill in all fields.";
      return;
    }

    const newUser = {
      username: this.username,
      mail: this.mail,
      password: this.password,
      name: this.name,
      surname: this.surname
    };

    this.auth.register(newUser).subscribe({
      next: (res) => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        if (typeof err.error === 'string') {
          this.errorMessage = err.error; 
        } else {
          this.errorMessage = "Registration failed. Please try again later.";
        }

        this.resetFields();
        this.cdr.detectChanges();
      }
    });
  }

  private resetFields() {
    this.username = '';
    this.mail = '';
    this.password = '';
    this.name = '';
    this.surname = '';
  }
}