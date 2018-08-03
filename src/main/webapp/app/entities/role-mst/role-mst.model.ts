import { BaseEntity } from './../../shared';

export class RoleMst implements BaseEntity {
    constructor(
        public id?: number,
        public roleName?: string,
        public roleDesc?: string,
        public createdOn?: any,
        public createdBy?: string,
        public updatedOn?: any,
        public updatedBy?: string,
        public roleNames?: BaseEntity[],
    ) {
    }
}
