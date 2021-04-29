import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RulesService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getDefaultRules(size: number, page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}rules/default/by-page`, { params, responseType: 'json' });
  }

  getSeverityRules(size: number, page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}rules/severity/by-page`, { params, responseType: 'json' });
  }

  getMessagesRules(size: number, page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}rules/messages/by-page`, { params, responseType: 'json' });
  }

  addSeverityRule(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/severity`, rule, { headers: this.headers, responseType: 'text' });
  }

  addMessagesRule(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/messages`, rule, { headers: this.headers, responseType: 'text' });
  }

}
