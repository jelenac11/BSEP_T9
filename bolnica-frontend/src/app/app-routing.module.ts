import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CsrComponent } from './csr/csr.component';

const routes: Routes = [
  {
    path: 'csr',
    component: CsrComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
