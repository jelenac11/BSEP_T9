import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { CertificatesService } from '../core/services/certificates.service';
import { Snackbar } from '../snackbar';
import * as moment from 'moment';
import { map, startWith } from 'rxjs/operators';
import { CSR } from '../core/model/response/csr.model';
import { DateAdapter } from '@angular/material/core';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';

@Component({
  selector: 'app-approve-csr',
  templateUrl: './approve-csr.component.html',
  styleUrls: ['./approve-csr.component.scss']
})
export class ApproveCsrComponent implements OnInit {
  formApprove: FormGroup;
  submitted = false;
  country_codes: string[] = [
    "CF", "TD", "CL", "CN", "CX", "CC", "CO", "KM", "CG", "CD", "CK", "CR", "CI", "CI", "HR", "CU", "CY", "CZ", "DK", "DJ", "DM", "DO", "EC", "EG", "SV", "GQ", "ER", "EE", "ET", "FK", "FO", "FJ", "FI", "FR", "GF", "PF", "TF", "GA", "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GD", "GP", "GU", "GT", "GG", "GN", "GW", "GY", "HT", "HM", "VA", "HN", "HK", "HU", "IS", "IN", "ID", "IR", "IQ", "IE", "IM", "IL", "IT", "JM", "JP", "JE", "JO", "KZ", "KE", "KI", "KP", "KR", "KR", "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LY", "LI", "LT", "LU", "MO", "MK", "MG", "MW", "MY", "MV", "ML", "MT", "MH", "MQ", "MR", "MU", "YT", "MX", "FM", "MD", "MC", "MN", "ME", "MS", "MA", "MZ", "MM", "MM", "NA", "NR", "NP", "NL", "AN", "NC", "NZ", "NI", "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW", "PS", "PA", "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR", "QA", 
    "RE", "RO", "RU", "RU", "RW", "SH", "KN", "LC", "PM", "VC", "VC", "VC", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL", "SG", "SK", "SI", "SB", "SO", "ZA", "GS", "SS", "ES", "LK", "SD", "SR", "SJ", "SZ", "SE", "CH", "SY", "TW", "TW", "TJ", "TZ", "TH", "TL", "TG", "TK", "TO", "TT", "TN", "TR", "TM", "TC", "TV", "UG", "UA", "AE", "GB", "US", "UM", "UY", "UZ", "VU", "VE", "VE", "VN", "VN", "VG", "VI", "WF", "EH", "YE", "ZM", "ZW"
  ]
  hide_subject_info = true;
  filteredOptions: Observable<string[]>;
  csr: CSR;
  maxDateEnd: Date;
  minDateStart: Date;
  minDateEnd: Date;
  maxDateStart: Date;
  pickerDisable = true;
  issuers = [];
  algorithms = ["sha256WithRSAEncryption", "sha384WithRSAEncryption", "sha512WithRSAEncryption"];
  types = ['SSL server', 'SSL client', 'Email certificate', 'Code signing certificate'];
  issuer = "";
  key_usages: string[] = ['digitalSignature', 'nonRepudiation', 'keyEncipherment', 'dataEncipherment', 'keyAgreement', 'encipherOnly', 'decipherOnly'];
  extended_key_usages: string[] = ['serverAuth', 'clientAuth', 'codeSigning', 'emailProtection', 'timeStamping'];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ApproveCsrComponent>,
    private snackBar: Snackbar,
    private certificatesService: CertificatesService,
    private dateAdapter: DateAdapter<any>,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    moment.locale('sr');
    this.dateAdapter.setLocale('sr');
    this.minDateStart = new Date();
    this.maxDateEnd = new Date(2026, 4, 6);
    this.maxDateStart = moment(this.maxDateEnd).subtract(1, 'days').toDate();

