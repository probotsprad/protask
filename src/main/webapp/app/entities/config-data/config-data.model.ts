import { BaseEntity } from './../../shared';

export const enum YesNoFlag {
    'Y',
    'N'
}

export class ConfigData implements BaseEntity {
    constructor(
        public id?: number,
        public key?: string,
        public value?: any,
        public encrypted?: YesNoFlag,
        public createdOn?: any,
        public createdBy?: string,
        public updatedOn?: any,
        public updatedBy?: string,
    ) {
    }
}
