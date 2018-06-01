import { SecretstoreService } from './../service/crypto/secretstore.service';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { UserSecretComponent } from 'src/app/user-secret/user-secret.component';
import { CardEditComponent } from 'src/app/card-edit/card-edit.component';
import { TemplateRef } from '@angular/core/src/linker/template_ref';
import { NotesService } from 'src/app/service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  providers: [NotesService, SecretstoreService]
})
export class NavbarComponent implements OnInit {

  isCollapsed = false;
  bsModalRef: BsModalRef;

  @Output()
  noteCreate: EventEmitter<string> = new EventEmitter<string>();

  constructor(private noteService: NotesService,
    private secretStore: SecretstoreService,
    private modalService: BsModalService) {
  }

  ngOnInit() {
    this.checkIfUserAllowed();
  }

  private checkIfUserAllowed() {
    let userHash = this.secretStore.getUserHash();
    if (userHash === null) {
      userHash = '';
    }
    this.noteService.getNotes(userHash, 0, 1).subscribe(notes => {},
      error => {
        this.bsModalRef = this.modalService.show(UserSecretComponent, {
          backdrop: 'static',
          ignoreBackdropClick: true,
          initialState: {
            hideCloseButton: true
          }
        });
    });
  }

  public openUserSecretModal() {
    this.bsModalRef = this.modalService.show(UserSecretComponent, {initialState: {
      hideCloseButton: false
    }});
  }

  public openNoteEditModal(template: TemplateRef<any>) {
    this.bsModalRef = this.modalService.show(template);
  }

  onCreateNote(event: any) {
    this.bsModalRef.hide();
    this.noteCreate.emit(event);
  }
}
