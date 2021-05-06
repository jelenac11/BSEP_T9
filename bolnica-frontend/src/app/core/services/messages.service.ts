import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getMessages(size: number, page: number, filter: any): Observable<any> {
    let pars = new HttpParams();
    pars = pars.append('size', size.toString());
    pars = pars.append('page', page.toString());
    return this.http.post(`${environment.api_url}messages/by-page`, filter, { headers: this.headers, params: pars, responseType: 'json' });
  }
}