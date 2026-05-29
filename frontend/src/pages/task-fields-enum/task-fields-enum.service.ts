import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { TaskFieldsEnumModel } from './task-fields-enum.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';

export interface Response {
    result: TaskFieldsEnumModel[];
    status: string;
    message: string;
}

@Injectable({
    providedIn: 'root'
})
export class TaskFieldsEnumService {

    constructor(private http: HttpClient) { }

    private apiUrl = 'http://' + window.location.hostname + ':8081/api/TaskFieldsEnum';

    // CORRECCIÓN 1: Método dinámico que extrae el token en tiempo real para cada petición
    private getHttpOptions() {
        const token = localStorage.getItem('app_token');
        let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return { headers };
    }

    private log(message: string) {
        console.log(message);
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error); 
            this.log(`${operation} failed: ${error.message}`);
            return of(result as T);
        };
    }

    serviceError: Response = { result: [], status: "error - network", message: "" };

    // Create TaskFieldsEnum
    create(item: TaskFieldsEnumModel): Observable<Response> {
        // CORRECCIÓN 2: Usamos el método dinámico ()
        return this.http.post<Response>(this.apiUrl, item, this.getHttpOptions())
            .pipe(
                tap((response: Response) => this.log(`TaskFieldsEnum.create(${response.status})`)),
                catchError(this.handleError<Response>('TaskFieldsEnum.create()', this.serviceError))
            );
    }

    // Read TaskFieldsEnum collection
    read(): Observable<Response> {
        // CORRECCIÓN 3: Añadimos getHttpOptions() que faltaba en el GET
        return this.http.get<Response>(this.apiUrl, this.getHttpOptions())
            .pipe(
                tap(_ => this.log('TaskFieldsEnum.read()')),
                catchError(this.handleError<Response>('TaskFieldsEnum.read()', this.serviceError))
            );
    }

    // Read TaskFieldsEnum by id
    find(task_fields_enum_id: number): Observable<Response> {
        const url = `${this.apiUrl}/${task_fields_enum_id}`;
        // CORRECCIÓN 4: Añadimos getHttpOptions() en el GET por ID
        return this.http.get<Response>(url, this.getHttpOptions())
            .pipe(
                tap(_ => this.log(`TaskFieldsEnum.find(${task_fields_enum_id})`)),
                catchError(this.handleError<Response>(`TaskFieldsEnum.find(${task_fields_enum_id})`, this.serviceError))
            );
    }

    // Update TaskFieldsEnum
    update(item: TaskFieldsEnumModel): Observable<Response> {
        return this.http.put<Response>(this.apiUrl, item, this.getHttpOptions())
            .pipe(
                tap(_ => this.log(`TaskFieldsEnum.update(${item.task_fields_enum_id})`)),
                catchError(this.handleError<Response>(`TaskFieldsEnum.update(${item.task_fields_enum_id})`, this.serviceError))
            );
    }

    // Delete TaskFieldsEnum
    delete(task_fields_enum_id: number): Observable<Response> {
        const url = `${this.apiUrl}/${task_fields_enum_id}`;
        return this.http.delete<Response>(url, this.getHttpOptions()).pipe(
            tap(_ => this.log(`TaskFieldsEnum.delete(${task_fields_enum_id})`)),
            catchError(this.handleError<Response>(`TaskFieldsEnum.delete(${task_fields_enum_id})`, this.serviceError))
        );
    }
}
