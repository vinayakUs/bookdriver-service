// import { Directive, ElementRef, EventEmitter, Output, HostListener } from '@angular/core';
//
// @Directive({
//   selector: '[appClickOutside]'
// })
// export class ClickOutsideDirective {
//
//   constructor(private _elementRef: ElementRef) {
//   }
//
//   @Output()
//   public clickOutside = new EventEmitter<MouseEvent>();
//
//   @HostListener('document:click', ['$event', '$event.target'])
//   public onClick(event: MouseEvent, targetElement: HTMLElement): void {
//     debugger
//     if (!targetElement) {
//       return;
//     }
//
//     const clickedInside = this._elementRef.nativeElement.contains(targetElement);
//     if (!clickedInside) {
//       this.clickOutside.emit(event);
//     }
//   }
//
// }
