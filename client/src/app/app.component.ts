import { Component, OnInit } from '@angular/core';
import { Measurements } from 'src/model/measurements';
import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'demo-app';
  status = '';
  countElements = 0;
  applicationReadyTime = 0;
  httpMetrics: Measurements[] = []
  mongoMetrics: Measurements[] = []

  constructor (private service: AppService) {}

  ngOnInit(): void {
    this.setStatus();
    this.setHttpMetrics();
    this.setMongoMetrics();
    this.setElementCount();
    this.setApplicationReadyTime();
  }

  setStatus = () => {
    this.service.getServiceStatus()
      .subscribe({
        next: (_status => this.status = _status)
      })
  }

  setHttpMetrics = () => {
    this.service.getHttpMetrics()
      .subscribe({
        next: (_response => this.httpMetrics = _response)
      })
  }

  setMongoMetrics = () => {
    this.service.getMongoMetrics()
      .subscribe({
        next: (_response => this.mongoMetrics = _response)
      })
  }

  setElementCount = () => {
    this.service.getElementCount()
      .subscribe({
        next: (_response => this.countElements = _response)
      })
  }

  setApplicationReadyTime = () => {
    this.service.getApplicationReadyTime()
      .subscribe({
        next: (_response => this.applicationReadyTime = _response)
      })
  }

  dispatchOkTest = () => {
    this.service.demoOk()
      .subscribe({})
  }

  dispatchFailureTest = () => {
    this.service.demoFailure()
    .subscribe({})
  }

  dispatchSlowOkTest = () => {
    this.service.slowOk()
    .subscribe({})
  }

  dispatchSlowFailureTest = () => {
    this.service.slowFailure()
    .subscribe({})
  }

  dispatchCpuLoad = () => {
    alert("Processo Disparado")
    this.service.cpuLoad()
      .subscribe({
        complete: () => alert("CPU Load Finalizado")
      })
  }

  dispatchCpuStress = () => {
    alert("Processo Disparado")
    this.service.cpuStress()
      .subscribe({
        complete: () => alert("CPU Stress Finalizado")
      })
  }

  dispatchMemoryStress = () => {
    alert("Processo Disparado")
    this.service.memoryStress()
      .subscribe({
        complete: () => alert("Memory Stress Finalizado")
      })
  }
}
