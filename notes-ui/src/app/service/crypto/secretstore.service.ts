import { LocalStorageService } from 'ngx-webstorage';
import { Injectable } from '@angular/core';
import * as forge from 'node-forge';
import * as _ from 'lodash';



@Injectable()
export class SecretstoreService {

  constructor(private localstore: LocalStorageService) { }

  private LOCALSTORE_KEY_NAME = 'userPrivateKey';
  private LOCALSTORE_USERHASH_NAME = 'userHash';

  getKey(): string {
    const hexKey = this.localstore.retrieve(this.LOCALSTORE_KEY_NAME);
    return hexKey;
  }

  storeKey(key: string) {
    this.localstore.store(this.LOCALSTORE_KEY_NAME, key);
  }

  getUserHash(): string {
    return this.localstore.retrieve(this.LOCALSTORE_USERHASH_NAME);
  }

  storeUserHash(userHash: string) {
    this.localstore.store(this.LOCALSTORE_USERHASH_NAME, userHash);
  }

  isUserLoggedOut(): boolean {
   return _.isEmpty(this.getKey()) || _.isEmpty(this.getUserHash());
  }

}
