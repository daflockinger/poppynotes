import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { UserSecretComponent } from 'src/app/user-secret/user-secret.component';
import { CardEditComponent } from 'src/app/card-edit/card-edit.component';
import { TemplateRef } from '@angular/core/src/linker/template_ref';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],

})
export class NavbarComponent implements OnInit {

  isCollapsed = false;
  bsModalRef: BsModalRef;

  @Output()
  noteCreate: EventEmitter<string> = new EventEmitter<string>();

  constructor(private modalService: BsModalService) {
  }

  ngOnInit() {
  }

  public openUserSecretModal() {
    this.bsModalRef = this.modalService.show(UserSecretComponent);
  }

  public openNoteEditModal(template: TemplateRef<any>) {
    this.bsModalRef = this.modalService.show(template);
  }

  onCreateNote(event: any) {
    this.bsModalRef.hide();
    this.noteCreate.emit(event);
  }
}
