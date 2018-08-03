import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SubTask,SubTaskService } from '../sub-task';
import { SubTaskPopupService } from './sub-task-popup.service';

@Component({
    selector: 'jhi-sub-task-dialog',
    templateUrl: './sub-task-dialog.component.html'
})
export class SubTaskDialogComponent implements OnInit {

    subTask: SubTask;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private subTaskService: SubTaskService,
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
        if (this.subTask.id !== undefined) {
            this.subscribeToSaveResponse(
                this.subTaskService.update(this.subTask));
        } else {
            this.subscribeToSaveResponse(
                this.subTaskService.create(this.subTask));
        }
    }

    private subscribeToSaveResponse(result: Observable<SubTask>) {
        result.subscribe((res: SubTask) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SubTask) {
        this.eventManager.broadcast({ name: 'subTaskListModification', content: 'OK'});
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
    selector: 'jhi-sub-task-popup',
    template: ''
})
export class SubTaskPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subTaskPopupService: SubTaskPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.subTaskPopupService
                    .open(SubTaskDialogComponent as Component, params['id']);
            } else {
                this.subTaskPopupService
                    .open(SubTaskDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
