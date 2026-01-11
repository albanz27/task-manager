import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login'
import { RegisterComponent } from './components/register/register';
import { TaskListComponent } from './components/task-list/task-list';
import { CreateTaskComponent } from './components/create-task/create-task';
import { AssignmentComponent } from './components/assign/assign';
import { authGuard } from './auth-guard';

export const routes: Routes = [
    { path: '', redirectTo:'login', pathMatch:'full'},
    { path: 'login',  component: LoginComponent},
    { path: 'register', component: RegisterComponent},
    { path: 'tasks/new', component: CreateTaskComponent, canActivate: [authGuard]},
    { path: 'tasks/:id/assign', component: AssignmentComponent, canActivate: [authGuard]},
    { path: 'tasks',  children: [
        { path: 'in-progress', component: TaskListComponent, canActivate: [authGuard]},
        { path: 'backlog', component: TaskListComponent, canActivate: [authGuard]},
        { path: 'completed', component: TaskListComponent, canActivate: [authGuard]}
    ]}
    
];