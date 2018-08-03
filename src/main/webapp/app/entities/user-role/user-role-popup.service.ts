import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserRole } from './user-role.model';
import { UserRoleService } from './user-role.service';

@Injectable()
export class UserRolePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private userRoleService: UserRoleService

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
                this.userRoleService.find(id).subscribe((userRole) => {
                    if (userRole.createdOn) {
                        userRole.createdOn = {
                            year: userRole.createdOn.getFullYear(),
                            month: userRole.createdOn.getMonth() + 1,
                            day: userRole.createdOn.getDate()
                        };
                    }
                    if (userRole.updatedOn) {
                        userRole.updatedOn = {
                            year: userRole.updatedOn.getFullYear(),
                            month: userRole.updatedOn.getMonth() + 1,
                            day: userRole.updatedOn.getDate()
                        };
                    }
                    this.ngbModalRef = this.userRoleModalRef(component, userRole);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.userRoleModalRef(component, new UserRole());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    userRoleModalRef(component: Component, userRole: UserRole): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.userRole = userRole;
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
