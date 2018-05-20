import { AppComponent } from './../../app.component';
import { Injectable } from '@angular/core';
import * as forge from 'node-forge';
import { LocalStorageService } from 'ngx-webstorage';
import { Bytes } from 'node-forge';

@Injectable()
export class CryptoService {

  constructor() { }

  public CIPHER_ALGO: any = 'AES-CTR';
  public MESSAGE_ENCODING: any = 'utf-8';
  public KEY_SIZE_BYTE = 64; // 64byte -> 512bit Key

  public createHash(sourceContent: string): string {
    const digest = forge.md.sha256.create();
    digest.update(sourceContent);

    return digest.digest().toHex();
  }

  public generateKeyHex(): string {
    return forge.util.bytesToHex(this.generateIv());
  }

  private generateIv(): Bytes {
    return forge.random.getBytesSync(this.KEY_SIZE_BYTE);
  }

  public encrypt(message: string, privateKey: string): any {
    const keyBuffer = new forge.util.ByteStringBuffer(forge.util.hexToBytes(privateKey));
    const cipher = forge.cipher.createCipher(this.CIPHER_ALGO, keyBuffer);
    cipher.start({iv: this.generateIv()});
    cipher.update(forge.util.createBuffer(message, this.MESSAGE_ENCODING));
    cipher.finish();
    return cipher.output.toHex();
  }

  public decrypt(message: string, privateKey: string): string {
    const keyBuffer = new forge.util.ByteStringBuffer(forge.util.hexToBytes(privateKey));
    const payload = forge.util.hexToBytes(message);
    const cipher = forge.cipher.createDecipher(this.CIPHER_ALGO, keyBuffer);
    cipher.start({iv: this.generateIv()});
    cipher.update(new forge.util.ByteStringBuffer(payload));
    cipher.finish();
    return cipher.output.toString();
  }
}
