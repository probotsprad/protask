import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserMst } from './user-mst.model';
import { UserMstPopupService } from './user-mst-popup.service';
import { UserMstService } from './user-mst.service';

@Component({
    selector: 'jhi-user-mst-dialog',
    templateUrl: './user-mst-dialog.component.html'
})
export class UserMstDialogComponent implements OnInit {

    userMst: UserMst;
    isSaving: boolean;
    createdOnDp: any;
    updatedOnDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private userMstService: UserMstService,
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
        if (this.userMst.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userMstService.update(this.userMst));
        } else {
            this.subscribeToSaveResponse(
                this.userMstService.create(this.userMst));
        }
    }

    private subscribeToSaveResponse(result: Observable<UserMst>) {
        result.subscribe((res: UserMst) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: UserMst) {
        this.eventManager.broadcast({ name: 'userMstListModification', content: 'OK'});
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
    selector: 'jhi-user-mst-popup',
    template: ''
})
export class UserMstPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userMstPopupService: UserMstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.userMstPopupService
                    .open(UserMstDialogComponent as Component, params['id']);
            } else {
                this.userMstPopupService
                    .open(UserMstDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
