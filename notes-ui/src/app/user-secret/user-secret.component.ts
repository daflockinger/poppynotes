import { SecretstoreService } from './../service/crypto/secretstore.service';
import { CryptoService } from './../service/crypto/crypto.service';
import { Component, OnInit, Inject } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { keyframes } from '@angular/animations/src/animation_metadata';
import { FormBuilder, FormControl, FormGroup, Validators, AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-user-secret',
  templateUrl: './user-secret.component.html',
  styleUrls: ['./user-secret.component.scss'],
  providers: [CryptoService, SecretstoreService]
})
export class UserSecretComponent implements OnInit {

  constructor(public bsModalRef: BsModalRef, private crypto: CryptoService,
    private secretStore: SecretstoreService,
    @Inject(FormBuilder) private formBuilder: FormBuilder) { }

  secretForm: FormGroup;

  keyEditModeDisabled = true;
  userKey = '';

  ngOnInit() {
    this.userKey = this.secretStore.getKey();

    this.secretForm = this.formBuilder.group({
      user: this.formBuilder.group({
        userKey: ['', Validators.required],
      })
    });
    this.disableEditKeyMode();
    if (!this.secretStore.isUserLoggedOut()) {
      this.updateUserForm();
    }
  }

  updateUserForm() {
    this.secretForm.controls.user.patchValue({
      userKey: this.userKey
    });
  }

  enableEditKeyMode() {
    this.keyEditModeDisabled = false;
    this.secretForm.controls.user.get('userKey').enable();
  }
  disableEditKeyMode() {
    this.keyEditModeDisabled = true;
    this.secretForm.controls.user.get('userKey').disable();
  }

  createUser() {
    this.userKey = this.crypto.generateKeyHex();
    this.secretStore.storeKey(this.userKey);
    const userHash = this.crypto.createHash(this.userKey);
    this.secretStore.storeUserHash(userHash);
    this.updateUserForm();
  }

  updateUser() {
    this.userKey = this.secretForm.value.user.userKey;

    this.secretStore.storeKey(this.userKey);
    const userHash = this.crypto.createHash(this.userKey);
    this.secretStore.storeUserHash(userHash);
    this.updateUserForm();
    this.disableEditKeyMode();
  }

  public isUserLoggedOut(): boolean {
    return this.secretStore.isUserLoggedOut();
  }
}
