import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSecretComponent } from './user-secret.component';

describe.skip('UserSecretComponent', () => {
  let component: UserSecretComponent;
  let fixture: ComponentFixture<UserSecretComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserSecretComponent ]
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
