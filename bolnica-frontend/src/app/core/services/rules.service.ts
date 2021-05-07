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
    return this.http.get(`${environment.api_url}rules/default`, { responseType: 'json' });
  }

  getSeverityRules(size: number, page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}rules/str`, { responseType: 'json' });
  }

  getMessagesRules(size: number, page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}rules/mtr`, { responseType: 'json' });
  }

  addSeverityRule(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/create-str`, rule, { headers: this.headers, responseType: 'text' });
  }

  addMessagesRule(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/create-mtr`, rule, { headers: this.headers, responseType: 'text' });
  }

  addTemperatureRuleHigher(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/create-hight`, rule, { headers: this.headers, responseType: 'text' });
  }

  addTemperatureRuleLower(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/create-lowt`, rule, { headers: this.headers, responseType: 'text' });
  }

  addLowOxygenLevelRule(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/create-olr`, rule, { headers: this.headers, responseType: 'text' });
  }

  addPressureRule(rule: any): Observable<string> {
    return this.http.post(`${environment.api_url}rules/create-prule`, rule, { headers: this.headers, responseType: 'text' });
  }

}
