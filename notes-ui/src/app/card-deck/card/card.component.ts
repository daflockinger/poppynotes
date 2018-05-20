import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { OverviewNote, NotesService, PinNote} from 'src/app/service';
import { SecretstoreService } from 'src/app/service/crypto/secretstore.service';
import * as _ from 'lodash';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  providers: [NotesService, SecretstoreService]
})
export class CardComponent implements OnInit {

  @Input('note') note: OverviewNote;
  @Output('triggerEdit') triggerEdit: EventEmitter<string> = new EventEmitter<string>();
  @Output('resortAnchored') resortAnchored: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private noteService: NotesService, private secretStore: SecretstoreService) { }

  ngOnInit() {
  }

  triggerOpenNoteEdit(noteId: string) {
    this.triggerEdit.emit(noteId);
  }

  anchorNote(doAnchor: boolean) {
     this.noteService.pinNote({
      pinIt: doAnchor,
      noteId: this.note.id
     }, this.secretStore.getUserHash()).subscribe((result) => {
       this.note.pinned = doAnchor;
       this.resortAnchored.emit(doAnchor);
     });
  }
}
