import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Component, OnInit, Inject, Input, Output, EventEmitter } from '@angular/core';
import { SecretstoreService } from 'src/app/service/crypto/secretstore.service';
import { CryptoService } from 'src/app/service/crypto/crypto.service';
import { NotesService, CompleteNote, UpdateNote } from 'src/app/service';
import * as _ from 'lodash';

@Component({
  selector: 'app-card-edit',
  templateUrl: './card-edit.component.html',
  styleUrls: ['./card-edit.component.scss'],
  providers: [CryptoService, SecretstoreService, NotesService]
})
export class CardEditComponent implements OnInit {

  constructor(private crypto: CryptoService,
    private secretStore: SecretstoreService,
    private noteService: NotesService,
    @Inject(FormBuilder) private formBuilder: FormBuilder) { }

  noteForm: FormGroup;

  @Input('noteId')
  noteId: string;

  @Output()
  update: EventEmitter<string> = new EventEmitter<string>();

  @Output()
  removed: EventEmitter<string> = new EventEmitter<string>();

  note: CompleteNote;

  ngOnInit() {
    this.noteForm = this.formBuilder.group({
        title: ['', Validators.required],
        content: ['', Validators.required],
    });

    const userHash = this.secretStore.getUserHash();
    if (!_.isEmpty(userHash) && !_.isEmpty(this.noteId)) {
      this.loadNote(this.noteId, userHash);
    } else {
      this.note = {};
    }
  }

  loadNote(noteId: string, userHash: string) {
    this.noteService.findNote(noteId, userHash).subscribe(note => {
      this.note = note;
      this.updateNoteForm();
    });
  }

  updateNoteForm() {
    this.noteForm.patchValue({
      title: this.note.title,
      content: this.note.content
    });
  }

  encryptAndSaveNote() {
    if (this.note.userHash === undefined) {
      this.note.userHash = this.secretStore.getUserHash();
    }
    this.note.initVector = this.crypto.generateKeyHex();
    this.note.lastEdit = new Date().toISOString();
    this.note.content = this.noteForm.value.content;
    this.note.title = this.noteForm.value.title;

    if (this.note.id !== undefined) {
      this.noteService.updateNote(this.note, this.note.userHash).subscribe((result) => {
        this.update.emit(this.note.id);
      }, (error) => {
        console.log(error);
      });
    } else {
      this.noteService.createNote(this.note, this.note.userHash).subscribe((result) => {
        this.update.emit(result.id);
      }, (error) => {
        console.log(error);
      });
    }
  }

  deleteNote() {
    this.noteService.removeNote(this.noteId, this.secretStore.getUserHash()).
      subscribe((result) => {
        this.removed.emit(this.noteId);
      }, (error) => {
        console.log(error);
      });
  }
}
