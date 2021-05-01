import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PatientsService {

  constructor(
    private http: HttpClient
  ) { }

  getPatients(): Observable<any> {
    return this.http.get(`${environment.api_url}patients`, { responseType: 'json' });
  }
}
