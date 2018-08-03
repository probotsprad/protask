import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Context } from './context.model';
import { ContextPopupService } from './context-popup.service';
import { ContextService } from './context.service';

@Component({
    selector: 'jhi-context-dialog',
    templateUrl: './context-dialog.component.html'
})
export class ContextDialogComponent implements OnInit {

    context: Context;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private contextService: ContextService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.context.id !== undefined) {
            this.subscribeToSaveResponse(
                this.contextService.update(this.context));
        } else {
            this.subscribeToSaveResponse(
                this.contextService.create(this.context));
        }
    }

    private subscribeToSaveResponse(result: Observable<Context>) {
        result.subscribe((res: Context) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Context) {
        this.eventManager.broadcast({ name: 'contextListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-context-popup',
    template: ''
})
export class ContextPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contextPopupService: ContextPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.contextPopupService
                    .open(ContextDialogComponent as Component, params['id']);
            } else {
                this.contextPopupService
                    .open(ContextDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
