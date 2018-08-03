import { BaseEntity } from './../../shared';

export const enum YesNoFlag {
    'Y',
    'N'
}

export class UserMst implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: string,
        public userEmail?: string,
        public firstName?: string,
        public lastName?: string,
        public active?: YesNoFlag,
        public createdOn?: any,
        public createdBy?: string,
        public updatedOn?: any,
        public updatedBy?: string,
    ) {
    }
}
