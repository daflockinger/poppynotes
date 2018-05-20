import { UpdateNote } from './../service/model/updateNote';
import { CryptoService } from 'src/app/service/crypto/crypto.service';
import { CardEditComponent } from './../card-edit/card-edit.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { OverviewNote, NotesService, DEFAULT_PAGE, DEFAULT_SIZE } from 'src/app/service';
import { Component, OnInit, Inject, TemplateRef } from '@angular/core';
import * as _ from 'lodash';
import { SecretstoreService } from 'src/app/service/crypto/secretstore.service';


@Component({
  selector: 'app-card-deck',
  templateUrl: './card-deck.component.html',
  styleUrls: ['./card-deck.component.scss'],
  providers: [NotesService, CryptoService, SecretstoreService]
})
export class CardDeckComponent implements OnInit {

  notes: OverviewNote[];
  bsModalRef: BsModalRef;
  currentNoteId: string;

  constructor(private noteService: NotesService,
    private crypto: CryptoService,
    private secretStore: SecretstoreService,
    private modalService: BsModalService) { }

  ngOnInit() {
    this.noteService.getNotes(this.getUserHash(), DEFAULT_PAGE, DEFAULT_SIZE).subscribe(notes => {
      this.notes = notes;
    }, error => {
      console.log(error.message);
    });
  }

  public fetchNext() {
    const pageNumber: number = this.calculateThisPage();
    this.noteService.getNotes(this.getUserHash(), pageNumber, DEFAULT_SIZE).subscribe(notes => {
      this.notes = this.notes.concat(notes);
    }, error => {
      console.log(error.message);
    });
  }

  private getUserHash() {
    return this.secretStore.getUserHash();
  }

  private calculateThisPage(): number {
    return Math.ceil(this.notes.length / DEFAULT_SIZE);
  }

  public openNoteEditModal(template: TemplateRef<any>, triggetNoteEvent: any) {
    this.currentNoteId = triggetNoteEvent;
    this.bsModalRef = this.modalService.show(template);
  }

  onCardDeckUpdate(event: any) {
    if (this.bsModalRef !== undefined) {
      this.bsModalRef.hide();
    }
    const userHash = this.secretStore.getUserHash();
    const updateIndex = _.findIndex(this.notes, {
      id: event
    });
    this.noteService.findNote(event, userHash).subscribe((result) => {
      if (updateIndex !== -1) {
        this.notes.splice(updateIndex, 1, result);
      } else {
        this.notes.unshift(result);
      }
      this.sortNotes();
    });
  }

  onCardDeckRemoval(event: any) {
    this.bsModalRef.hide();
    _.remove(this.notes, {
      id: event
    });
  }

  resortAnchored(event: any) {
    this.sortNotes();
  }

  private sortNotes() {
    this.notes = _.orderBy(this.notes, [({ pinned }) => pinned || false, ({ lastEdit }) => new Date(lastEdit)], ['desc', 'desc']);
  }
}
