import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SubTask } from './sub-task.model';
import { SubTaskPopupService } from './sub-task-popup.service';
import { SubTaskService } from './sub-task.service';

@Component({
    selector: 'jhi-sub-task-delete-dialog',
    templateUrl: './sub-task-delete-dialog.component.html'
})
export class SubTaskDeleteDialogComponent {

    subTask: SubTask;

    constructor(
        private subTaskService: SubTaskService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.subTaskService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'subTaskListModification',
                content: 'Deleted an subTask'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sub-task-delete-popup',
    template: ''
})
export class SubTaskDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subTaskPopupService: SubTaskPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.subTaskPopupService
                .open(SubTaskDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
