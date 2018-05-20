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
  public KEY_SIZE_BYTE = 32; // 32byte -> 256bit Key

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

  encodeUtf8(text: string): string {
    return forge.util.encodeUtf8(text);
  }

  decodeUtf8(text: string): string {
    return forge.util.decodeUtf8(text);
  }

  public encrypt(message: string, privateKey: string, initVectorHex: string): any {
    const keyBuffer = new forge.util.ByteStringBuffer(forge.util.hexToBytes(privateKey));
    const cipher = forge.cipher.createCipher(this.CIPHER_ALGO, keyBuffer);
    cipher.start({iv: forge.util.hexToBytes(initVectorHex)});
    cipher.update(forge.util.createBuffer(message, this.MESSAGE_ENCODING));
    cipher.finish();
    return forge.util.encode64(cipher.output.getBytes()); // .toHex();
  }

  public decrypt(message: string, privateKey: string, initVectorHex: string): string {
    const keyBuffer = new forge.util.ByteStringBuffer(forge.util.hexToBytes(privateKey));
    const payload = forge.util.decode64(message); // forge.util.hexToBytes(message);
    const cipher = forge.cipher.createDecipher(this.CIPHER_ALGO, keyBuffer);
    cipher.start({iv: forge.util.hexToBytes(initVectorHex)});
    cipher.update(new forge.util.ByteStringBuffer(payload));
    cipher.finish();
    return cipher.output.toString();
  }
}
