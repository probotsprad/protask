import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Policies } from './policies.model';
import { PoliciesPopupService } from './policies-popup.service';
import { PoliciesService } from './policies.service';

@Component({
    selector: 'jhi-policies-delete-dialog',
    templateUrl: './policies-delete-dialog.component.html'
})
export class PoliciesDeleteDialogComponent {

    policies: Policies;

    constructor(
        private policiesService: PoliciesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.policiesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'policiesListModification',
                content: 'Deleted an policies'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-policies-delete-popup',
    template: ''
})
export class PoliciesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private policiesPopupService: PoliciesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.policiesPopupService
                .open(PoliciesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
