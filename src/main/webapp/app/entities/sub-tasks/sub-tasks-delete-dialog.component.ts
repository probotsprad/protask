import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SubTasks } from './sub-tasks.model';
import { SubTasksPopupService } from './sub-tasks-popup.service';
import { SubTasksService } from './sub-tasks.service';

@Component({
    selector: 'jhi-sub-tasks-delete-dialog',
    templateUrl: './sub-tasks-delete-dialog.component.html'
})
export class SubTasksDeleteDialogComponent {

    subTasks: SubTasks;

    constructor(
        private subTasksService: SubTasksService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.subTasksService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'subTasksListModification',
                content: 'Deleted an subTasks'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sub-tasks-delete-popup',
    template: ''
})
export class SubTasksDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private subTasksPopupService: SubTasksPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.subTasksPopupService
                .open(SubTasksDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
