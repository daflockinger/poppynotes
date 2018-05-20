import { SecretstoreService } from 'src/app/service/crypto/secretstore.service';
import { BrowserXhr } from '@angular/http';
import { CustomCorsExtension } from './service/custom-cors-extension';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { CommonModule, HashLocationStrategy, LocationStrategy } from '@angular/common';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { MomentModule } from 'ngx-moment';
import { ModalModule } from 'ngx-bootstrap';
import {Ng2Webstorage, LocalStorage} from 'ngx-webstorage';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CardDeckComponent } from './card-deck/card-deck.component';
import { CardComponent } from './card-deck/card/card.component';
import { UserSecretComponent } from './user-secret/user-secret.component';
import { CardEditComponent } from './card-edit/card-edit.component';
import { NoteResizerDirective } from './note-resizer.directive';
import { httpInterceptorProviders } from 'src/app/service';
import { CryptoService } from 'src/app/service/crypto/crypto.service';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CardDeckComponent,
    CardComponent,
    UserSecretComponent,
    CardEditComponent,
    NoteResizerDirective
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AngularFontAwesomeModule,
    CommonModule,
    HttpClientModule,
    HttpClientXsrfModule,
    MomentModule,
    ModalModule.forRoot(),
    Ng2Webstorage,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    {provide: BrowserXhr, useClass: CustomCorsExtension},
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    httpInterceptorProviders,
    CryptoService,
    SecretstoreService
  ],
  entryComponents: [
    UserSecretComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
