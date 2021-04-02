import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { AddCaComponent } from '../add-ca/add-ca.component';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { CSRPage } from '../core/model/response/csr-page.model';
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
  //certificates: CertificatesPage;
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
      /*this.certificatesService.getCertificates(this.size, this.page - 1).subscribe((data: CertificatePage) => {
        this.certificates = { content: data.content, totalElements: data.totalElements };
      });*/
    } else {
      /*this.certificatesService.getRevokedCertificates(this.size, this.page - 1).subscribe((data: CertificatePage) => {
        this.certificates = { content: data.content, totalElements: data.totalElements };
      });*/
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

  reject(id: number): void {
    this.certificatesService.reject(id).subscribe((succ: string) => {
      this.getCertificates();
      this.snackBar.success('You have successfully rejected CSR!');
    }, err => {
      this.snackBar.error(err.error);
    });
  }

  approve(id: number): void {
    this.certificatesService.approve(id).subscribe((succ: string) => {
      this.getCertificates();
      this.snackBar.success('You have successfully approved CSR!');
    }, err => {
      this.snackBar.error(err.error);
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
