import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Policies } from './policies.model';
import { PoliciesPopupService } from './policies-popup.service';
import { PoliciesService } from './policies.service';

@Component({
    selector: 'jhi-policies-dialog',
    templateUrl: './policies-dialog.component.html'
})
export class PoliciesDialogComponent implements OnInit {

    policies: Policies;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private policiesService: PoliciesService,
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
        if (this.policies.id !== undefined) {
            this.subscribeToSaveResponse(
                this.policiesService.update(this.policies));
        } else {
            this.subscribeToSaveResponse(
                this.policiesService.create(this.policies));
        }
    }

    private subscribeToSaveResponse(result: Observable<Policies>) {
        result.subscribe((res: Policies) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Policies) {
        this.eventManager.broadcast({ name: 'policiesListModification', content: 'OK'});
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
    selector: 'jhi-policies-popup',
    template: ''
})
export class PoliciesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private policiesPopupService: PoliciesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.policiesPopupService
                    .open(PoliciesDialogComponent as Component, params['id']);
            } else {
                this.policiesPopupService
                    .open(PoliciesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
