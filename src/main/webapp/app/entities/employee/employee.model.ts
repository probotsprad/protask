import { BaseEntity } from './../../shared';

export class Employee implements BaseEntity {
    constructor(
        public id?: number,
        public staffName?: string,
        public dateOfBirth?: any,
        public gender?: string,
        public emailId?: string,
        public qualification?: string,
        public designation?: string,
        public experience?: string,
        public phoneNo?: number,
        public userName?: string,
        public password?: string,
    ) {
    }
}
