import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { CardDeckComponent } from './card-deck.component';
import { AngularFontAwesomeComponent } from 'angular-font-awesome';

@Component({selector: 'app-navbar', template: ''})
class NavbarStubComponent {}

@Component({selector: 'app-card', template: ''})
class CardStubComponent {}

@Component({selector: 'app-card-edit', template: ''})
class CardEditStubComponent {}

describe('CardDeckComponent', () => {
  let component: CardDeckComponent;
  let fixture: ComponentFixture<CardDeckComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardDeckComponent,
        NavbarStubComponent,
        CardStubComponent,
        CardEditStubComponent,
        AngularFontAwesomeComponent
       ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDeckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
