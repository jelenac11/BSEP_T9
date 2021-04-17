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
import {MatPaginatorModule} from '@angular/material/paginator';
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
import { MatStepperModule } from '@angular/material/stepper';
import { MatTabsModule } from '@angular/material/tabs';
import { ApproveCsrComponent } from './approve-csr/approve-csr.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MatSelectModule } from '@angular/material/select';
import { SignInComponent } from './sign-in/sign-in.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpTokenInterceptor } from './core/interceptors/http.token.interceptor';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { VerifyCsrComponent } from './verify-csr/verify-csr.component';
import { RevokeComponent } from './revoke/revoke.component';
import {MatRadioModule} from '@angular/material/radio';
import { InformationDialogComponent } from './information-dialog/information-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CertificatesComponent,
    UsersComponent,
    LogsComponent,
    AddCaComponent,
    ConfirmationDialogComponent,
    ApproveCsrComponent,
    SignInComponent,
    VerifyCsrComponent,
    RevokeComponent,
    InformationDialogComponent
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
    MatStepperModule,
    MatSnackBarModule,
    MatTabsModule,
    ButtonsModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatAutocompleteModule,
    AppRoutingModule,
    CoreModule,
    NgxPaginationModule,
    MatDialogModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatSelectModule,
    MatPaginatorModule,
    MatCheckboxModule,
    MatRadioModule
  ],
  providers: [
    Snackbar,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
