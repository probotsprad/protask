import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SubTasks } from './sub-tasks.model';
import { SubTasksPopupService } from './sub-tasks-popup.service';
import { SubTasksService } from './sub-tasks.service';

@Component({
    selector: 'jhi-sub-tasks-dialog',
    templateUrl: './sub-tasks-dialog.component.html'
})
export class SubTasksDialogComponent implements OnInit {

    subTasks: SubTasks;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private subTasksService: SubTasksService,
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
        if (this.subTasks.id !== undefined) {
            this.subscribeToSaveResponse(
                this.subTasksService.update(this.subTasks));
        } else {
            this.subscribeToSaveResponse(
                this.subTasksService.create(this.subTasks));
        }
    }

    private subscribeToSaveResponse(result: Observable<SubTasks>) {
        result.subscribe((res: SubTasks) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: SubTasks) {
        this.eventManager.broadcast({ name: 'subTasksListModification', content: 'OK'});
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
    selector: 'jhi-sub-tasks-popup',
    template: ''
})
export class SubTasksPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subTasksPopupService: SubTasksPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.subTasksPopupService
                    .open(SubTasksDialogComponent as Component, params['id']);
            } else {
                this.subTasksPopupService
                    .open(SubTasksDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
