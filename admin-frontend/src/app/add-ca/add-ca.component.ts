import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DateAdapter } from '@angular/material/core';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { CertificatesService } from '../core/services/certificates.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-add-ca',
  templateUrl: './add-ca.component.html',
  styleUrls: ['./add-ca.component.scss']
})
export class AddCaComponent implements OnInit {
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  country_codes: string[] = [
    "CF", "TD", "CL", "CN", "CX", "CC", "CO", "KM", "CG", "CD", "CK", "CR", "CI", "CI", "HR", "CU", "CY", "CZ", "DK", "DJ", "DM", "DO", "EC", "EG", "SV", "GQ", "ER", "EE", "ET", "FK", "FO", "FJ", "FI", "FR", "GF", "PF", "TF", "GA", "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GD", "GP", "GU", "GT", "GG", "GN", "GW", "GY", "HT", "HM", "VA", "HN", "HK", "HU", "IS", "IN", "ID", "IR", "IQ", "IE", "IM", "IL", "IT", "JM", "JP", "JE", "JO", "KZ", "KE", "KI", "KP", "KR", "KR", "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LY", "LI", "LT", "LU", "MO", "MK", "MG", "MW", "MY", "MV", "ML", "MT", "MH", "MQ", "MR", "MU", "YT", "MX", "FM", "MD", "MC", "MN", "ME", "MS", "MA", "MZ", "MM", "MM", "NA", "NR", "NP", "NL", "AN", "NC", "NZ", "NI", "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW", "PS", "PA", "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR", "QA", 
    "RE", "RO", "RU", "RU", "RW", "SH", "KN", "LC", "PM", "VC", "VC", "VC", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL", "SG", "SK", "SI", "SB", "SO", "ZA", "GS", "SS", "ES", "LK", "SD", "SR", "SJ", "SZ", "SE", "CH", "SY", "TW", "TW", "TJ", "TZ", "TH", "TL", "TG", "TK", "TO", "TT", "TN", "TR", "TM", "TC", "TV", "UG", "UA", "AE", "GB", "US", "UM", "UY", "UZ", "VU", "VE", "VE", "VN", "VN", "VG", "VI", "WF", "EH", "YE", "ZM", "ZW"
  ]
  filteredOptions: Observable<string[]>;
  maxDateEnd: Date;
  minDateStart: Date;
  minDateEnd: Date;
  maxDateStart: Date;
  pickerDisable = true;
  algorithms = ["sha256WithRSAEncryption", "sha384WithRSAEncryption", "sha512WithRSAEncryption"];
  certificate: any = {commonName: '', organization: '', organizationalUnit: '', cityLocality: '', stateCountyRegion: '', country: '', emailAddress: '', notBefore: '', notAfter: '', signingAlgorithm: '', keyIdentifierExtension: false, alternativeNameExtension: false };

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddCaComponent>,
    private snackBar: Snackbar,
    private certificatesService: CertificatesService,
    private dateAdapter: DateAdapter<any>,
    @Inject(MAT_DIALOG_DATA) private data
  ) { }

  ngOnInit(): void {
    moment.locale('sr');
    this.dateAdapter.setLocale('sr');
    this.minDateStart = new Date();
    this.maxDateEnd = new Date(2027, 4, 6);
    this.maxDateStart = moment(this.maxDateEnd).subtract(1, 'days').toDate();

    this.firstFormGroup = this.fb.group({
      common_name: ["", [Validators.required, Validators.pattern('[a-zA-Z0-9\. ]+')]],
      organization: ["", [Validators.required, Validators.pattern('[a-zA-Z0-9 ]+')]],
      organizational_unit: ["", [Validators.required, Validators.pattern('[a-zA-Z0-9 ]+')]],
      city_locality: ["", [Validators.required, Validators.pattern('[a-zA-Z ]+')]],
      state_county_region: ["",[Validators.required, Validators.pattern('[a-zA-Z ]+')]],
      country: ["", Validators.required],
      email_address: ["", [Validators.required, Validators.email]]
    });

    this.secondFormGroup = this.fb.group({
      not_before: ["", [Validators.required]],
      not_after: ["", [Validators.required]],
      signing_algorithm: [""],
      key_identifier_extension: [false],
      alternative_name_extension: [false]
    });

    this.filter()
  }

  filter() {
    this.filteredOptions = this.firstFormGroup.controls.country.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  public setMinEnd(event: MatDatepickerInputEvent<Date>) {
    this.pickerDisable = false;
    this.minDateEnd = moment(event.value).add(1, 'days').toDate();
  }

  close(): void {
    this.dialogRef.close(false);
  }

  private _filter(value: any): string[] {
    const filterValue = value.toLowerCase();

    return this.country_codes.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

  public key_identifier_check($event) {
    this.secondFormGroup.patchValue({
      key_identifier_extension: $event.checked
    });
  }

  public alternative_name_check($event) {
    this.secondFormGroup.patchValue({
      alternative_name_extension: $event.checked
    });
  }

  first(): void {
    if (this.firstFormGroup.invalid) {
      return;
    }
    this.certificate.commonName = this.firstFormGroup.value.common_name;
    this.certificate.organization = this.firstFormGroup.value.organization;
    this.certificate.organizationalUnit = this.firstFormGroup.value.organizational_unit;
    this.certificate.cityLocality = this.firstFormGroup.value.city_locality;
    this.certificate.stateCountyRegion = this.firstFormGroup.value.state_county_region;
    this.certificate.country = this.firstFormGroup.value.country;
    this.certificate.emailAddress = this.firstFormGroup.value.email_address;
  }

  second(): void {
    if (this.secondFormGroup.invalid) {
      return;
    }
    this.certificate.notBefore = this.secondFormGroup.value.not_before;
    this.certificate.notAfter = this.secondFormGroup.value.not_after;
    this.certificate.signingAlgorithm = this.secondFormGroup.value.signing_algorithm;
    this.certificate.keyIdentifierExtension = this.secondFormGroup.value.key_identifier_extension;
    this.certificate.alternativeNameExtension = this.secondFormGroup.value.alternative_name_extension;
    
    let plus5 = this.secondFormGroup.value.not_before.clone();
    if (this.secondFormGroup.value.not_after > plus5.add('years', 5)) {
      this.snackBar.error("Maximum validity time is five years.");
      return;
    }
    this.certificatesService.addCA(this.certificate).subscribe((data: any) => {
      this.snackBar.success("You have succesfully created CA certificate!");
      this.dialogRef.close(true);
    },
    error => {
      this.snackBar.error(error.error);
    });
  }

}
