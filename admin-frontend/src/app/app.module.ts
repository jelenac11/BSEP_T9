import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { UsersComponent } from './users/users.component';
import { LogsComponent } from './logs/logs.component';
import { CommonModule } from '@angular/common';
import { ButtonsModule, NavbarModule } from 'angular-bootstrap-md';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from './core/core.module';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Snackbar } from './snackbar';
import { AddCaComponent } from './add-ca/add-ca.component';
import { ConfirmationDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CertificatesComponent,
    UsersComponent,
    LogsComponent,
    AddCaComponent,
    ConfirmationDialogComponent
  ],
  imports: [
    CommonModule,
    NavbarModule,
    ReactiveFormsModule,
    RouterModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatTabsModule,
    ButtonsModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatAutocompleteModule,
    AppRoutingModule,
    CoreModule,
    NgxPaginationModule,
    MatDialogModule
  ],
  providers: [Snackbar],
  bootstrap: [AppComponent]
})
export class AppModule { }
