import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AlarmsComponent } from './alarms/alarms.component';
import { CallbackComponent } from './callback/callback.component';
import { AlarmsDoctorComponent } from './alarms-doctor/alarms-doctor.component';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { CsrComponent } from './csr/csr.component';
import { LogsComponent } from './logs/logs.component';
import { ReportComponent } from './report/report.component';
import { MessagesComponent } from './messages/messages.component';
import { PatientsComponent } from './patients/patients.component';

const routes: Routes = [
  {
    path: 'homepage',
    component: CallbackComponent,
    canActivate: [NoAuthGuard],
  },
  {
    path: 'logs',
    component: LogsComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:logs'
    }
  },
  {
    path: 'alarms',
    component: AlarmsComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:alarms'
    }
  },
  {
    path: 'report',
    component: ReportComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:reports'
    }
  },
  {
    path: 'csr',
    component: CsrComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'write:csr'
    }
  },
  {
    path: 'messages',
    component: MessagesComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:messages'
    }
  },
  {
    path: 'patients',
    component: PatientsComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:patients'
    }
  },
  {
    path: 'alarms-doctor',
    component: AlarmsDoctorComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:alarmsDoctor'
    }
  },
  {
    path: '**',
    component: CallbackComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
