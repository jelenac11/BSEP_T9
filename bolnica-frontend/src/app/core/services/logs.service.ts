import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LogsPage } from '../model/response/logs-page.model';

@Injectable({
  providedIn: 'root'
})
export class LogsService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getLogs(size: number, page: number, search: any): Observable<LogsPage> {
    let pars = new HttpParams();
    pars = pars.append('size', size.toString());
    pars = pars.append('page', page.toString());
    return this.http.post(`${environment.api_url}logs/by-page`, search, { headers: this.headers, params: pars, responseType: 'json' });
  }
}
