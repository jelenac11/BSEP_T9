import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AlarmsDoctorComponent } from './alarms-doctor/alarms-doctor.component';
import { AlarmsComponent } from './alarms/alarms.component';
import { CallbackComponent } from './callback/callback.component';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { CsrComponent } from './csr/csr.component';
import { LogsComponent } from './logs/logs.component';
import { MessagesComponent } from './messages/messages.component';
import { ReportComponent } from './report/report.component';
import { RulesDoctorComponent } from './rules-doctor/rules-doctor.component';
import { RulesComponent } from './rules/rules.component';

const routes: Routes = [
  {
    path: 'homepage',
    component: CallbackComponent,
    canActivate: [NoAuthGuard],
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
    path: 'rules',
    component: RulesComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:rules'
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
    path: 'messages',
    component: MessagesComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:messages'
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
    path: 'rules-doctor',
    component: RulesDoctorComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:rulesDoctor'
    }
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
