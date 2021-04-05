import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CertificatesService } from '../core/services/certificates.service';

@Component({
  selector: 'app-verify-csr',
  templateUrl: './verify-csr.component.html',
  styleUrls: ['./verify-csr.component.scss']
})
export class VerifyCsrComponent implements OnInit {
  token: string;

  constructor(
    private certificatesService: CertificatesService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.paramMap.get('token');
    this.certificatesService.verifyCSR(this.token).subscribe();
  }

}
