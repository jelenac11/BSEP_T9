import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CertificatesComponent } from './certificates/certificates.component';
import { LogsComponent } from './logs/logs.component';
import { UsersComponent } from './users/users.component';

const routes: Routes = [
  {
    path: 'certificates',
    component: CertificatesComponent
  },
  {
    path: 'users',
    component: UsersComponent
  },
  {
    path: 'logs',
    component: LogsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
