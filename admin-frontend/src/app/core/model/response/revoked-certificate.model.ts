export interface RevokedCertificate {
    serialNumber?: number;
    commonName?: string;
    issuer?: string;
    notBefore?: string;
    notAfter?: string;
    revokeDate?: string;
}
