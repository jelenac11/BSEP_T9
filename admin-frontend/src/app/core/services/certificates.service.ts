import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CSRPage } from '../model/response/csr-page.model';

@Injectable({
  providedIn: 'root'
})
export class CertificatesService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getCSRs(size: number, page: number): Observable<CSRPage> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}csr/by-page`, { params, responseType: 'json' });
  }

  getCertificates(size: number, page: number): Observable<CSRPage> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}certificates/by-page`, { params, responseType: 'json' });
  }

  getRevokedCertificates(size: number, page: number): Observable<CSRPage> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}revoked-certificates/by-page`, { params, responseType: 'json' });
  }

  reject(id: number): Observable<string> {
    return this.http.delete(`${environment.api_url}csr/${id}`, { responseType: 'text' });
  }

  approve(id: number): Observable<string> {
    return this.http.get(`${environment.api_url}csr/approve/${id}`, { responseType: 'text' });
  }

}
