import { CryptoService } from 'src/app/service/crypto/crypto.service';
import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SecretstoreService } from 'src/app/service/crypto/secretstore.service';
import * as _ from 'lodash';
import { HttpResponse } from '@angular/common/http/src/response';

@Injectable()
export class CryptoInterceptor implements HttpInterceptor {

  constructor(private crypto: CryptoService,
    private secretStore: SecretstoreService) { }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.body !== undefined && req.body != null && this.instanceofNoteBody(req.body)) {
      this.encryptNote(req.body);
    }

    return next
      .handle(req)
      .map((response: HttpResponse<SecretNoteBody>) => {
      if (response.body !== undefined && response.body != null) {
        if (Array.isArray(response.body)) {
          response.body
            .filter((note) => this.instanceofNoteBody(note))
            .forEach((note) => this.decryptNote(note));
        } else if (this.instanceofNoteBody(response.body)) {
          this.decryptNote(response.body);
        }
      }
      return response;
    });
  }

  private instanceofNoteBody(body: any): boolean {
    if ('title' in body && 'content' in body && 'initVector' in body) {
      return true;
    } else {
      return false;
    }
  }

  private encryptNote(noteBody: SecretNoteBody) {
    const cryptedBody = new NoteBody();
    cryptedBody.title = this.crypto.encodeUtf8(noteBody.title);
    cryptedBody.content = this.crypto.encodeUtf8(noteBody.content);
    const encryptedNote = this.crypto.encrypt(JSON.stringify(cryptedBody), this.secretStore.getKey(), noteBody.initVector);
    const fakeEncryptedTitle = encryptedNote.substring(0, Math.min(20, encryptedNote.length));
    noteBody.title = fakeEncryptedTitle;
    noteBody.content = encryptedNote;
  }

  private decryptNote(noteBody: SecretNoteBody) {
    try {
      const secretNoteBody = noteBody as SecretNoteBody;
      const decryptedNoteJson = this.crypto.decrypt(secretNoteBody.content, this.secretStore.getKey(), secretNoteBody.initVector);
      const decryptedNote = JSON.parse(decryptedNoteJson) as NoteBody;

      noteBody.content = decryptedNote.content;
      noteBody.title = decryptedNote.title;
    } catch (error) {
      console.log('Decryption failed: ' + error);
    }
  }
}

export class NoteBody {
  title: string;
  content: string;
}

export class SecretNoteBody extends NoteBody {
  initVector: string;
}
