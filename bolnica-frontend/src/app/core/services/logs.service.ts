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

  getLogs(size: number, page: number): Observable<LogsPage> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}logs/by-page`, { params, responseType: 'json' });
  }
}
