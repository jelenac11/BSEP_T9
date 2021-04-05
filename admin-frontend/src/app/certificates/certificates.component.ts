import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { AddCaComponent } from '../add-ca/add-ca.component';
import { ApproveCsrComponent } from '../approve-csr/approve-csr.component';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { CertificatesPage } from '../core/model/response/certificates-page.model';
import { CSRPage } from '../core/model/response/csr-page.model';
import { RevokedCertificatesPage } from '../core/model/response/revoked-certificates-page.model';
import { CertificatesService } from '../core/services/certificates.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.scss']
})
export class CertificatesComponent implements OnInit {
  clicked = 'a';
  tabs: string[] = ['CSR', 'Certificates', 'Revoked certificates'];
  certificates: any;
  revokedCertificates: RevokedCertificatesPage = { content: [], totalElements: 0 };
  csrs: CSRPage;
  page = 1;
  size = 10;
  currentTab = 0;
  loggedIn = '';

  constructor(
    private certificatesService: CertificatesService,
    private dialog: MatDialog,
    private snackBar: Snackbar
  ) { }

  ngOnInit(): void {
    //this.certificates = { content: [], totalElements: 0 };
    this.csrs = { content: [], totalElements: 0 };
    this.getCertificates();
  }

  getCertificates(): void {
    if (this.currentTab === 0) {
      this.certificatesService.getCSRs(this.size, this.page - 1).subscribe((data: CSRPage) => {
        this.csrs = { content: data.content, totalElements: data.totalElements };
      });
    } else if (this.currentTab === 1) {
      /*this.certificatesService.getCertificates(this.size, this.page - 1).subscribe((data: CertificatesPage) => {
        this.certificates = { content: data.content, totalElements: data.totalElements };
      });*/
      this.certificatesService.getCertificates().subscribe((data: any) => {
        this.certificates = data;
      });
    } else {
      this.certificatesService.getRevokedCertificates(this.size, this.page - 1).subscribe((data: CertificatesPage) => {
        this.revokedCertificates = { content: data.content, totalElements: data.totalElements };
      });
    }
  }

  changeTab($event: MatTabChangeEvent): void {
    this.currentTab = $event.index;
    this.page = 1;
    this.getCertificates();
  }

  handlePageChange($event: number): void {
    this.page = $event;
    this.getCertificates();
  }

  openDialogReject(id: number): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        message: 'Are you sure you want to reject this CSR?',
        buttonText: {
          ok: 'Yes',
          cancel: 'No'
        }
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.reject(id);
      }
    });
  }

  openDialogRevoke(id: number): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        message: 'Are you sure you want to revoke this certificate?',
        buttonText: {
          ok: 'Yes',
          cancel: 'No'
        }
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.revoke(id);
      }
    });
  }

  reject(id: number): void {
    this.certificatesService.reject(id).subscribe((succ: string) => {
      this.getCertificates();
      this.snackBar.success('You have successfully rejected CSR!');
    }, err => {
      this.snackBar.error(err.error);
    });
  }

  revoke(id: number): void {
    this.certificatesService.revoke(id).subscribe((succ: string) => {
      this.getCertificates();
      this.snackBar.success('You have successfully revoked certificate.');
    }, err => {
      this.snackBar.error(err.error);
    });
  }

  approve(id: number): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    dialogConfig.data = id;
    const dialogRef = this.dialog.open(ApproveCsrComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.getCertificates();
      }
    });
  }

  addCA(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.minHeight = '440px';
    dialogConfig.minWidth = '400px';
    const dialogRef = this.dialog.open(AddCaComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.getCertificates();
      }
    });
  }

}
