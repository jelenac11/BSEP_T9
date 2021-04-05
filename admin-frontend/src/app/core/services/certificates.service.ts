import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CSRPage } from '../model/response/csr-page.model';
import { CSR } from '../model/response/csr.model';

@Injectable({
  providedIn: 'root'
})
export class CertificatesService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

  constructor(
    private http: HttpClient
  ) { }

  getCSR(id: number): Observable<CSR> {
    return this.http.get(`${environment.api_url}csr/${id}`, { responseType: 'json' });
  }

  getCAs(): Observable<any> {
    return this.http.get(`${environment.api_url}certificates/ca`, { responseType: 'json' });
  }

  getCSRs(size: number, page: number): Observable<CSRPage> {
    let params = new HttpParams();
    params = params.append('size', size.toString());
    params = params.append('page', page.toString());
    return this.http.get(`${environment.api_url}csr/by-page`, { params, responseType: 'json' });
  }

  getCertificates(): Observable<any> {
    return this.http.get(`${environment.api_url}certificates`, { responseType: 'json' });
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

  approve(certificate: any): Observable<string> {
    return this.http.post(`${environment.api_url}csr/approve`, certificate, { headers: this.headers, responseType: 'text' });
  }

}
