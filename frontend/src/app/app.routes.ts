import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login'
import { RegisterComponent } from './components/register/register';
import { AppComponent } from './app';
import { TaskListComponent } from './components/task-list/task-list';
import { CreateTaskComponent } from './components/create-task/create-task';

export const routes: Routes = [
    { path: '', redirectTo:'login', pathMatch:'full'},
    { path: 'login',  component: LoginComponent},
    { path: 'register', component: RegisterComponent},
    { path: 'tasks/new', component: CreateTaskComponent},
    { path: 'tasks',  children: [
        { path: 'in-progress', component: TaskListComponent},
        { path: 'backlog', component: TaskListComponent},
        { path: 'completed', component: TaskListComponent}
    ]}
    
];