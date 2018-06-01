import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AngularFontAwesomeComponent } from 'angular-font-awesome';


import { UserSecretComponent } from './user-secret.component';

describe('UserSecretComponent', () => {
  let component: UserSecretComponent;
  let fixture: ComponentFixture<UserSecretComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserSecretComponent,
        AngularFontAwesomeComponent
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserSecretComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
