import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';

import { NgxPaginationModule } from 'ngx-pagination';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CsrComponent } from './csr/csr.component';
import { HeaderComponent } from './header/header.component';
import { CommonModule } from '@angular/common';
import { ButtonsModule, NavbarModule } from 'angular-bootstrap-md';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from './core/core.module';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Snackbar } from './snackbar';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpTokenInterceptor } from './core/interceptors/http.token.interceptor';
import { CertificatesComponent } from './certificates/certificates.component';
import { CallbackComponent } from './callback/callback.component';
import { LogsComponent } from './logs/logs.component';
import { MatSelectModule } from '@angular/material/select';
import { AlarmsComponent } from './alarms/alarms.component';
import { RulesComponent } from './rules/rules.component';
import { AddRuleComponent } from './add-rule/add-rule.component';
import { MatTabsModule } from '@angular/material/tabs';

@NgModule({
  declarations: [
    AppComponent,
    CsrComponent,
    HeaderComponent,
    CertificatesComponent,
    CallbackComponent,
    LogsComponent,
    AlarmsComponent,
    RulesComponent,
    AddRuleComponent
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
    ButtonsModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatAutocompleteModule,
    AppRoutingModule,
    CoreModule,
    NgxPaginationModule,
    MatDatepickerModule, 
    MatFormFieldModule, 
    MatNativeDateModule,
    MatSelectModule,
    MatTabsModule
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
