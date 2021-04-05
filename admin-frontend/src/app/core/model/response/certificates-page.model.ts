import { Certificate } from "./certificate.model";

export interface CertificatesPage {
    content?: Certificate[];
    totalElements?: number;
}
