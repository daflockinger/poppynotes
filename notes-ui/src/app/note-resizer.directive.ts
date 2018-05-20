import { Directive, ElementRef, Input, HostListener } from '@angular/core';
import { AfterViewInit, AfterViewChecked } from '@angular/core/src/metadata/lifecycle_hooks';

@Directive({
  selector: 'textarea[appResizeABit]'
})
export class NoteResizerDirective implements AfterViewInit, AfterViewChecked {

  private textArea: HTMLElement;
  private readonly PIXEL = 'px';
  private readonly MIN_HEIGHT = '120' + this.PIXEL;
  private clientWidth: number;

  @HostListener('window:resize', ['$event.target'])
  onResize(textArea: HTMLTextAreaElement): void {
    if (this.hasWidthChanged()) {
      this.clientWidth = this.element.nativeElement.clientWidth;
      this.adjust();
    }
  }

  private hasWidthChanged() {
    return this.textArea.clientWidth !== this.clientWidth;
  }

  @HostListener('input', ['$event.target'])
  onInput(textArea: HTMLTextAreaElement): void {
    this.adjust();
  }

  constructor(public element: ElementRef) {
    this.textArea = element.nativeElement;
    this.clientWidth = this.textArea.clientWidth;
    this.textArea.style.minHeight = this.MIN_HEIGHT;
  }

  ngAfterViewInit(): void {
    const style = window.getComputedStyle(this.textArea, null);
    if (style.resize === 'both') {
      this.textArea.style.resize = 'horizontal';
    } else if (style.resize === 'vertical') {
      this.textArea.style.resize = 'none';
    }
    this.adjust();
  }

  ngAfterViewChecked(): void {
    this.ngAfterViewInit();
  }

  adjust(): void {
    if (this.hasHeightChanged()) {
      this.textArea.style.overflow = 'hidden';
      this.textArea.style.height = 'auto';
      this.textArea.style.height = this.textArea.scrollHeight + this.PIXEL;
    }
  }

  private hasHeightChanged() {
    return this.textArea.style.height !== this.element.nativeElement.scrollHeight + this.PIXEL;
  }
}
