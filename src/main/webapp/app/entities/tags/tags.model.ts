import { BaseEntity } from './../../shared';

export class Tags implements BaseEntity {
    constructor(
        public id?: number,
        public tagName?: string,
    ) {
    }
}
