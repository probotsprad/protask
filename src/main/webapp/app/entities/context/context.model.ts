import { BaseEntity } from './../../shared';

export class Context implements BaseEntity {
    constructor(
        public id?: number,
        public contextName?: string,
        public contextDescription?: string,
    ) {
    }
}
