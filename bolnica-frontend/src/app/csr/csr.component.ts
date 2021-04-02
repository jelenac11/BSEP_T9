import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import {filter, map, startWith} from 'rxjs/operators';
import { CsrService } from '../core/services/csr.service';
import { Snackbar } from '../snackbar';

@Component({
  selector: 'app-csr',
  templateUrl: './csr.component.html',
  styleUrls: ['./csr.component.scss']
})
export class CsrComponent implements OnInit {
  csrForm: FormGroup;
  submitted = false;
  country_codes: string[] = [
    "CF", "TD", "CL", "CN", "CX", "CC", "CO", "KM", "CG", "CD", "CK", "CR", "CI", "CI", "HR", "CU", "CY", "CZ", "DK", "DJ", "DM", "DO", "EC", "EG", "SV", "GQ", "ER", "EE", "ET", "FK", "FO", "FJ", "FI", "FR", "GF", "PF", "TF", "GA", "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GD", "GP", "GU", "GT", "GG", "GN", "GW", "GY", "HT", "HM", "VA", "HN", "HK", "HU", "IS", "IN", "ID", "IR", "IQ", "IE", "IM", "IL", "IT", "JM", "JP", "JE", "JO", "KZ", "KE", "KI", "KP", "KR", "KR", "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LY", "LI", "LT", "LU", "MO", "MK", "MG", "MW", "MY", "MV", "ML", "MT", "MH", "MQ", "MR", "MU", "YT", "MX", "FM", "MD", "MC", "MN", "ME", "MS", "MA", "MZ", "MM", "MM", "NA", "NR", "NP", "NL", "AN", "NC", "NZ", "NI", "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW", "PS", "PA", "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR", "QA", 
    "RE", "RO", "RU", "RU", "RW", "SH", "KN", "LC", "PM", "VC", "VC", "VC", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL", "SG", "SK", "SI", "SB", "SO", "ZA", "GS", "SS", "ES", "LK", "SD", "SR", "SJ", "SZ", "SE", "CH", "SY", "TW", "TW", "TJ", "TZ", "TH", "TL", "TG", "TK", "TO", "TT", "TN", "TR", "TM", "TC", "TV", "UG", "UA", "AE", "GB", "US", "UM", "UY", "UZ", "VU", "VE", "VE", "VN", "VN", "VG", "VI", "WF", "EH", "YE", "ZM", "ZW"
  ]
  filteredOptions: Observable<string[]>;

  constructor(
    private formBuilder: FormBuilder,
    private snackBar: Snackbar,
    private router: Router,
    private csrService: CsrService
  ) { }

  ngOnInit(): void {
    this.csrForm = this.formBuilder.group({
      common_name: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9\. ]+')]],
      organization: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9 ]+')]],
      organizational_unit: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9 ]+')]],
      city_locality: ['', [Validators.required, Validators.pattern('[a-zA-Z ]+')]],
      state_county_region: ['',[Validators.required, Validators.pattern('[a-zA-Z ]+')]],
      country: ['', Validators.required],
      email_address: ['', [Validators.required, Validators.email]]
    });
    this.filter()
  }

  filter() {
    this.filteredOptions = this.csrForm.controls.country.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }
  get f(): { [key: string]: AbstractControl; } { return this.csrForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    if (this.csrForm.invalid) {
      return;
    }
    const csr: any = { common_name: '', organization: '', organizational_unit: '', city_locality: '', state_county_region: '', country: '', email_address: '' };
    csr.common_name = this.csrForm.value.common_name;
    csr.organization = this.csrForm.value.organization;
    csr.organizational_unit = this.csrForm.value.organizational_unit;
    csr.city_locality = this.csrForm.value.city_locality;
    csr.state_county_region = this.csrForm.value.state_county_region;
    csr.country = this.csrForm.value.country;
    csr.email_address = this.csrForm.value.email_address;
    this.csrService.requestCertificate(csr).subscribe((data: any) => {
      this.snackBar.success("Request sent successfully!");
    },
    error => {
      this.snackBar.error(error.error);
    });
  }

  private _filter(value: any): string[] {
    const filterValue = value.toLowerCase();

    return this.country_codes.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }

}