    this.formApprove = this.fb.group({
      common_name: ["", [Validators.required, Validators.pattern('[a-zA-Z0-9\. ]+')]],
      organization: ["", [Validators.required, Validators.pattern('[a-zA-Z0-9 ]+')]],
      organizational_unit: ["", [Validators.required, Validators.pattern('[a-zA-Z0-9 ]+')]],
      city_locality: ["", [Validators.required, Validators.pattern('[a-zA-Z ]+')]],
      state_county_region: ["",[Validators.required, Validators.pattern('[a-zA-Z ]+')]],
      country: ["", Validators.required],
      email_address: ["", [Validators.required, Validators.email]],
      not_before: ["", [Validators.required]],
      not_after: ["", [Validators.required]],
      issuer: ["", [Validators.required]],
      signing_algorithm: [""],
      type: ["", [Validators.required]],
      key_usage: [[]],
      extended_key_usage: [[]],
      key_identifier_extension: [false],
      alternative_name_extension: [false]
    });
    this.certificatesService.getCSR(this.data).subscribe(data => {
      this.csr = data;
      this.setValues();
      this.filter()
    })
    this.certificatesService.getCAs().subscribe(data => {
      this.issuers = data;
    })
  }

  private setValues(): void {
    this.formApprove.patchValue({
      common_name: this.csr.commonName,
      organization: this.csr.organization,
      organizational_unit: this.csr.organizationalUnit,
      city_locality: this.csr.cityLocality,
      state_county_region: this.csr.stateCountyRegion,
      country: this.csr.country,
      email_address: this.csr.emailAddress
    })
  }

  filter() {
    this.filteredOptions = this.formApprove.controls.country.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }
  get f(): { [key: string]: AbstractControl; } { return this.formApprove.controls; }

  approve(): void {
    this.submitted = true;
    if (this.formApprove.invalid) {
      return;
    }
    const certificate: any = {serialNumber: this.data, notBefore: '', notAfter: '', issuer: '', signingAlgorithm: '', type: '', keyUsage: [], extendedKeyUsage: [], keyIdentifierExtension: false, alternativeNameExtension: false };
    certificate.notBefore = this.formApprove.value.not_before;
    certificate.notAfter = this.formApprove.value.not_after;
    certificate.issuer = this.formApprove.value.issuer;
    certificate.signingAlgorithm = this.formApprove.value.signing_algorithm;
    certificate.type = this.formApprove.value.type;
    certificate.keyUsage = this.formApprove.value.key_usage;
    certificate.extendedKeyUsage = this.formApprove.value.extended_key_usage;
    certificate.keyIdentifierExtension = this.formApprove.value.key_identifier_extension;
    certificate.alternativeNameExtension = this.formApprove.value.alternative_name_extension;
    let plus2 = this.formApprove.value.not_before.clone();
    if (this.formApprove.value.not_after > plus2.add('years', 2)) {
      this.snackBar.error("Maximum validity time is two years.");
      return;
    }
    this.certificatesService.approve(certificate).subscribe((data: any) => {
      this.snackBar.success("You have succesfully approved CSR!");
      this.dialogRef.close(true);
    },
    error => {
      this.snackBar.error(error.error);
    });
  }

  public setMinEnd(event: MatDatepickerInputEvent<Date>) {
    this.pickerDisable = false;
    this.minDateEnd = moment(event.value).add(1, 'days').toDate();
  }

  close(): void {
    this.dialogRef.close(false);
  }

  toggleSubjectInfo(): void {
    this.hide_subject_info = !this.hide_subject_info;
  }

  private _filter(value: any): string[] {
    const filterValue = value.toLowerCase();

    return this.country_codes.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

  public issuerChanged($event) {
    this.formApprove.patchValue({
      issuer: $event,
    });
  }

  public key_identifier_check($event) {
    this.formApprove.patchValue({
      key_identifier_extension: $event.checked
    });
  }

  public alternative_name_check($event) {
    this.formApprove.patchValue({
      alternative_name_extension: $event.checked
    });
  }

  public typeChanged($event) {
    if ($event.value == 'SSL server') {
      this.formApprove.patchValue({
        key_usage: ['digitalSignature', 'keyEncipherment'],
        extended_key_usage: ['serverAuth']
      });
    } else if ($event.value == 'SSL client') {
      this.formApprove.patchValue({
        key_usage: ['digitalSignature'],
        extended_key_usage: ['clientAuth']
      });
    } else if ($event.value == 'Email certificate') {
      this.formApprove.patchValue({
        key_usage: [],
        extended_key_usage: ['emailProtection']
      });
    } else if ($event.value == 'Code signing certificate') {
      this.formApprove.patchValue({
        key_usage: ['digitalSignature'],
        extended_key_usage: ['codeSigning']
      });
    }
  }

}
