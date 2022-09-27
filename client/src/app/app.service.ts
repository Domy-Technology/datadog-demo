import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { catchError, map, Observable, of, throwError } from "rxjs";
import { Measurements } from "src/model/measurements";
import { LoggingService } from "./logging.service";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private http: HttpClient, private log: LoggingService) { }

  public getServiceStatus(): Observable<string> {
    return this.http.get<any>(`${environment.apiUrl}/actuator/health`)
        .pipe(map(response => response.status))
        .pipe(catchError((error: any) => of("DOWN")))
  }

  public getHttpMetrics(): Observable<Measurements[]> {
    const url = `${environment.apiUrl}/actuator/metrics/http.server.requests`
    return this.http.get<any>(url)
        .pipe(map(response => response.measurements))
        .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public getMongoMetrics(): Observable<Measurements[]> {
    const url = `${environment.apiUrl}/actuator/metrics/mongodb.driver.commands`
    return this.http.get<any>(url)
        .pipe(map(response => response.measurements))
        .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public getApplicationReadyTime(): Observable<number> {
    const url = `${environment.apiUrl}/actuator/metrics/application.ready.time`
    return this.http.get<any>(url)
        .pipe(map(response => response.measurements))
        .pipe(map(response => response[0]?.value ?? 0))
        .pipe(catchError((error: any) => of(0)))
  }

  public getElementCount(): Observable<number> {
    return this.http.get<any>(`${environment.apiUrl}/repo/count`)
        .pipe(map(response => response))
        .pipe(catchError((error: any) => of(0)))
  }

  public demoOk(): Observable<void> {
    const url = `${environment.apiUrl}/demo/ok`
    return this.http.get<void>(url)
      .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public demoFailure(): Observable<void> {
    const url = `${environment.apiUrl}/demo/failure`
    return this.http.get<void>(url)
      .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public slowOk(): Observable<void> {
    const url = `${environment.apiUrl}/slow/ok`
    return this.http.get<void>(url)
      .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public slowFailure(): Observable<void> {
    const url = `${environment.apiUrl}/slow/failure`
    return this.http.get<void>(url)
      .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public cpuLoad(): Observable<string> {
    const url = `${environment.apiUrl}/danger-zone/cpu-destroy/${environment.cpuLoadInteractions}`
    return this.http.post<string>(url, null)
      .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public memoryStress(): Observable<void> {
    const url = `${environment.apiUrl}/danger-zone/gc-destroy`
    return this.http.post<void>(url, null)
      .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public fillData(): Observable<void> {
    const url = `${environment.apiUrl}/repo/random`
    return this.http.post<void>(url, null)
      .pipe(catchError((error: any) => this.handleRequestError(error, null, url)))
  }

  public cpuStress(): Observable<void> {
    const url = `${environment.apiUrl}/danger-zone/cpu-load`
    const body = {
      "numCore": environment.cpuLoadNumCore,
      "numThreadsPerCore": environment.cpuLoadNumThreads,
      "load": environment.cpuLoadLoad,
      "duration": environment.cpuLoadDuration}
    return this.http.post<void>(url, body).pipe(catchError((error: any) => this.handleRequestError(error, body, url)))
  }

  private handleRequestError(error: any, body: any, url: string): Observable<any> {
    this.log.logError('Ocorreu um erro na chamada HTTP', {
      status: error.status,
      body: body,
      url: url
    }, error.stack ?? '')
    if (error.status === 0) {
      console.error('An error occurred:', error.error);
      alert(error.error)
    } else {
      console.error(
        `Backend returned code ${error.status}, body was: ${body}`, error.error);
      alert(`Backend returned code ${error.status}, body was: ${body}`)
    }
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}
