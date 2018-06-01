import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AngularFontAwesomeComponent } from 'angular-font-awesome';

import { CardComponent } from './card.component';
import { NotesService, OverviewNote } from 'src/app/service';
import { SecretstoreService } from 'src/app/service/crypto/secretstore.service';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'amTimeAgo'})
class FakeMomentPipe implements PipeTransform {
  transform(value) {}
}

describe('CardComponent', () => {
  let component: CardComponent;
  let fixture: ComponentFixture<CardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardComponent,
        FakeMomentPipe,
        AngularFontAwesomeComponent,
       ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotesService, SecretstoreService],
      declarations: [],
      imports: [],
      schemas: []});
    fixture = TestBed.createComponent(CardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    const note = {
      'id': '1',
      'title': 'note title',
      'content': 'note content',
      'lastEdit': new Date().toISOString(),
      'pinned': true,
      'initVector': '1234'
    };
    component.note = note;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
