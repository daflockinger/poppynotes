import { BsModalService } from 'ngx-bootstrap/modal';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AngularFontAwesomeComponent } from 'angular-font-awesome';
import { Component } from '@angular/core';

import { NavbarComponent } from './navbar.component';

@Component({selector: 'app-card-edit', template: ''})
class CardEditStubComponent {}

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavbarComponent,
        CardEditStubComponent,
        AngularFontAwesomeComponent ],
      providers: [BsModalService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
