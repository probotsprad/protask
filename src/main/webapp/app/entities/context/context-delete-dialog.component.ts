import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Context } from './context.model';
import { ContextPopupService } from './context-popup.service';
import { ContextService } from './context.service';

@Component({
    selector: 'jhi-context-delete-dialog',
    templateUrl: './context-delete-dialog.component.html'
})
export class ContextDeleteDialogComponent {

    context: Context;

    constructor(
        private contextService: ContextService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contextService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contextListModification',
                content: 'Deleted an context'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-context-delete-popup',
    template: ''
})
export class ContextDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contextPopupService: ContextPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.contextPopupService
                .open(ContextDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
