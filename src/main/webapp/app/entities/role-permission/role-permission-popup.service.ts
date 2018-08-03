import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RolePermission } from './role-permission.model';
import { RolePermissionService } from './role-permission.service';

@Injectable()
export class RolePermissionPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private rolePermissionService: RolePermissionService

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
                this.rolePermissionService.find(id).subscribe((rolePermission) => {
                    if (rolePermission.createdOn) {
                        rolePermission.createdOn = {
                            year: rolePermission.createdOn.getFullYear(),
                            month: rolePermission.createdOn.getMonth() + 1,
                            day: rolePermission.createdOn.getDate()
                        };
                    }
                    if (rolePermission.updatedOn) {
                        rolePermission.updatedOn = {
                            year: rolePermission.updatedOn.getFullYear(),
                            month: rolePermission.updatedOn.getMonth() + 1,
                            day: rolePermission.updatedOn.getDate()
                        };
                    }
                    this.ngbModalRef = this.rolePermissionModalRef(component, rolePermission);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.rolePermissionModalRef(component, new RolePermission());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    rolePermissionModalRef(component: Component, rolePermission: RolePermission): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.rolePermission = rolePermission;
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
