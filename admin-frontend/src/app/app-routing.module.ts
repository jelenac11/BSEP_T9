import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CallbackComponent } from './callback/callback.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { VerifyCsrComponent } from './verify-csr/verify-csr.component';

const routes: Routes = [
  {
    path: 'homepage',
    component: CallbackComponent,
    canActivate: [NoAuthGuard],
  },
  {
    path: 'verify-csr/:token',
    component: VerifyCsrComponent,
  },
  {
    path: 'certificates',
    component: CertificatesComponent,
    canActivate: [RoleGuard],
    data: {
        expectedRoles: 'read:certificates'
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
