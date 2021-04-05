import { RevokedCertificate } from "./revoked-certificate.model";

export interface RevokedCertificatesPage {
    content?: RevokedCertificate[];
    totalElements?: number;
}
