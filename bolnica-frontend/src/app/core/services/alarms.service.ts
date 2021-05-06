import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AlarmsPage } from '../model/response/alarms-page.model';

@Injectable({
  providedIn: 'root'
})
export class AlarmsService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getAlarms(size: number, page: number): Observable<AlarmsPage> {
    let pars = new HttpParams();
    pars = pars.append('size', size.toString());
    pars = pars.append('page', page.toString());
    return this.http.get(`${environment.api_url}alarms/by-page`, { params: pars, responseType: 'json' });
  }
}
