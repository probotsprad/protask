import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute,Router } from '@angular/router';
import { Response } from '@angular/http';

import { Observable,Subscription } from 'rxjs/Rx';
//import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Task } from './task.model';
import { TaskPopupService } from './task-popup.service';
import { TaskService } from './task.service';

import { ContextService, Context } from '../context';
import { PoliciesService, Policies } from '../policies';
import { TagsService, Tags } from '../tags';
import { DepartmentService, Department } from '../department';
import { CategoryService, Category } from '../category';
import { ResponseWrapper } from '../../shared';


@Component({
    selector: 'jhi-task-dialog',
    templateUrl: './task-dialog.component.html'
})
export class TaskDialogComponent implements OnInit {

    task: Task;
    isSaving: boolean;
    startDateDp: any;
    endDateDp: any;
    remainderDp: any;
    contexts: Context[];
    departments: Department[];
    categorys: Category[];
    tagsa: Tags[];
    policiesa: Policies[];
    private subscription: Subscription;

    constructor(
        //public activeModal: NgbActiveModal,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private contextService: ContextService,
        private departmentService: DepartmentService,
        private policiesService: PoliciesService,
        private tagsService: TagsService,
        private categoryService: CategoryService,
        private taskService: TaskService,
         private route: ActivatedRoute,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.task = new Task();
        this.subscription = this.route.params.subscribe((params) => {
           if(params['id'] == undefined){
               
           } else {
                this.load(params['id']);
           }
       });
        
        this.contextService.query({
               page: 0,
               size: 1000,
           })
            .subscribe((res: ResponseWrapper) => { this.contexts = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
            
        this.departmentService.query({
               page: 0,
               size: 1000,
           })
            .subscribe((res: ResponseWrapper) => { this.departments = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        
        this.categoryService.query({
               page: 0,
               size: 1000,
           })
            .subscribe((res: ResponseWrapper) => { this.categorys = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
            
        this.tagsService.query({
               page: 0,
               size: 1000,
           })
            .subscribe((res: ResponseWrapper) => { this.tagsa = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
            
        this.policiesService.query({
               page: 0,
               size: 1000,
           })
            .subscribe((res: ResponseWrapper) => { this.policiesa = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    
     load(id) {
        this.taskService.find(id).subscribe((task) => {
           this.task = task;
         
         });
         }

    clear() {
       // this.activeModal.dismiss('cancel');
        this.router.navigate(['/task']);
    }

    save() {
        this.isSaving = true;
        if (this.task.id !== undefined) {
            this.subscribeToSaveResponse(
                this.taskService.update(this.task));
        } else {
            this.subscribeToSaveResponse(
                this.taskService.create(this.task));
        }
    }

    private subscribeToSaveResponse(result: Observable<Task>) {
        result.subscribe((res: Task) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Task) {
        this.eventManager.broadcast({ name: 'taskListModification', content: 'OK'});
        this.isSaving = false;
        //this.activeModal.dismiss(result);
        this.router.navigate(['/task']);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-task-popup',
    template: ''
})
export class TaskPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskPopupService: TaskPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.taskPopupService
                    .open(TaskDialogComponent as Component, params['id']);
            } else {
                this.taskPopupService
                    .open(TaskDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
