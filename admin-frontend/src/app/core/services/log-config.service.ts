import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LogConfigService {
  constructor(
    private http: HttpClient
  ) { }

  createLogConfig(logConfig: any): Observable<string> {
    return this.http.post(`${environment.api_url}configuration`, logConfig, { responseType: 'text' });
  }

  getHospitals(): Observable<any> {
    return this.http.get(`${environment.api_url}configuration/hospitals`, { responseType: 'json' });
  }

}
