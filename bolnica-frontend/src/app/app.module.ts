import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';

import {MatRadioModule} from '@angular/material/radio';
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
import { CallbackComponent } from './callback/callback.component';
import { LogsComponent } from './logs/logs.component';
import { MatSelectModule } from '@angular/material/select';
import { AlarmsComponent } from './alarms/alarms.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDialogModule } from '@angular/material/dialog';
import { AddSeverityRuleComponent } from './add-severity-rule/add-severity-rule.component';
import { AddMessagesRuleComponent } from './add-messages-rule/add-messages-rule.component';
import { ReportComponent } from './report/report.component';
import { AlarmsDoctorComponent } from './alarms-doctor/alarms-doctor.component';
import { MessagesComponent } from './messages/messages.component';
import { AddTemperatureRuleComponent } from './add-temperature-rule/add-temperature-rule.component';
import { AddLowOxygenLevelRuleComponent } from './add-low-oxygen-level-rule/add-low-oxygen-level-rule.component';
import { AddPressureRuleComponent } from './add-pressure-rule/add-pressure-rule.component';

@NgModule({
  declarations: [
    AppComponent,
    CsrComponent,
    HeaderComponent,
    CallbackComponent,
    LogsComponent,
    AlarmsComponent,
    AddSeverityRuleComponent,
    AddMessagesRuleComponent,
    ReportComponent,
    AlarmsDoctorComponent,
    MessagesComponent,
    AddTemperatureRuleComponent,
    AddLowOxygenLevelRuleComponent,
    AddPressureRuleComponent
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
    MatDialogModule,
    MatSelectModule,
    MatTabsModule,
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
