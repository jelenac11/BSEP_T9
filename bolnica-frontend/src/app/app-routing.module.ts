import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CallbackComponent } from './callback/callback.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { CsrComponent } from './csr/csr.component';

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
    path: 'certificates',
    component: CertificatesComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:certificates'
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
