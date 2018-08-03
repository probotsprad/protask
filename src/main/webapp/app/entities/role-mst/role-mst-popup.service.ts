import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RoleMst } from './role-mst.model';
import { RoleMstService } from './role-mst.service';

@Injectable()
export class RoleMstPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private roleMstService: RoleMstService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.roleMstService.find(id).subscribe((roleMst) => {
                    if (roleMst.createdOn) {
                        roleMst.createdOn = {
                            year: roleMst.createdOn.getFullYear(),
                            month: roleMst.createdOn.getMonth() + 1,
                            day: roleMst.createdOn.getDate()
                        };
                    }
                    if (roleMst.updatedOn) {
                        roleMst.updatedOn = {
                            year: roleMst.updatedOn.getFullYear(),
                            month: roleMst.updatedOn.getMonth() + 1,
                            day: roleMst.updatedOn.getDate()
                        };
                    }
                    this.ngbModalRef = this.roleMstModalRef(component, roleMst);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.roleMstModalRef(component, new RoleMst());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    roleMstModalRef(component: Component, roleMst: RoleMst): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.roleMst = roleMst;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
